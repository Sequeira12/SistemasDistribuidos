package Message;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;

public class MessageServerInterfaceServer extends UnicastRemoteObject implements MessageServerInterface, IServerRemoteInterface, ISearcheQueue {

    public static IServerRemoteInterface Servidor;
    public static ISearcheQueue QueueSearche;
    public static ArrayList<InterfaceDownloaders> Downloads = new ArrayList<>();

    public static ArrayList<InterfaceDownloaders> Download2 = new ArrayList<>();
    public static IQueueRemoteInterface iq;
    public static ArrayList<IClientRemoteInterface> Barrels = new ArrayList<>();

    public static Connection connection;
    public static ArrayList<Integer> BarrelsID = new ArrayList<>();

    public static ArrayList<InterfaceClienteServer> Clientes = new ArrayList<>();


    public MessageServerInterfaceServer() throws RemoteException {
        super();
    }


    public void SendInfoDownloaders(ArrayList<Integer> Download) throws RemoteException {
        int contador = 0;

    }

    public String VerificaTop10() throws RemoteException, SQLException {
        StringBuilder top10;
        top10 = new StringBuilder("TOP 10 PESQUISAS!!!!\n");
        String sql = "select token1, sum(contador) as soma from (select distinct(token1),barrel,contador from token_url where contador > 0 group by token1,barrel,contador) as Counter group by token1 order by soma DESC limit 10;";
        PreparedStatement statement = connection.prepareStatement(sql);
        ResultSet rs = statement.executeQuery();
        int pos = 1;
        while (rs.next()) {
            top10.append(pos).append(" - ").append(rs.getString(1)).append(" --> ").append(rs.getInt(2)).append("\n");
            pos++;
        }

        return top10.toString();

    }


    public int registerClient(IClientRemoteInterface client, int id) throws RemoteException, SQLException {
        for (int i = 0; i < Barrels.size(); i++) {
            if (BarrelsID.get(i).compareTo(id) == 0) {
                return -1;
            }
        }
        String sql = "select count(distinct(url)) from token_url where barrel = ?;";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, id);
        ResultSet s = statement.executeQuery();
        int valor = 0;
        if(s.next()){
            valor = s.getInt(1);
        }
        if(valor==0){
            System.out.println("SUPOSTAMENTE");
            for (int i = 0; i < 5; i++) {
                sql = "INSERT INTO token_url (barrel, token1, url) select ?, token1,url from token_url where barrel = (select barrel as conta from token_url group by barrel order by count(token1) DESC limit 1) except select ?,token1,url from token_url where barrel = ?";
                //sql = "INSERT INTO token_url (barrel, token1, url) SELECT ?, token1, url FROM token_url WHERE barrel = (SELECT barrel FROM token_url GROUP BY barrel ORDER BY COUNT(token1) DESC LIMIT 1);";
                PreparedStatement statement2 = connection.prepareStatement(sql);
                statement2.setInt(1, id);
                statement2.setInt(2, id);
                statement2.setInt(3, id);
                int a = statement2.executeUpdate();
            }
            System.out.println("Informação adicionada ao Barrel " + id);
        }

        Barrels.add(client);
        BarrelsID.add(id);
        for (int j = 0; j < Clientes.size(); j++) {

            Clientes.get(j).atualizaStatus(Barrels, Download2);
        }
        System.out.printf("%d\n", Barrels.size());
        System.out.println("Barrel registrado no servidor.");
        return valor;
    }


    public void run() throws RemoteException {
        int i = 0, k = 0;
        while (true) {
            try {
                System.out.printf("Barrels Disponiveis %d\n", Barrels.size());
                System.out.printf("Clientes Disponiveis %d\n", Clientes.size());
                System.out.printf("Downloaders Disponiveis %d\n\n", Download2.size());
                if (iq.info() != null) {
                    Downloads = iq.info();
                }
                if (Downloads != Download2) {
                    Download2 = Downloads;

                    for (int j = 0; j < Clientes.size(); j++) {
                        Clientes.get(j).atualizaStatus(Barrels, Downloads);
                    }
                }


                for (i = 0; i < Barrels.size(); i++) {
                    Barrels.get(i).Connected();
                }


                for (k = 0; k < Clientes.size(); k++) {
                    Clientes.get(k).Connected();
                }


                Thread.sleep(1000);


            } catch (ServerException a) {
                System.out.println("Server Exception \n");
            } catch (ConnectException a) {
                // Se a conexão não existir retiro da lista
                if (i != Barrels.size()) {
                    unregisterBarrel(i);
                    for (int j = 0; j < Clientes.size(); j++) {
                        Clientes.get(j).atualizaStatus(Barrels, Download2);
                    }
                }
                if (k != Clientes.size()) {
                    unregisterClient(k);
                }


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void addClient(InterfaceClienteServer a) throws RemoteException {
        Clientes.add(a);
        System.out.println("Cliente ADICIONADO" + Clientes.size());
        a.atualizaStatus(Barrels, Download2);
    }

    public void unregisterBarrel(int posicao) throws RemoteException {
        Barrels.remove(posicao);
        BarrelsID.remove(posicao);
        /*for (int k = posicao; k < Barrels.size() - 1; k++) {
            Barrels.set(k, Barrels.get(k + 1));
            if (k == Barrels.size() - 1) {
                Barrels.set(k, null);

            }
        }*/
        System.out.println("Barrel removido do servidor.");
    }

    public HashMap<Integer, String> PedidoHash(int a,int id) throws RemoteException, SQLException {
        if(Barrels.size()==0) return null;
        String verifica = "select barrel as conta from token_url group by barrel order by count(token1) DESC";
        PreparedStatement stm = connection.prepareStatement(verifica);
        ResultSet resultado = stm.executeQuery();
        int barrel=0;
        if(resultado.next()){
            barrel=resultado.getInt(1);

            System.out.println("\n\n\nBARREL" + barrel + "\n\n");
            int p = BarrelsID.indexOf(barrel);
            if(p!=-1) {
                if (barrel != id && Barrels.get(p).Connected()) {
                    System.out.println("JA FEZ CRLH");
                    return Barrels.get(p).sendHash(a);
                }
            }
        }
        System.out.println("SEM BARRELLLLLL");
        return null;
    }

    public void unregisterClient(int posicao) throws RemoteException {
        Clientes.remove(posicao);

        for (int k = posicao; k < Clientes.size() - 1; k++) {
            Clientes.set(k, Clientes.get(k + 1));
            if (k == Clientes.size() - 1) {
                Clientes.set(k, null);

            }
        }
        System.out.println("Cliente removido do servidor.");
    }


    public ArrayList<String> FindUrlWithToken(String token) throws RemoteException, SQLException {

        try {
            ArrayList<String> connectados = new ArrayList<>();
            System.out.println(Barrels.size());
            if (Barrels.size() != 0) {


                Random gerador = new Random();
                int numero = gerador.nextInt((Barrels.size()));
                System.out.printf("Barrel que vai executar a Procura ---> %d\n", numero);
                if (Barrels.get(numero) != null) {
                    connectados = Barrels.get(numero).ProcuraToken(token);
                }
            } else {
                String No = "Sem Resultados";
                connectados.add(No);
            }
            return connectados;
        } catch (RemoteException a) {
            return null;
        }
    }

    public void SendUrltoQueue(String url) throws RemoteException {
        iq.coloca(url);
    }

    public String sayHello(int login) throws RemoteException {
        if (login == 0) {
            return "Bem-vindo\nEscolha as opções:\n1-Token para procurar\n2-URL para indexar\n3-Estatisticas\n0-exit\nObrigado!!\n";
        } else {
            return "Bem-vindo\nEscolha as opções:\n1-Token para procurar\n2-URL para indexar\n3-Estatisticas\n4-lista de páginas com ligação para uma página\n0-exit\nObrigado!!\n";
        }

    }

    public ArrayList<String> listPagesConnectedtoAnotherPage(String url) throws SQLException, RemoteException {
        ArrayList<String> connectados = new ArrayList<>();
        System.out.println(Barrels.size());
        if (Barrels.size() != 0) {
            Random gerador = new Random();
            int numero = gerador.nextInt((Barrels.size()));
            System.out.printf("Barrel que vai executar a LISTAGEM DE URL ---> %d\n", numero);
            if (Barrels.get(numero) != null) {


                connectados = Barrels.get(numero).listPage(url);

                //connectados = Barrels.get(numero).listPage(url);
            }
        } else {
            String No = "Sem Resultados";
            connectados.add(No);
        }
        return connectados;
    }

    public boolean Register(String username, String password) throws SQLException {
        String sql = "select count(*) from info_client where username = ? and pass =  ? ";
        PreparedStatement stament = connection.prepareStatement(sql);
        stament.setString(1, username);
        stament.setString(2, password);
        stament.execute();
        ResultSet rs = stament.executeQuery();
        if (rs.next()) {
            int a = rs.getInt(1);
            if (a == 0) {
                sql = "insert into info_client (username,pass) values(?,?);";
                stament = connection.prepareStatement(sql);
                stament.setString(1, username);
                stament.setString(2, password);
                stament.executeUpdate();
                return true;

            }
        }
        return false;

    }

    public boolean Login(String username, String password) throws SQLException {
        String sql = "select count(*) from info_client where username = ? and pass =  ? ;";
        PreparedStatement stament = connection.prepareStatement(sql);
        stament.setString(1, username);
        stament.setString(2, password);

        ResultSet rs = stament.executeQuery();
        if (rs.next()) {
            int a = rs.getInt(1);
            if (a == 1) {
                return true;

            }
        }
        return false;

    }


    public String SendUrlQueue(String token) throws RemoteException {

        return null;
        //return mensagem;
    }

    // =========================================================
    public static void main(String args[]) {

        try {
            /**
             * Fazer por multicast a ligacao do Barrels com o Search Module
             * ter uma array com os (ips portos) dos barrels que estão conectados
             *
             * Fazer funcao aqui para que o barrels invoce para adicionar a porta dele a um arraylist de porta globais do servidor
             *
             */
            String url = "jdbc:postgresql://localhost/sddb";
            String username = "adminsd";
            String password = "admin";


            DriverManager.registerDriver(new org.postgresql.Driver());
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Connected to database");

            iq = (IQueueRemoteInterface) LocateRegistry.getRegistry(7003).lookup("QD");

            MessageServerInterfaceServer h = new MessageServerInterfaceServer();
            Registry r = LocateRegistry.createRegistry(7001);
            r.rebind("SD", h);


            QueueSearche = (ISearcheQueue) LocateRegistry.getRegistry(7005).lookup("QS"); // LIGACAO RMI


            Servidor = new MessageServerInterfaceServer();
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            java.rmi.Naming.rebind("ServerObject", Servidor);
            System.out.println("Servidor pronto para receber chamadas remotas.");
            h.run();


        } catch (RemoteException re) {
            System.out.println("Exception in HelloImpl.main: " + re);
        } catch (MalformedURLException | NotBoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}


