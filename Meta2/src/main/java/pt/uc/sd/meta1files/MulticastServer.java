package pt.uc.sd.meta1files;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;


public class MulticastServer extends Thread {
    private String MULTICAST_ADDRESS = "224.3.2.1";
    private int PORT = 4321;
    private long SLEEP_TIME = 5000;

    public String palavra;
    public String palavraFim;

    /**
     * constructor1
     */
    public MulticastServer() {
        super("Server " + (long) (Math.random() * 1000));
    }


    /**
     * function that gets the type of message that he needs to send and the message itself. It also calls the funtion
     * to send the message
     * @param palavraS size and type of the message
     * @param PalavraFinal message
     */
    public synchronized void Myserver(String palavraS,String PalavraFinal) {
        palavra = palavraS;
        palavraFim = PalavraFinal;
        run();

    }

    /**
     * function that sends the information to the multicast client
     */
    public synchronized void run() {
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
