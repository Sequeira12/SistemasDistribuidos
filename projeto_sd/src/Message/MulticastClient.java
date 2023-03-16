package Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

public class MulticastClient extends Thread {
    private String MULTICAST_ADDRESS = "224.3.2.1";
    private int PORT = 4321;
    Connection connection = null;
    int id;
    public void myClient(Connection conn, int i){
        connection=conn;
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
                System.out.printf("%s\n",urltoken[1]);
                if(urltoken[1].compareTo("TOKEN") == 0){
                    UrlOrToken = true;
                }else{
                    UrlOrToken = false;
                }

                byte[] buffer2 = new byte[Integer.parseInt(urltoken[0]) * 8];
                DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length);
                socket.receive(packet2);
                System.out.println("Received packet from " + packet2.getAddress().getHostAddress() + ":" + packet2.getPort() + " with message:");
                String messageFinal = new String(packet2.getData(), 0, packet2.getLength());
                System.out.println(messageFinal);
                int contador = 0;
                // divide a string em tokens usando o caractere "|"
                if (messageFinal.contains(" ;")) {
                    String[] tokens = messageFinal.split(" ;");
                    for (String token : tokens) {
                        if (token != null && token.contains(" | ")) {
                            String[] news = token.split(" \\| ");
                            String tokenID = null, url = null;
                            for (String nova : news) {
                                System.out.printf("%d\n",contador);
                                if (contador == 0) {
                                    tokenID = nova.trim();
                                    //  System.out.println("TOKEN " + trimmedToken);
                                } else {
                                    url = nova.trim();
                                    //  System.out.println("URL " + trimmedToken);
                                }

                                contador++;

                            }
                            contador = 0;
                            if(UrlOrToken) {
                                String sql = "insert into token_url (barrel,token1,url) values(?,?,?)";
                                PreparedStatement stament = connection.prepareStatement(sql);
                                stament.setInt(1, id);
                                stament.setString(2, tokenID);
                                stament.setString(3, url);
                                stament.executeUpdate();
                            }else{
                                String sql = "insert into url_url (barrel,url1,url2) values(?,?,?)";
                                PreparedStatement stament = connection.prepareStatement(sql);
                                stament.setInt(1, id);
                                stament.setString(2, tokenID);
                                stament.setString(3, url);
                                stament.executeUpdate();
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
            socket.close();
        }
    }
}
