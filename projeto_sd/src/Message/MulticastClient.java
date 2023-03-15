package Message;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class MulticastClient extends Thread {
    private String MULTICAST_ADDRESS = "224.3.2.1";
    private int PORT = 4321;



    public void run() {
        MulticastSocket socket = null;
        try {
            socket = new MulticastSocket(PORT);  // create socket and bind it
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);
            socket.joinGroup(group);
            while (true) {
                byte[] buffer = new byte[256];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);
                String messageTamanho = new String(packet.getData(), 0, packet.getLength());

                byte[] buffer2 = new byte[Integer.parseInt(messageTamanho)*8];
                DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length);
                socket.receive(packet2);
                System.out.println("Received packet from " + packet2.getAddress().getHostAddress() + ":" + packet2.getPort() + " with message:");
                String messageFinal = new String(packet2.getData(), 0, packet2.getLength());
                System.out.println(messageFinal);


            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
