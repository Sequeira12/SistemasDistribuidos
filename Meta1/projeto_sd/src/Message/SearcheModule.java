package Message;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class SearcheModule extends UnicastRemoteObject implements MessageServerInterface, IServerRemoteInterface, ISearcheQueue {

    public static IServerRemoteInterface Servidor;

    public static ArrayList<InterfaceDownloaders> Downloads = new ArrayList<>();

    public static ArrayList<InterfaceDownloaders> Download2 = new ArrayList<>();
    public static IQueueRemoteInterface iq;
    public static ArrayList<IBarrelRemoteInterface> Barrels = new ArrayList<>();
    public static Connection connection;
    public static ArrayList<Integer> BarrelsID = new ArrayList<>();

    public static ArrayList<InterfaceClienteServer> Clientes = new ArrayList<>();


    public SearcheModule() throws RemoteException {
        super();
    }

    /**
     * function that gets the top 10 tokens researched
     * @return string with the top 10 tokens researched
     * @throws RemoteException
     * @throws SQLException
     */
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

    /**
     * Function that register the barrel that is connecting and fills his database with the information that other barrels
     * have if the barrel is new or if he is the only one connected
     * @param client barrel that is going to connect
     * @param id barrel id
     * @return -1 if the connection could not be made
     * @throws RemoteException
     * @throws SQLException
     */
    public int registerClient(IBarrelRemoteInterface client, int id) throws RemoteException, SQLException {
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
        if (s.next()) {
            valor = s.getInt(1);
        }


        if (valor == 0 || Barrels.size() == 0) {


            sql = "INSERT INTO token_url (barrel, token1, url) select ?, token1,url from token_url where barrel = (select barrel as conta from token_url group by barrel order by count(token1) DESC limit 1) except select ?,token1,url from token_url where barrel = ?";
            PreparedStatement statement2 = connection.prepareStatement(sql);
            statement2.setInt(1, id);
            statement2.setInt(2, id);
            statement2.setInt(3, id);
            statement2.executeUpdate();

            System.out.println("Informação adicionada ao Barrel (TOTAL/DIFERENCA) " + id);
        }

        Barrels.add(client);
        iq.atualizaNumeroBarrels(Barrels.size());
        BarrelsID.add(id);
        for (InterfaceClienteServer cliente : Clientes) {
            cliente.atualizaStatus(Barrels, Download2);
        }

        System.out.println("Barrel registrado no servidor.");
        return valor;
    }

    /**
     * verifies how many clients, barrels and downloaders are online
     * @throws RemoteException
     */
    public void verificaDisponiveis() throws RemoteException {
        int i = 0, k = 0;
        while (true) try {
            System.out.printf("Barrels Disponiveis %d\n", Barrels.size());
            System.out.printf("Clientes Disponiveis %d\n", Clientes.size());
            System.out.printf("Downloaders Disponiveis %d\n\n", Download2.size());
            if (iq.info() != null) {
                Downloads = iq.info();
            }
            if (Downloads != Download2) {
                Download2 = Downloads;

                for (InterfaceClienteServer cliente : Clientes) {
                    cliente.atualizaStatus(Barrels, Downloads);
                }
            }


            for (i = 0; i < Barrels.size(); i++) {
                Barrels.get(i).Connected();
            }


            for (k = 0; k < Clientes.size(); k++) {
                Clientes.get(k).Connected();
            }


            TimeUnit.SECONDS.sleep(2);


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

    /**
     * Function that adds a new client to the arraylist of clients and gives to that client the number of downloaders
     * and barrels for his status
     * @param a new client
     * @throws RemoteException
     */
    public void addClient(InterfaceClienteServer a) throws RemoteException {
        Clientes.add(a);
        System.out.println("Cliente ADICIONADO" + Clientes.size());
        a.atualizaStatus(Barrels, Download2);
    }

    /**
     * function that removes the barrel that disconnected from the arraylist of clients
     * @param posicao position in the arraylist of barrels
     * @throws RemoteException
     */
    public void unregisterBarrel(int posicao) throws RemoteException {
        Barrels.remove(posicao);
        BarrelsID.remove(posicao);
        iq.atualizaNumeroBarrels(Barrels.size());

        System.out.println("Barrel removido do servidor.");
    }


    /**
     * requests a hashmap to a barrel available with the information that another barrel needs
     * @param a start
     * @param id barrel id
     * @return
     * @throws RemoteException
     * @throws SQLException
     */
    public HashMap<Integer, String> PedidoHash(int a, int id) throws RemoteException, SQLException {
        if (Barrels.size() == 0) return null;
        String verifica = "select barrel as conta,count(distinct(url)) from token_url group by barrel order by count(token1),count(distinct(url)) DESC";
        PreparedStatement stm = connection.prepareStatement(verifica);
        ResultSet resultado = stm.executeQuery();
        int barrel = 0;
        int valorMaximo = 0;
        int contador = 0;
        while (resultado.next()) {
            if (contador == 0) valorMaximo = resultado.getInt(2);
            contador++;
            barrel = resultado.getInt(1);


            int p = BarrelsID.indexOf(barrel);
            if (p != -1) {
                if (barrel != id && Barrels.get(p).Connected()) {

                    HashMap<Integer, String> aux = Barrels.get(p).sendHash(a);
                    Map.Entry<Integer, String> firstEntry = aux.entrySet().iterator().next();


                    if (firstEntry.getKey() == a) {
                        System.out.println("\n\n\nBARREL que vai enviar a informação " + barrel + "\n\n");
                        return aux;
                    }

                }
            }
        }
        String sql = "INSERT INTO token_url (barrel, token1, url) select ?, token1,url from token_url where barrel = (select barrel as conta from token_url group by barrel order by count(token1) DESC limit 1) except select ?,token1,url from token_url where barrel = ?";
        PreparedStatement statement2 = connection.prepareStatement(sql);
        statement2.setInt(1, id);
        statement2.setInt(2, id);
        statement2.setInt(3, id);
        statement2.executeUpdate();


        System.out.println("(Barrels disponiveis sem a informação total -> informação colocada)\n");
        return null;
    }


    /**
     * function that removes the client that disconnected from the arraylist of clients
     * @param posicao position in the arraylist to remove
     * @throws RemoteException
     */
    public static void unregisterClient(int posicao) throws RemoteException {
        Clientes.remove(posicao);
        System.out.println("Cliente removido do servidor.");
    }


    /**
     * Funtion that returns an arraylist of urls where the token can be find
     * @param token token to find
     * @param logado if the user is logged in
     * @return arraylist of urls where the token can be find
     * @throws RemoteException
     * @throws SQLException
     */
    public ArrayList<String> FindUrlWithToken(String token, int logado) throws RemoteException, SQLException {

        try {
            ArrayList<String> connectados = new ArrayList<>();
            System.out.println(Barrels.size());
            if (Barrels.size() != 0) {


                Random gerador = new Random();
                int numero = gerador.nextInt((Barrels.size()));
                System.out.printf("Barrel que vai executar a Procura ---> %d\n", numero);


                if (Barrels.get(numero) != null) {

                    connectados = Barrels.get(numero).ProcuraToken(token, logado);

                }
            } else {
                String No = "Sem Resultados";
                connectados.add(No);
            }
            return connectados;
        } catch (RemoteException a) {

            return FindUrlWithToken(token, logado);
        }
    }


    /**
     * says hello to the new user
     * @param login if user is loogged in
     * @return string with the hello message
     * @throws RemoteException
     */
    public String sayHello(int login) throws RemoteException {
        if (login == 0) {
            return "Bem-vindo\nEscolha as opções:\n1-Token para procurar\n2-URL para indexar\n3-Estatisticas\n0-exit\nObrigado!!\n";
        } else {
            return "Bem-vindo\nEscolha as opções:\n1-Token para procurar\n2-URL para indexar\n3-Estatisticas\n4-lista de páginas com ligação para uma página\n0-exit\nObrigado!!\n";
        }

    }

    /**
     * function that list the urls that connect to an url given by the user
     * @param url url to process
     * @return a string with the list of urls connected to another page
     * @throws SQLException
     * @throws RemoteException
     */
    public String listPagesConnectedtoAnotherPage(String url) throws SQLException, RemoteException {
        String connectados = null;
        System.out.println(Barrels.size());
        if (Barrels.size() != 0) {
            Random gerador = new Random();
            int numero = gerador.nextInt((Barrels.size()));
            System.out.printf("Barrel que vai executar a LISTAGEM DE URL ---> %d\n", numero);
            if (Barrels.get(numero) != null) {


                connectados = Barrels.get(numero).listPage(url);
            }
        } else {
            connectados = "Sem Resultados";

        }
        return connectados;
    }

    /**
     * registers a new client
     * @param username of the client
     * @param password of the client
     * @return true if sucessfull
     * @throws SQLException
     */
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

    /**
     * login a new user
     * @param username of the client
     * @param password of the client
     * @return true if sucessfull
     * @throws SQLException
     */
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

    /**
     * funcion that puts a new url given by the user in the queue to be processed
     * @param url url to send to the queue
     * @throws RemoteException
     * @throws SQLException
     */
    public void SendUrltoQueue(String url) throws RemoteException, SQLException {
        iq.coloca(url, 1);
    }

    /**
     * connects the search module to the database and connects the search module to the queue
     * @param args
     */
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

            SearcheModule h = new SearcheModule();
            Registry r = LocateRegistry.createRegistry(7001);
            r.rebind("SD", h);





            Servidor = new SearcheModule();
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            java.rmi.Naming.rebind("ServerObject", Servidor);
            System.out.println("Servidor pronto para receber chamadas remotas.");
            h.verificaDisponiveis();


        } catch (RemoteException re) {
            System.out.println("Exception in HelloImpl.main: " + re);
        } catch (MalformedURLException | NotBoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


}


