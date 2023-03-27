package Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ColocaInfoBarrel extends Thread {


    public static int conta, a;
    public static IServerRemoteInterface server;

    public static Connection connection = null;
    public static int id;
    public static MulticastClient client = new MulticastClient();

    public static HashMap<Integer, String> hashmapas = new HashMap<>();
    public void Info(IServerRemoteInterface server1,Connection conn, HashMap<Integer, String> info1, int i,MulticastClient client,int num) throws SQLException {
        connection = conn;
        connection.setAutoCommit(false);
        id = i;
        hashmapas = info1;
        server = server1;
        a = num;


    }


    public static void colocaHashBd(boolean UrlOrToken, String messageFinal) throws SQLException {
        System.out.println("\n\n\n\n\n\n\n\nCOLOOOOCOOOOOOOOO\n\n\n");
        int conta2 = 0;
        String citacao = null, titulo = null, url = null, Url2 = null;


        // divide a string em tokens usando o caractere "|"
        if (messageFinal.contains(" ;")) {
            String[] tokens = messageFinal.split(" ;");
            String[] ne2 = tokens[0].split(" \\| ");
            if (ne2.length == 3) {
                UrlOrToken = true;
            } else {
                UrlOrToken = false;
            }
            for (String token : tokens) {

                if (token != null) {

                    if (conta2 == 0 && UrlOrToken) {
                        String[] news = token.split(" \\| ");
                        System.out.println("\n\n\n" + news[0] + news.length + "\n\n\n");
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
                            conta2++;
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
                        conta2++;
                    }
                }
            }
        }
        connection.commit();
    }

    public void run() {
        System.out.println("aqui");
        HashMap<Integer, String> auxi = null;
        try{

            auxi = server.PedidoHash(a, id);


        if (auxi == null) {
            System.out.println("aqui1");
            client.myClient(connection, id, hashmapas, 0);
        } else {
            System.out.println("aqui2");
            System.out.println("conta: " + conta);
            System.out.println("tamanho que deu pa colocar " + auxi.size());
            int lastKey = 0;
            for (Map.Entry<Integer, String> entry : auxi.entrySet()) {
                colocaHashBd(true, entry.getValue());
                lastKey = entry.getKey();
            }
            int contador = lastKey++;

            System.out.println("SENDO A MERD DO CONTADOR " + contador);
            conta = contador++;



        }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }
}
