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
            System.out.println(first-a);
            for(int i = 0; i<first-a; i++){
                 mapElemet = (Map.Entry)it.next();
            }
            aux.put((Integer) mapElemet.getKey(), (String) mapElemet.getValue());
        }
        return aux;
    }


    public static void colocaHashBd(boolean UrlOrToken, String messageFinal) throws SQLException {
        int conta2 = 0;
        String citacao = null, titulo = null, url = null, Url2 = null;;
        ;
        // divide a string em tokens usando o caractere "|"
        if (messageFinal.contains(" ;")) {
            String[] tokens = messageFinal.split(" ;");

            for (String token : tokens) {

                if (token != null) {

                    if (conta2 == 0 && UrlOrToken) {
                        String[] news = token.split(" \\| ");
                        titulo = news[0];
                        citacao = news[1];
                        url = news[2];
                        conta2++;
                    }


                    if (conta2 == 1 && UrlOrToken) {
                        String sql = "insert into url_info (url,titulo,citacao) values(?,?,?)";
                        PreparedStatement stament = connection.prepareStatement(sql);
                        stament.setString(1, url);
                        stament.setString(2, titulo);
                        stament.setString(3, citacao);
                        stament.executeUpdate();
                        conta2++;
                    } else {
                        String token1 = token.trim();
                        if (UrlOrToken) {
                            String sql = "select count(*)  from token_url where token_url.token1 = ? and token_url.url = ? and barrel=?;";
                            PreparedStatement stament = connection.prepareStatement(sql);
                            stament.setString(1, token1);
                            stament.setString(2, url);
                            stament.setInt(3, id);

                            ResultSet rs = stament.executeQuery();
                            if (rs.next()) {

                                int a = rs.getInt(1);
                                if (a == 0) {
                                    String sql2 = "insert into token_url (barrel,token1,url) values(?,?,?)";
                                    PreparedStatement stament2 = connection.prepareStatement(sql2);
                                    stament2.setInt(1, id);
                                    stament2.setString(2, token1);
                                    stament2.setString(3, url);
                                    stament2.executeUpdate();
                                    System.out.println("Inseriu " + Integer.toString(id) + " " + token1 + "\n");
                                }
                            }
                        } else {

                            if (conta2 > 0) {

                                Url2 = token;
                                String sql2 = "insert into url_url (barrel,url1,url2) values(?,?,?)";
                                PreparedStatement stament2 = connection.prepareStatement(sql2);
                                stament2.setInt(1, id);
                                stament2.setString(2, url);
                                stament2.setString(3, Url2);
                                stament2.executeUpdate();
                                conta2++;
                            } else {
                                url = token;
                                conta2++;
                            }
                        }
                    }
                }
            }
        }
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
            else {
                System.out.println("aqui");
                HashMap<Integer,String> auxi = server.PedidoHash(a, id);
                if (auxi == null) {
                    System.out.println("aqui1");
                    client.myClient(connection, id, hashmapas, 0);
                } else {
                    System.out.println("aqui2");
                    System.out.println("conta: "  + conta);
                    Iterator<Map.Entry<Integer, String>> it = auxi.entrySet().iterator();
                    int contaOutro = 0;
                    int i = 0;
                    while (it.hasNext()) {
                        Map.Entry mapElemet = (Map.Entry) it.next();
                        if(i>=conta*2) {
                            int first = (int) mapElemet.getKey();
                            contaOutro = (int) mapElemet.getKey();
                            hashmapas.put(contaOutro, (String) mapElemet.getValue());
                        }
                        i++;

                    }
                    if(hashmapas.size()!=0){
                        Iterator<Map.Entry<Integer, String>> ite = hashmapas.entrySet().iterator();
                        while (ite.hasNext()) {
                            Map.Entry mapElemet = (Map.Entry) ite.next();
                            int index = (int) mapElemet.getKey();
                            System.out.println(index + " " + mapElemet.getValue());
                            if(index%2==0){
                                colocaHashBd(true, (String) mapElemet.getValue());
                            }
                            else{
                                colocaHashBd(false, (String) mapElemet.getValue());
                            }
                        }
                    }
                    client.myClient(connection, id, hashmapas, contaOutro + 1);
                }
            }
        } catch (RemoteException re) {

            System.out.println("Exception in HelloImpl.main: " + re);
        }
    }

}