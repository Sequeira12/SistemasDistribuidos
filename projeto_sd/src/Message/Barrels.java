package Message;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.*;

public class Barrels extends UnicastRemoteObject implements IBarrelRemoteInterface {

    public static int conta, a;

    public static boolean livre;
    public static IServerRemoteInterface server;

    public static Connection connection = null;
    public static int id;
    public static MulticastClient client = new MulticastClient();
    public static HashMap<Integer, String> hashmapas = new HashMap<>();

    public Barrels() throws RemoteException {
        super();
    }

    /**
     * Function responsible to connect the barrel to the search module
     * @param client barrel a conectar
     * @param id id do barrel
     * @return
     */
    public int connectToServer(IBarrelRemoteInterface client, int id) {
        try {
            int valor;
            server = (IServerRemoteInterface) Naming.lookup("ServerObject");

            valor = server.registerClient(client, id);

            System.out.println("Conexão com o servidor estabelecida.");
            return valor;
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Function that verifies if the barrel is still online
     * @return true if the barrel is connected
     */
    public boolean Connected() {
        return true;
    }

    /**
     * Function that finds the urls (1) where we can find a certain token
     * If the user is looged in, he can also see the links that point to the first urls (1)
     * @param token token to find
     * @param logado 1 if is a logged in client 0 if not
     * @return arraylist of links + links that point to the first one where the token is located
     * @throws SQLException
     * @throws RemoteException
     */
    public ArrayList<String> ProcuraToken(String token,int logado) throws SQLException, RemoteException {
        connection.setAutoCommit(true);

        ArrayList<String> connectados = new ArrayList<>();
        String[] news = token.split(" ");

        String sql = "select distinct(url_url.url2),(select count(distinct(url1)) from url_url where url2 = token_url.url and url1 != token_url.url ) as url_count , url_info.titulo,url_info.citacao from token_url join url_url on token_url.url = url_url.url2 join  url_info on token_url.url = url_info.url where token_url.token1 IN (" + String.join(",", Arrays.stream(news).map(t -> "?").toArray(String[]::new)) + ") " + "and url1 != url2 and token_url.barrel = ?  order  by url_count desc";

        String updateContador = "update token_url SET contador = contador + 1 where token1 = ? and barrel = ?;";
        PreparedStatement stament = connection.prepareStatement(sql);
        PreparedStatement statementUpdate = connection.prepareStatement(updateContador);
        int i;
        System.out.println(news.length);
        for (i = 0; i < news.length; i++) {
            stament.setString(i + 1, news[i]);
            statementUpdate.setString(1, news[i]);
            statementUpdate.setInt(2, id);
            int rs2 = statementUpdate.executeUpdate();

        }
        stament.setInt(i + 1, id);

        ResultSet rs = stament.executeQuery();


        System.out.printf("Procura da palavra: %s\n", token);
        int conta = 0;

        while (rs.next()) {

            connectados.add("URL: " + rs.getString(1) + "\n\tTITULO: " + rs.getString(3) + "\n\tCITAÇÃO: " + rs.getString(4) + "\n");
            if(logado==1){
                String connected = listPage(rs.getString(1));
                connectados.add(connected);
            }


            conta++;
        }
        if (conta == 0) {
            connectados = null;
        }
        rs.close();
        stament.close();
        connection.setAutoCommit(false);
        return connectados;

    }

    /**
     * Function that, given an url, returns a string of urls that have a connection to the link given
     * @param url url to search
     * @return a string that includes every url that point to the url given by the client
     * @throws RemoteException
     * @throws SQLException
     */
    public String listPage(String url) throws RemoteException, SQLException {


        StringBuilder connectados = new StringBuilder();
        String sql = "select distinct(url1) from url_url where url2 = ? and barrel = ? and url2 != url1;";
        PreparedStatement stament = connection.prepareStatement(sql);
        stament.setString(1, url);
        stament.setInt(2, id);
        ResultSet rs = stament.executeQuery();
        int conta = 0;

        while (rs.next()) {
            String composta = "Ligação: " + rs.getString(1) + "\n";
            connectados.append(composta);
            conta++;
        }
        if (conta == 0) {
            connectados = new StringBuilder("Sem resultados");
        }
        rs.close();
        stament.close();
        return connectados.toString();


    }

    /**
     * Function that returns a hashmap with the information that another barrel needs
     * @param a numember of urls already procecced of the barrel that is requesting the information
     * @return hashmap
     */
    public HashMap<Integer, String> sendHash(int a) {
        System.out.println("RECEBI O A COM VALOR DE " + a);
        HashMap<Integer, String> aux = new HashMap<>();

        int last = 0;
        for (Map.Entry<Integer, String> entry : client.sendHashtoBarrels().entrySet()) {
            Integer novo = entry.getKey();
            System.out.println( "_____" + novo + "  " + a);
            if (novo >= a) {
                System.out.println(novo + "  " + a);
                System.out.println(novo);
                aux.put(novo, entry.getValue());
            }
            last = novo;
        }
        System.out.println("TA FEITO COM " + last);


        return aux;
    }


    /**
     * Function that connects the barrel to the database. If it is a new barrel, it will fill his database with the
     * information that the others already have. If it is not new, it will request a hashmap with the information that
     * he dont have.
     * @param args
     * @throws SQLException
     */
    // =========================================================
    public static void main(String args[]) throws SQLException {
        String url = "jdbc:postgresql://localhost/sddb";
        String username = "adminsd";
        String password = "admin";

        id = Integer.parseInt(args[0]);


        DriverManager.registerDriver(new org.postgresql.Driver());
        connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(false);
        System.out.println("Connected to database");
        try {
            livre = true;

            Barrels clientObj = new Barrels();
            a = clientObj.connectToServer(clientObj, id);
            if (a == -1) {
                System.out.println("Id ocupado");
                System.exit(0);
            }

            String verifica = "Select count(distinct(url)) from token_url where barrel = ?";
            PreparedStatement stm = connection.prepareStatement(verifica);
            stm.setInt(1, id);
            ResultSet resultado = stm.executeQuery();

            if (resultado.next()) {
                conta = resultado.getInt(1);
            }
            System.out.println("O VALOR DE A --> " + a);
            a *= 2;
            System.out.println(a);

            if (a != 0) {
                ColocaInfoBarrel m = new ColocaInfoBarrel();
                m.Info(server, connection, hashmapas, id,a);
                client.myClient(connection, id, hashmapas, a);
                m.start();
                client.start();

            } else {
                client.myClient(connection, id, hashmapas, a);
                client.start();

            }

        } catch (
                RemoteException re) {

            System.out.println("Exception in HelloImpl.main: " + re);
        }
    }

}