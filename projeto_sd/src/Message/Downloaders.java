package Message;


import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;


import java.rmi.registry.LocateRegistry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.rmi.RemoteException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import java.util.concurrent.TimeUnit;

public class Downloaders {
    private static final long serialVersionUID = 1L;

    //tokens -> urls
    public static HashMap<String, String> tokens_url = new HashMap<String, String>();


    public static MulticastServer MulticastServer = new MulticastServer();

    public static void SendInfo(String url, IQueueRemoteInterface iq) throws RemoteException {
        try {
            Document doc = Jsoup.connect(url).get();
            StringTokenizer tokens = new StringTokenizer(doc.text());
            int conta = 0;
            while (tokens.hasMoreElements() && ++conta < 100) {
                System.out.println(tokens.nextToken().toLowerCase());
                String palavra = tokens.nextToken().toLowerCase();
                tokens_url.put(palavra, url);


            }
            StringBuilder EnviaMulti = new StringBuilder();
            String Ponto = " ; ";
            String Barra = " | ";


            for (HashMap.Entry<String, String> tokens_url : tokens_url.entrySet()) {
                EnviaMulti.append(tokens_url.getKey()).append(Barra).append(tokens_url.getValue()).append(Ponto);
            }
            String fim = EnviaMulti.toString();
            MulticastServer.run(fim);
            System.out.println(EnviaMulti);
            tokens_url.clear();
            //  MulticastServer.run();

            Elements links = doc.select("a[href]");
            for (Element link : links) {

                System.out.println(link.text() + "\n" + link.attr("abs:href") + "\n");
                iq.coloca(link.attr("abs:href"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        try {
            IQueueRemoteInterface iq = (IQueueRemoteInterface) LocateRegistry.getRegistry(7003).lookup("QD");
            MulticastServer MulticastServer = new MulticastServer();
            while (true) {
                String t;
                t = iq.retira();
                if (t != null) {
                    System.out.println(t);
                    SendInfo(t, iq);
                }

                System.out.println(t);

                TimeUnit.SECONDS.sleep(10);
            }
        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
            e.printStackTrace();
        }
    }
}

