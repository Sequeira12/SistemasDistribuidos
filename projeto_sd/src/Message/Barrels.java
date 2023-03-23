package Message;

import org.jsoup.nodes.Element;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.*;

public class Barrels extends UnicastRemoteObject implements IClientRemoteInterface {
    /**
     *
     */
    public static ArrayList<String> StopWords = new ArrayList<String>();
    private static final long serialVersionUID = 1L;
    public static IServerRemoteInterface server;

    public static ArrayList<MulticastClient> mults = new ArrayList<MulticastClient>();
    public static HashMap<String, ArrayList<Element>> urls_ligacoes = new HashMap<String, ArrayList<Element>>();

    public static Connection connection = null;
    public static int id;
    static MulticastClient client = new MulticastClient();
    public static HashMap<Integer, String> hashmapas = new HashMap<>();

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

        return connection;
    }

    public boolean Connected() {
        return true;
    }

    public ArrayList<String> ProcuraToken(String token) throws SQLException {


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
            statementUpdate.setString(1,news[i]);
            statementUpdate.setInt(2,id);
            int rs2 = statementUpdate.executeUpdate();

        }
        stament.setInt(i + 1, id);

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


    public ArrayList<String> listPage(String url) throws RemoteException, SQLException {


        ArrayList<String> connectados = new ArrayList<>();
        String sql = "select distinct(url1) from url_url where url2 = ? and barrel = ?;";
        PreparedStatement stament = connection.prepareStatement(sql);
        stament.setString(1, url);
        stament.setInt(2, id);
        ResultSet rs = stament.executeQuery();
        int conta = 0;

        while (rs.next()) {
            String composta = "Ligação: " + rs.getString(1) + "\n";
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

    public HashMap<Integer,String> sendHash(int a){
        HashMap<Integer,String> aux = new HashMap<>();
        HashMap<Integer,String> aux1=client.sendHashtoBarrels();
        Iterator<Map.Entry<Integer, String>> it = aux1.entrySet().iterator();
        while(it.hasNext()){
            Map.Entry mapElemet = (Map.Entry)it.next();
            int first= (int) mapElemet.getKey();
            for(int i = 0; i<first-a; i++){
                 mapElemet = (Map.Entry)it.next();
            }
            aux.put((Integer) mapElemet.getKey(), (String) mapElemet.getValue());
        }
        aux.put(0,"FDS");
        return aux;
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

            String verifica = "Select count(distinct(url)) from token_url where barrel = ?";
            PreparedStatement stm = connection.prepareStatement(verifica);
            stm.setInt(1,id);
            ResultSet resultado = stm.executeQuery();
            int conta=0;
            if(resultado.next()){
                conta=resultado.getInt(1);
            }
            if(a==0){
                client.myClient(connection, id, hashmapas, conta);
            }
            else{
                Iterator it = server.PedidoHash(a,id).entrySet().iterator();
                int contaOutro=0;
                while(it.hasNext()){
                    Map.Entry mapElemet = (Map.Entry)it.next();
                    int first= (int) mapElemet.getKey();
                    for(int i = 0; i<first-a; i++){
                        mapElemet = (Map.Entry)it.next();
                    }
                    contaOutro= (int) mapElemet.getKey();
                    hashmapas.put(contaOutro, (String) mapElemet.getValue());

                }
                client.myClient(connection, id, hashmapas, contaOutro+1);
            }
        } catch (RemoteException re) {

            System.out.println("Exception in HelloImpl.main: " + re);
        }
    }

}