package Message;

import java.io.IOException;
import java.lang.reflect.Array;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;


import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;

public class MulticastServer extends Thread {
    private String MULTICAST_ADDRESS = "224.3.2.1";
    private int PORT = 4321;
    private long SLEEP_TIME = 5000;

    public String palavra;
    public String palavraFim;


    public MulticastServer() {
        super("Server " + (long) (Math.random() * 1000));
    }

    public synchronized void Myserver(String palavraS,String PalavraFinal) {
        palavra = palavraS;
        palavraFim = PalavraFinal;
        run();

    }

    public void run() {
        MulticastSocket socket = null;
        long counter = 0;

        try {

            socket = new MulticastSocket();  // create socket without binding it (only for sending)


            byte[] buffer = palavra.getBytes();
            System.out.printf("SEND: %s\n",palavra);
            InetAddress group = InetAddress.getByName(MULTICAST_ADDRESS);

            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, group, PORT);
            socket.send(packet);

            byte[] buffer2 = palavraFim.getBytes();
            DatagramPacket packet2 = new DatagramPacket(buffer2, buffer2.length, group, PORT);
            socket.send(packet2);


            try {
                sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {

            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            socket.close();
        }
    }
}
