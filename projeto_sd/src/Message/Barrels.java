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
    public static HashMap<String, ArrayList<Element>> urls_ligacoes = new HashMap<String, ArrayList<Element>>();

    public static Connection connection = null;

    public static HashMap<String, ArrayList<String>> tokens_url = new HashMap<String, ArrayList<String>>();

    public Barrels() throws RemoteException {
        super();
    }

    public void connectToServer(IClientRemoteInterface client) {
        try {
            server = (IServerRemoteInterface) Naming.lookup("ServerObject");

            server.registerClient(client);
            System.out.println("Conexão com o servidor estabelecida.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean Connected() {
        return true;
    }

    public ArrayList<String> ProcuraToken(String token) throws SQLException {


        ArrayList<String> connectados = new ArrayList<>();
        String[] news = token.split(" ");

        String sql = "select distinct(url_url.url2),(select count(distinct(url1)) from url_url where url2 = token_url.url and url1 != token_url.url ) as url_count , url_info.titulo,url_info.citacao from token_url join url_url on token_url.url = url_url.url2 join  url_info on token_url.url = url_info.url where token_url.token1 IN (" + String.join(",", Arrays.stream(news).map(t -> "?").toArray(String[]::new)) + ") " + "and url1 != url2 order  by url_count desc";

        //String sql = "select distinct(url_url.url2), (select count(distinct(url1))  from url_url where url2 = token_url.url and url1 != token_url.url ) as url_count from token_url join url_url on token_url.url = url_url.url2 where token_url.token1 IN (" + String.join(",", Arrays.stream(news).map(t -> "?").toArray(String[]::new)) + ") " + " and url1 != url2 order  by url_count desc";


        PreparedStatement stament = connection.prepareStatement(sql);

        for (int i = 0; i < news.length; i++) {
            stament.setString(i+1, news[i]);
        }

        ResultSet rs = stament.executeQuery();
        System.out.printf("Procura da palavra: %s\n", token);
        int conta = 0;

        while(rs.next()){
            String composta = "URL: " + rs.getString(1) + "\n\tTITULO: " + rs.getString(3) + "\n\tCITAÇÃO: " + rs.getString(4) + "\n";
            connectados.add(composta);
            conta++;
        }
        if(conta==0){
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

        DriverManager.registerDriver(new org.postgresql.Driver());
        connection = DriverManager.getConnection(url, username, password);
        System.out.println("Connected to database");
        try {
            String El1 = "www.uc.pt";
            String El2 = "www.dei.uc.pt";
            ArrayList<String> arraypalavra = new ArrayList<String>();
            arraypalavra.add(El1);
            arraypalavra.add(El2);

            tokens_url.put("Coimbra", arraypalavra);

            Barrels clientObj = new Barrels();
            clientObj.connectToServer(clientObj);

            MulticastClient cliente = new MulticastClient();
            cliente.myClient(connection,1);
            cliente.run();


        } catch (RemoteException re) {

            System.out.println("Exception in HelloImpl.main: " + re);
        }
    }

}