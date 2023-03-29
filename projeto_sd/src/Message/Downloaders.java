package Message;

import java.io.IOException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.rmi.registry.LocateRegistry;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.net.ssl.SSLHandshakeException;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class Downloaders extends UnicastRemoteObject implements InterfaceDownloaders {
    private static final long serialVersionUID = 1L;
    public int id;
    //tokens -> urls
    public static String[] words = {"a", "à", "adeus", "agora", "aí", "ainda", "além", "algo", "alguém", "algum", "alguma", "algumas", "alguns", "ali", "ampla", "amplas", "amplo", "amplos", "ano", "anos", "ante", "antes", "ao", "aos", "apenas", "apoio", "após", "aquela", "aquelas", "aquele", "aqueles", "aqui", "aquilo", "área", "as", "às", "assim", "até", "atrás", "através", "baixo", "bastante", "bem", "boa", "boas", "bom", "bons", "breve", "cá", "cada", "catorze", "cedo", "cento", "certamente", "certeza", "cima", "cinco", "coisa", "coisas", "com", "como", "conselho", "contra", "contudo", "custa", "da", "dá", "dão", "daquela", "daquelas", "daquele", "daqueles", "dar", "das", "de", "debaixo", "dela", "delas", "dele", "deles", "demais", "dentro", "depois", "desde", "dessa", "dessas", "desse", "desses", "desta", "destas", "deste", "destes", "deve", "devem", "devendo", "dever", "deverá", "deverão", "deveria", "deveriam", "devia", "deviam", "dez", "dezanove", "dezasseis", "dezassete", "dezoito", "dia", "diante", "disse", "disso", "disto", "dito", "diz", "dizem", "dizer", "do", "dois", "dos", "doze", "duas", "dúvida", "e", "é", "ela", "elas", "ele", "eles", "em", "embora", "enquanto", "entre", "era", "eram", "éramos", "és", "essa", "essas", "esse", "esses", "esta", "está", "estamos", "estão", "estar", "estas", "estás", "estava", "estavam", "estávamos", "este", "esteja", "estejam", "estejamos", "estes", "esteve", "estive", "estivemos", "estiver", "estivera", "estiveram", "estivéramos", "estiverem", "estivermos", "estivesse", "estivessem", "estivéssemos", "estiveste", "estivestes", "estou", "etc", "eu", "exemplo", "faço", "falta", "favor", "faz", "fazeis", "fazem", "fazemos", "fazendo", "fazer", "fazes", "feita", "feitas", "feito", "feitos", "fez", "fim", "final", "foi", "fomos", "for", "fora", "foram", "fôramos", "forem", "forma", "formos", "fosse", "fossem", "fôssemos", "foste", "fostes", "fui", "geral", "grande", "grandes", "grupo", "há", "haja", "hajam", "hajamos", "hão", "havemos", "havia", "hei", "hoje", "hora", "horas", "houve", "houvemos", "houver", "houvera", "houverá", "houveram", "houvéramos", "houverão", "houverei", "houverem", "houveremos", "houveria", "houveriam", "houveríamos", "houvermos", "houvesse", "houvessem", "houvéssemos", "isso", "isto", "já", "la", "lá", "lado", "lhe", "lhes", "lo", "local", "logo", "longe", "lugar", "maior", "maioria", "mais", "mal", "mas", "máximo", "me", "meio", "menor", "menos", "mês", "meses", "mesma", "mesmas", "mesmo", "mesmos", "meu", "meus", "mil", "minha", "minhas", "momento", "muita", "muitas", "muito", "muitos", "na", "nada", "não", "naquela", "naquelas", "naquele", "naqueles", "nas", "nem", "nenhum", "nenhuma", "nessa", "nessas", "nesse", "nesses", "nesta", "nestas", "neste", "nestes", "ninguém", "nível", "no", "noite", "nome", "nos", "nós", "nossa", "nossas", "nosso", "nossos", "nova", "novas", "nove", "novo", "novos", "num", "numa", "número", "nunca", "o", "obra", "obrigada", "obrigado", "oitava", "oitavo", "oito", "onde", "ontem", "onze", "os", "ou", "outra", "outras", "outro", "outros", "para", "parece", "parte", "partir", "paucas", "pela", "pelas", "pelo", "pelos", "pequena", "pequenas", "pequeno", "pequenos", "per", "perante", "perto", "pode", "pude", "pôde", "podem", "podendo", "poder", "poderia", "poderiam", "podia", "podiam", "põe", "põem", "pois", "ponto", "pontos", "por", "porém", "porque", "porquê", "posição", "possível", "possivelmente", "posso", "pouca", "poucas", "pouco", "poucos", "primeira", "primeiras", "primeiro", "primeiros", "própria", "próprias", "próprio", "próprios", "próxima", "próximas", "próximo", "próximos", "pude", "puderam", "quais", "quáis", "qual", "quando", "quanto", "quantos", "quarta", "quarto", "quatro", "que", "quê", "quem", "quer", "quereis", "querem", "queremas", "queres", "quero", "questão", "quinta", "quinto", "quinze", "relação", "sabe", "sabem", "são", "se", "segunda", "segundo", "sei", "seis", "seja", "sejam", "sejamos", "sem", "sempre", "sendo", "ser", "será", "serão", "serei", "seremos", "seria", "seriam", "seríamos", "sete", "sétima", "sétimo", "seu", "seus", "sexta", "sexto", "si", "sido", "sim", "sistema", "só", "sob", "sobre", "sois", "somos", "sou", "sua", "suas", "tal", "talvez", "também", "tampouco", "tanta", "tantas", "tanto", "tão", "tarde", "te", "tem", "tém", "têm", "temos", "tendes", "tendo", "tenha", "tenham", "tenhamos", "tenho", "tens", "ter", "terá", "terão", "terceira", "terceiro", "terei", "teremos", "teria", "teriam", "teríamos", "teu", "teus", "teve", "ti", "tido", "tinha", "tinham", "tínhamos", "tive", "tivemos", "tiver", "tivera", "tiveram", "tivéramos", "tiverem", "tivermos", "tivesse", "tivessem", "tivéssemos", "tiveste", "tivestes", "toda", "todas", "todavia", "todo", "todos", "trabalho", "três", "treze", "tu", "tua", "tuas", "tudo", "última", "últimas", "último", "últimos", "um", "uma", "umas", "uns", "vai", "vais", "vão", "vários", "vem", "vêm", "vendo", "vens", "ver", "vez", "vezes", "viagem", "vindo", "vinte", "vir", "você", "vocês", "vos", "vós", "vossa", "vossas", "vosso", "vossos", "zero", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "_"};
    public static Connection connection = null;
    public static List<String> StopWords = Arrays.asList(words);
    public static MulticastServer MulticastServer = new MulticastServer();


    public static String mensagemTokens, mensagemUrls;

    public static int porta;

    protected Downloaders() throws RemoteException {
        super();
    }

    public boolean Connected() {
        return true;
    }


    public static void SendInfoAgain() {
        String Barra = " | ";
        String InfoTokenMulti = mensagemTokens.length() + Barra + "TOKEN";
        String InfoUrlMulti = mensagemUrls.length() + Barra + "URL";
        MulticastServer.Myserver(InfoTokenMulti, mensagemTokens);
        MulticastServer.Myserver(InfoUrlMulti, mensagemUrls);
    }


    public static void SendInfo(String url, IQueueRemoteInterface iq) throws RemoteException, SSLHandshakeException, SQLException {
        try {

            Document doc = Jsoup.connect(url).get();
            String titulo = doc.title();
            String citacao;
            //DA ERRO NO LINK APPS.UC.PT
            System.out.println(doc.text().length() + "  " + doc.title().length());

            if (doc.text().length() < 50 && doc.text().length() != titulo.length()) {
                citacao = doc.text().substring(titulo.length() + 1, doc.text().length());
            } else {
                if (doc.text().length() - titulo.length() + 1 > 50) {
                    citacao = doc.text().substring(titulo.length(), titulo.length() + 50);
                } else {
                    citacao = doc.text().substring(titulo.length(), doc.text().length());

                }
            }
            citacao += "...";

            StringTokenizer tokens = new StringTokenizer(doc.text());

            StringBuilder EnviaMulti = new StringBuilder();
            String Ponto = " ; ";
            String Barra = " | ";
            EnviaMulti.append(titulo).append(Barra).append(citacao).append(Barra).append(url).append(Ponto);
            while (tokens.hasMoreElements()) {

                String palavra = tokens.nextToken().toLowerCase();
                System.out.println(palavra);
                if (!StopWords.contains(palavra)) {
                    EnviaMulti.append(palavra).append(Ponto);
                }

            }
            //      titullo | citacao | url ; palavra ; palavra ; palavra ;

            // CENA DOS TOKENS
            int tamanhoInfo = EnviaMulti.length();
            String tamanho = Integer.toString(tamanhoInfo);
            String InfoTokenMulti = tamanho + Barra + "TOKEN";
            // info tamanho|TOKEN
            String fim = EnviaMulti.toString();
            MulticastServer.Myserver(InfoTokenMulti, fim);
            mensagemTokens = fim;


            // CENA DOS URLS
            StringBuilder EnviaMultiLinks = new StringBuilder();
            Elements links = doc.select("a[href]");
            EnviaMultiLinks.append(url).append(Ponto);
            connection.setAutoCommit(false);
            for (Element link : links) {

                EnviaMultiLinks.append(link.attr("abs:href")).append(Ponto);

                iq.coloca(link.attr("abs:href"), 1);
            }


            int tamanhoInfoUrls = EnviaMultiLinks.length();
            String tamanhoURL = Integer.toString(tamanhoInfoUrls);
            String InfoUrlMulti = tamanhoURL + Barra + "URL";
            // info tamanho|TOKEN
            String fimUrl = EnviaMultiLinks.toString();
            mensagemUrls = fimUrl;
            MulticastServer.Myserver(InfoUrlMulti, fimUrl);
            String sql = "update Queue_url set executed = true where url = ? and barrel = ? and executed = false;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, url);
            statement.setInt(2, porta);
            statement.executeUpdate();
            connection.commit();
            System.out.println("IMPRIMEEE");


        } catch (HttpStatusException a) {
            System.out.println("Site indisponivel (BOTS)");
        } catch (SSLHandshakeException e) {
            String sql = "update Queue_url set executed = true where url = ? and barrel = ? and executed = false;";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, url);
            statement.setInt(2, porta);
            statement.executeUpdate();

            System.out.println("Site indisponivel");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("Site deformado");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String args[]) throws SQLException {
        String url = "jdbc:postgresql://localhost/sddb";
        String username = "adminsd";
        String password = "admin";

        DriverManager.registerDriver(new org.postgresql.Driver());
        connection = DriverManager.getConnection(url, username, password);

        try {
            IQueueRemoteInterface iq = (IQueueRemoteInterface) LocateRegistry.getRegistry(7003).lookup("QD");

            Downloaders a = new Downloaders();
            MulticastServer MulticastServer = new MulticastServer();
            //iq.registerDownloader(a);
            boolean result = iq.ConnectDownload(a, Integer.parseInt(args[0]));
            if (!result) {
                System.exit(0);
            }
            porta = Integer.parseInt(args[0]);


            while (true) {
                String t;
                if(iq.giveNumeroBarrels() != 0) {
                    t = iq.retira();
                    if (t != null) {
                        String sql = "update Queue_url set barrel = ?, executed = false where barrel is null and executed is null and url = ?;";
                        PreparedStatement stmt = connection.prepareStatement(sql);
                        stmt.setInt(1, porta);
                        stmt.setString(2, t);
                        stmt.executeUpdate();
                        int barrelsBefore = iq.giveNumeroBarrels();
                        System.out.println(t);
                        SendInfo(t, iq);
                        // TimeUnit.SECONDS.sleep(3);
                        int barrelsAfter = iq.giveNumeroBarrels();
                        if (barrelsAfter > barrelsBefore) {
                            System.out.println("ALGO DE ERRADO NAO ESTA CERTO " + barrelsBefore + " " + barrelsAfter);
                            SendInfoAgain();
                        }
                    }
                    System.out.println(t);
                }



                TimeUnit.SECONDS.sleep(10);
            }
        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
            e.printStackTrace();
        }
    }
}

