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

    public static ArrayList<InterfaceDownloaders> Downloaders = new ArrayList<>();



    public QueueUrls() throws RemoteException {
        super();
    }


    public void unregisterDownloader(int posicao) throws RemoteException {
        int porta = DownloadersOnPORTA.get(posicao);
        Downloaders.remove(posicao);

        DownloadersOnPORTA.remove(posicao);

        for (int k = posicao; k < DownloadersOnPORTA.size() - 1; k++) {
            DownloadersOnPORTA.set(k, DownloadersOnPORTA.get(k + 1));

            Downloaders.set(k, Downloaders.get(k + 1));

            if (k == DownloadersOnPORTA.size() - 1) {
                DownloadersOnPORTA.set(k, null);
                Downloaders.set(k, null);

            }
        }
        System.out.printf("Downloader com porta %d removido do servidor.\n", porta);
    }

    public ArrayList<InterfaceDownloaders> info() {
        return Downloaders;
    }


    public boolean ConnectDownload(InterfaceDownloaders iq, int porta) throws RemoteException {

        System.out.println("ENTROU\n");
        for (int i = 0; i < DownloadersOnPORTA.size(); i++) {
            if (DownloadersOnPORTA.get(i) == porta) {

                return false;
            }
            if (i == DownloadersOnPORTA.size() - 1) {

                System.out.printf("DOWNALOADER PORTA: %d conectado!!\n", porta);
                DownloadersOnPORTA.add(porta);
                Ligacao.SendInfoDownloaders(DownloadersOnPORTA);
                Downloaders.add(iq);

                return true;
            }
        }
        if (DownloadersOnPORTA.size() == 0) {
            DownloadersOnPORTA.add(porta);
            System.out.println(Ligacao);
            System.out.printf("DOWNALOADER PORTA: %d conectado TAMANHO %d!!\n", porta, DownloadersOnPORTA.size());
            Ligacao.SendInfoDownloaders(DownloadersOnPORTA);
            Downloaders.add(iq);

            return true;
        }
        return true;

    }

    public void run() {
        int i = 0;

        while (true) {
            try {
                System.out.println("Nº de Downloaders" + Downloaders.size());
                for (i = 0; i < Downloaders.size(); i++) {
                    Downloaders.get(i).Connected();
                }

                Thread.sleep(1000);
            } catch (RemoteException e) {
                try {
                    unregisterDownloader(i);
                } catch (RemoteException ex) {
                    throw new RuntimeException(ex);
                }

                System.out.println("Downloader Retirado");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
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
        h.run();

    }
}
