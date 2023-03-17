package Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.sql.*;
import java.util.Arrays;

public class MulticastClient extends Thread {
    private String MULTICAST_ADDRESS = "224.3.2.1";
    private int PORT = 4321;
    Connection connection = null;
    int id;

    public void myClient(Connection conn, int i) {
        connection = conn;
        id = i;
    }

    public void run() {
        MulticastSocket socket = null;
        try {
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
                System.out.printf("%s\n", urltoken[1]);
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
                int contador = 0;
                int conta2 = 0;
                String citacao = null, titulo = null, url = null;
                ;
                // divide a string em tokens usando o caractere "|"
                if (messageFinal.contains(" ;")) {
                    String[] tokens = messageFinal.split(" ;");
                    for (String token : tokens) {
                        if (token != null && token.contains(" | ")) {
                            String[] news = token.split(" \\| ");
                            String tokenID = null;
                            if (conta2 == 1 && UrlOrToken) {
                                String sql = "insert into url_info (url,titulo,citacao) values(?,?,?)";
                                PreparedStatement stament = connection.prepareStatement(sql);
                                stament.setString(1, url);
                                stament.setString(2, titulo);
                                stament.setString(3, citacao);
                                stament.executeUpdate();
                                conta2++;
                            }
                            if (conta2 == 0 && UrlOrToken) {
                                titulo = news[0];
                                citacao = news[1];
                                url = news[2];
                                System.out.println(titulo + citacao);
                                conta2++;

                            } else {


                                for (String nova : news) {

                                    if (contador == 0) {
                                        tokenID = nova.trim();

                                    } else {
                                        url = nova.trim();

                                    }

                                    contador++;

                                }
                                contador = 0;


                                if (UrlOrToken) {
                                    String sql = "select count(*)  from token_url where token_url.token1 = ? and token_url.url = ?;";
                                    PreparedStatement stament = connection.prepareStatement(sql);
                                    stament.setString(1, tokenID);
                                    stament.setString(2, url);

                                    ResultSet rs = stament.executeQuery();
                                    if (rs.next()) {

                                        int a = rs.getInt(1);
                                        if (a == 0) {
                                            String sql2 = "insert into token_url (barrel,token1,url) values(?,?,?)";
                                            PreparedStatement stament2 = connection.prepareStatement(sql2);
                                            stament2.setInt(1, id);
                                            stament2.setString(2, tokenID);
                                            stament2.setString(3, url);
                                            stament2.executeUpdate();
                                        }
                                    }
                                } else {
                                    String sql2 = "insert into url_url (barrel,url1,url2) values(?,?,?)";
                                    PreparedStatement stament2 = connection.prepareStatement(sql2);
                                    stament2.setInt(1, id);
                                    stament2.setString(2, tokenID);
                                    stament2.setString(3, url);
                                    stament2.executeUpdate();
                                }
                            }
                        }
                    }
                }


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
