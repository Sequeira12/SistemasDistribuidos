package Message;


import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.io.IOException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.rmi.registry.LocateRegistry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.rmi.RemoteException;

import java.util.*;

import java.util.concurrent.TimeUnit;

public class Downloaders {
    private static final long serialVersionUID = 1L;

    //tokens -> urls



    public static MulticastServer MulticastServer = new MulticastServer();

    public static void SendInfo(String url, IQueueRemoteInterface iq) throws RemoteException {
        try {
            Document doc = Jsoup.connect(url).get();
            StringTokenizer tokens = new StringTokenizer(doc.text());

            StringBuilder EnviaMulti = new StringBuilder();
            String Ponto = " ; ";
            String Barra = " | ";
            while (tokens.hasMoreElements() ) {

                String palavra = tokens.nextToken().toLowerCase();
                System.out.println(palavra);

                EnviaMulti.append(palavra).append(Barra).append(url).append(Ponto);
            }


            // CENA DOS TOKENS
            int tamanhoInfo = EnviaMulti.length();
            String tamanho = Integer.toString(tamanhoInfo);
            String InfoTokenMulti = tamanho + Barra + "TOKEN";
            // info tamanho|TOKEN
            MulticastServer.run(InfoTokenMulti);
            String fim = EnviaMulti.toString();
            MulticastServer.run(fim);



            // CENA DOS URLS
           StringBuilder EnviaMultiLinks = new StringBuilder();
            Elements links = doc.select("a[href]");
            for (Element link : links) {

                EnviaMultiLinks.append(url).append(Barra).append(link.attr("abs:href")).append(Ponto);
               // System.out.println(link.text() + "\n" + link.attr("abs:href") + "\n");
                iq.coloca(link.attr("abs:href"));
            }

            int tamanhoInfoUrls = EnviaMulti.length();
            String tamanhoURL = Integer.toString(tamanhoInfoUrls);
            String InfoUrlMulti = tamanhoURL + Barra + "URL";
            // info tamanho|TOKEN
            MulticastServer.run(InfoUrlMulti);
            String fimUrl = EnviaMultiLinks.toString();
            MulticastServer.run(fimUrl);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) throws SQLException {


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

