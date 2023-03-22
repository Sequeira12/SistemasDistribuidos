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

    public static MulticastServer mult;
    public static ISearcheQueue Ligacao;
    public static ArrayList<IQueueRemoteInterface> DownloadersOn = new ArrayList<IQueueRemoteInterface>();
    public static ArrayList<Integer> DownloadersOnPORTA = new ArrayList<Integer>();

    public QueueUrls() throws RemoteException {
        super();
    }

    public ArrayList<Integer> info() {
        return DownloadersOnPORTA;
    }

    public boolean ConnectDownload(IQueueRemoteInterface iq, int porta) throws RemoteException {

        System.out.println("ENTROU\n");
        for (int i = 0; i < DownloadersOnPORTA.size(); i++) {
            if (DownloadersOnPORTA.get(i) == porta) {

                return false;
            }
            if (i == DownloadersOnPORTA.size() - 1) {
                DownloadersOnPORTA.add(porta);
                DownloadersOn.add(iq);
                System.out.println(Ligacao);
                System.out.printf("DOWNALOADER PORTA: %d conectado!!\n", porta);
                //  System.out.println(Ligacao);
                Ligacao.SendInfoDownloaders(DownloadersOnPORTA);

                return true;
            }
        }
        if (DownloadersOnPORTA.size() == 0) {
            DownloadersOnPORTA.add(porta);
            DownloadersOn.add(iq);

            System.out.println(Ligacao);
            System.out.printf("DOWNALOADER PORTA: %d conectado TAMANHO %d!!\n", porta, DownloadersOnPORTA.size());
            Ligacao.SendInfoDownloaders(DownloadersOnPORTA);
            System.out.println("MORRE AQUI\n");
            return true;
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
        r.rebind("QD", h);


        Ligacao = new MessageServerInterfaceServer();
        Registry r1 = LocateRegistry.createRegistry(7005);
        r1.rebind("QS", Ligacao);

    }
}
