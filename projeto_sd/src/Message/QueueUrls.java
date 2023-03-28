package Message;

import java.sql.*;
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
    public static ArrayList<Integer> DownloadersOnPORTA = new ArrayList<Integer>();

    int numeroBarrels=0;

    public static ArrayList<InterfaceDownloaders> Downloaders = new ArrayList<>();

    public static Connection connection = null;

    public QueueUrls() throws RemoteException {
        super();
    }


    public void atualizaNumeroBarrels(int n){
        numeroBarrels=n;
    }

    public int giveNumeroBarrels(){
        return numeroBarrels;
    }

    public void unregisterDownloader(int posicao) throws RemoteException {
        int porta = DownloadersOnPORTA.get(posicao);
        Downloaders.remove(posicao);

        DownloadersOnPORTA.remove(posicao);


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

    public void run() throws RemoteException, SQLException {
        int i = 0;

        while (true) {
            try {
                System.out.println("Nº de Downloaders" + Downloaders.size());
                for (i = 0; i < Downloaders.size(); i++) {
                    Downloaders.get(i).Connected();
                }

                Thread.sleep(1000);
            } catch (RemoteException e) {

                int porta = DownloadersOnPORTA.get(i);

                System.out.println("O URL QUE SAIU FOI O " + porta);

                String sql = "select url from Queue_url where barrel = ? and executed = false;";

                PreparedStatement stmt = connection.prepareStatement(sql);
                stmt.setInt(1, porta);
                ResultSet rs = stmt.executeQuery();
                String ul = null;
                if (rs.next()) {
                    ul = rs.getString(1);

                }
                if (ul != null) {

                    sql = "update Queue_url set barrel = null, executed = null where barrel = ?";
                    stmt = connection.prepareStatement(sql);
                    stmt.setInt(1, porta);
                    stmt.executeUpdate();
                    System.out.println("Colocou o url -> " + ul + " na Fila de URLS(NAO O EXECUTOU POR COMPLETO)");
                    coloca(ul, 0);
                }


                unregisterDownloader(i);
                System.out.println("Downloader Retirado");
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
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

    public synchronized void coloca(String e, int i) throws SQLException {
        Urls_To_Downloaders.add(e);
        if (i == 1) {
            String sql = "select count(*) from Queue_url where url = ?;";
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, e);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                if (rs.getInt(1) == 0) {
                    sql = "insert into Queue_url (url) values(?);";
                    stmt = connection.prepareStatement(sql);
                    stmt.setString(1, e);
                    stmt.executeUpdate();
                }
            }
        }


        System.out.println("URL adicionado");
    }

    public static void main(String[] args) throws RemoteException, SQLException {

        //Urls_To_Downloaders.add("https://www.uc.pt");
        String url = "jdbc:postgresql://localhost/sddb";
        String username = "adminsd";
        String password = "admin";

        DriverManager.registerDriver(new org.postgresql.Driver());
        connection = DriverManager.getConnection(url, username, password);

        String sql = "select url from Queue_url where barrel is null and executed is null;";
        PreparedStatement stmt = connection.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        while (rs.next()) {
            System.out.println("HEYYYY");
            String ul = rs.getString("url");
            // Processar o URL aqui, por exemplo:
            Urls_To_Downloaders.add(ul);
        }

        QueueUrls h = new QueueUrls();
        Registry r = LocateRegistry.createRegistry(7003);
        r.rebind("QD", h);


        Ligacao = new MessageServerInterfaceServer();
        Registry r1 = LocateRegistry.createRegistry(7005);
        r1.rebind("QS", Ligacao);
        h.run();

    }
}
