package Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Barrels extends UnicastRemoteObject implements IClientRemoteInterface {
    /**
     *
     */
    public static ArrayList<String> StopWords = new ArrayList<String>();
    private static final long serialVersionUID = 1L;
    private IServerRemoteInterface server;

    public static ArrayList<MulticastClient> mults = new ArrayList<MulticastClient>();
    public static HashMap<String, ArrayList<Element>> urls_ligacoes = new HashMap<String, ArrayList<Element>>();

    public static Connection connection = null;
    public static int id;

    public static HashMap<String, ArrayList<String>> tokens_url = new HashMap<String, ArrayList<String>>();

    public Barrels() throws RemoteException {
        super();
    }

    public int connectToServer(IClientRemoteInterface client, int id) {
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

    public Connection conetor() {
        System.out.println("ccccc");
        return connection;
    }

    public boolean Connected() {
        return true;
    }

    public ArrayList<String> ProcuraToken(String token) throws SQLException {


        ArrayList<String> connectados = new ArrayList<>();
        String[] news = token.split(" ");

        String sql = "select distinct(url_url.url2),(select count(distinct(url1)) from url_url where url2 = token_url.url and url1 != token_url.url ) as url_count , url_info.titulo,url_info.citacao from token_url join url_url on token_url.url = url_url.url2 join  url_info on token_url.url = url_info.url where token_url.token1 IN (" + String.join(",", Arrays.stream(news).map(t -> "?").toArray(String[]::new)) + ") " + "and url1 != url2 and token_url.barrel = ? order  by url_count desc";

        //String sql = "select distinct(url_url.url2), (select count(distinct(url1))  from url_url where url2 = token_url.url and url1 != token_url.url ) as url_count from token_url join url_url on token_url.url = url_url.url2 where token_url.token1 IN (" + String.join(",", Arrays.stream(news).map(t -> "?").toArray(String[]::new)) + ") " + " and url1 != url2 order  by url_count desc";


        PreparedStatement stament = connection.prepareStatement(sql);

        for (int i = 0; i < news.length; i++) {
            stament.setString(i + 1, news[i]);
        }

        ResultSet rs = stament.executeQuery();
        System.out.printf("Procura da palavra: %s\n", token);
        int conta = 0;

        while (rs.next()) {
            String composta = "URL: " + rs.getString(1) + "\n\tTITULO: " + rs.getString(3) + "\n\tCITAÇÃO: " + rs.getString(4) + "\n";
            connectados.add(composta);
            conta++;
        }
        if (conta == 0) {
            connectados = null;
        }
        rs.close();
        stament.close();
        return connectados;
    }


    // =========================================================
    public static void main(String args[]) throws SQLException {
        String url = "jdbc:postgresql://localhost/sddb";
        String username = "adminsd";
        String password = "admin";

        id = Integer.parseInt(args[0]);


        DriverManager.registerDriver(new org.postgresql.Driver());
        connection = DriverManager.getConnection(url, username, password);
        System.out.println("Connected to database");
        try {

            Barrels clientObj = new Barrels();
            int a = clientObj.connectToServer(clientObj, id);
            if (a == -1) {
                return;
            }
            MulticastClient client = new MulticastClient();
            client.myClient(connection, id);
            client.run();


        } catch (RemoteException re) {

            System.out.println("Exception in HelloImpl.main: " + re);
        }
    }

}