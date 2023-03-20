package Message;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

public class QueueUrls extends UnicastRemoteObject implements IQueueRemoteInterface {
    //     public static BlockingQueue<String> Urls_To_Downloaders = new LinkedBlockingQueue<>();
    public static Queue<String> Urls_To_Downloaders = new ConcurrentLinkedQueue<>();

    public static ISearcheQueue Ligacao;
    public static ArrayList<IQueueRemoteInterface> DownloadersOn = new ArrayList<IQueueRemoteInterface>();
    public static ArrayList<Integer> DownloadersOnPORTA = new ArrayList<Integer>();

    public QueueUrls() throws RemoteException {
        super();
    }





    public boolean ConnectDownload(IQueueRemoteInterface iq,int porta) throws RemoteException {

        for (int i = 0; i < DownloadersOnPORTA.size(); i++) {
            if (DownloadersOnPORTA.get(i) == porta) {
                return false;
            }
            if (i == DownloadersOnPORTA.size() - 1) {
                DownloadersOnPORTA.add(porta);
                DownloadersOn.add(iq);
                Ligacao.SendInfoDownloaders(DownloadersOnPORTA);
                return true;
            }
        }
        return true;

    }

    public synchronized String retira() {
        String ret = null;
        if (!Urls_To_Downloaders.isEmpty()) {
            System.out.println("URL retirado");
            ret = Urls_To_Downloaders.peek();
            Urls_To_Downloaders.remove();
        }
        return ret;
    }

    public synchronized void coloca(String e) {
        Urls_To_Downloaders.add(e);
        System.out.println("URL adicionado");
    }

    public static void main(String[] args) throws RemoteException {

        //Urls_To_Downloaders.add("https://www.uc.pt");

        QueueUrls h = new QueueUrls();
        Registry r = LocateRegistry.createRegistry(7003);
        Ligacao = (ISearcheQueue) LocateRegistry.createRegistry(7005);

        r.rebind("QD", h);

    }
}
