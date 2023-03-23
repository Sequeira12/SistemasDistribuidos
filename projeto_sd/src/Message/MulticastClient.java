package Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.sql.*;
import java.util.Arrays;
import java.util.HashMap;

public class MulticastClient extends Thread {
    private String MULTICAST_ADDRESS = "224.3.2.1";
    private int PORT = 4321;
    Connection connection = null;
    int id;


    HashMap<Integer,String> info = null;
    int contaHash;

    public synchronized void myClient(Connection conn, int i,HashMap<Integer,String> info1, int conta) {
        connection = conn;
        id = i;
        info=info1;
        contaHash=conta;
        System.out.println("Tamanho da hash recebida: " + info1.size());
        run();
    }

    public HashMap<Integer,String> sendHashtoBarrels(){
        return info;
    }

    public void run() {
        MulticastSocket socket = null;
        try {
            System.out.printf("VAI LER NA PORTA %d com id %d\n", PORT, id);
            socket = new MulticastSocket(PORT);  // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);



            socket.joinGroup(group);
            while (true) {
                boolean UrlOrToken;
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String messageTamanho = new String(packet.getData(), 0, packet.getLength());

                String[] urltoken = messageTamanho.split(" \\| ");
                System.out.printf("\n\n%s %s\n\n", urltoken[0], urltoken[1]);
                if (urltoken[1].compareTo("TOKEN") == 0) {
                    UrlOrToken = true;
                } else {
                    UrlOrToken = false;
                }

                byte[] buffer2 = new byte[Integer.parseInt(urltoken[0]) * 8];
                DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length);
                socket.receive(packet2);
                System.out.println("Received packet from " + packet2.getAddress().getHostAddress() + ":" + packet2.getPort() + " with message:");
                String messageFinal = new String(packet2.getData(), 0, packet2.getLength());
                System.out.println(messageFinal);

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
                info.put(contaHash,messageFinal);
                contaHash++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            assert socket != null;
            socket.close();
        }
    }
}
