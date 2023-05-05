package Message;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.*;

public class Barrels extends UnicastRemoteObject implements IBarrelRemoteInterface {

    public static int conta, a;
    public static String[] words = {"a", "à", "adeus", "agora", "aí", "ainda", "além", "algo", "alguém", "algum", "alguma", "algumas", "alguns", "ali", "ampla", "amplas", "amplo", "amplos", "ano", "anos", "ante", "antes", "ao", "aos", "apenas", "apoio", "após", "aquela", "aquelas", "aquele", "aqueles", "aqui", "aquilo", "área", "as", "às", "assim", "até", "atrás", "através", "baixo", "bastante", "bem", "boa", "boas", "bom", "bons", "breve", "cá", "cada", "catorze", "cedo", "cento", "certamente", "certeza", "cima", "cinco", "coisa", "coisas", "com", "como", "conselho", "contra", "contudo", "custa", "da", "dá", "dão", "daquela", "daquelas", "daquele", "daqueles", "dar", "das", "de", "debaixo", "dela", "delas", "dele", "deles", "demais", "dentro", "depois", "desde", "dessa", "dessas", "desse", "desses", "desta", "destas", "deste", "destes", "deve", "devem", "devendo", "dever", "deverá", "deverão", "deveria", "deveriam", "devia", "deviam", "dez", "dezanove", "dezasseis", "dezassete", "dezoito", "dia", "diante", "disse", "disso", "disto", "dito", "diz", "dizem", "dizer", "do", "dois", "dos", "doze", "duas", "dúvida", "e", "é", "ela", "elas", "ele", "eles", "em", "embora", "enquanto", "entre", "era", "eram", "éramos", "és", "essa", "essas", "esse", "esses", "esta", "está", "estamos", "estão", "estar", "estas", "estás", "estava", "estavam", "estávamos", "este", "esteja", "estejam", "estejamos", "estes", "esteve", "estive", "estivemos", "estiver", "estivera", "estiveram", "estivéramos", "estiverem", "estivermos", "estivesse", "estivessem", "estivéssemos", "estiveste", "estivestes", "estou", "etc", "eu", "exemplo", "faço", "falta", "favor", "faz", "fazeis", "fazem", "fazemos", "fazendo", "fazer", "fazes", "feita", "feitas", "feito", "feitos", "fez", "fim", "final", "foi", "fomos", "for", "fora", "foram", "fôramos", "forem", "forma", "formos", "fosse", "fossem", "fôssemos", "foste", "fostes", "fui", "geral", "grande", "grandes", "grupo", "há", "haja", "hajam", "hajamos", "hão", "havemos", "havia", "hei", "hoje", "hora", "horas", "houve", "houvemos", "houver", "houvera", "houverá", "houveram", "houvéramos", "houverão", "houverei", "houverem", "houveremos", "houveria", "houveriam", "houveríamos", "houvermos", "houvesse", "houvessem", "houvéssemos", "isso", "isto", "já", "la", "lá", "lado", "lhe", "lhes", "lo", "local", "logo", "longe", "lugar", "maior", "maioria", "mais", "mal", "mas", "máximo", "me", "meio", "menor", "menos", "mês", "meses", "mesma", "mesmas", "mesmo", "mesmos", "meu", "meus", "mil", "minha", "minhas", "momento", "muita", "muitas", "muito", "muitos", "na", "nada", "não", "naquela", "naquelas", "naquele", "naqueles", "nas", "nem", "nenhum", "nenhuma", "nessa", "nessas", "nesse", "nesses", "nesta", "nestas", "neste", "nestes", "ninguém", "nível", "no", "noite", "nome", "nos", "nós", "nossa", "nossas", "nosso", "nossos", "nova", "novas", "nove", "novo", "novos", "num", "numa", "número", "nunca", "o", "obra", "obrigada", "obrigado", "oitava", "oitavo", "oito", "onde", "ontem", "onze", "os", "ou", "outra", "outras", "outro", "outros", "para", "parece", "parte", "partir", "paucas", "pela", "pelas", "pelo", "pelos", "pequena", "pequenas", "pequeno", "pequenos", "per", "perante", "perto", "pode", "pude", "pôde", "podem", "podendo", "poder", "poderia", "poderiam", "podia", "podiam", "põe", "põem", "pois", "ponto", "pontos", "por", "porém", "porque", "porquê", "posição", "possível", "possivelmente", "posso", "pouca", "poucas", "pouco", "poucos", "primeira", "primeiras", "primeiro", "primeiros", "própria", "próprias", "próprio", "próprios", "próxima", "próximas", "próximo", "próximos", "pude", "puderam", "quais", "quáis", "qual", "quando", "quanto", "quantos", "quarta", "quarto", "quatro", "que", "quê", "quem", "quer", "quereis", "querem", "queremas", "queres", "quero", "questão", "quinta", "quinto", "quinze", "relação", "sabe", "sabem", "são", "se", "segunda", "segundo", "sei", "seis", "seja", "sejam", "sejamos", "sem", "sempre", "sendo", "ser", "será", "serão", "serei", "seremos", "seria", "seriam", "seríamos", "sete", "sétima", "sétimo", "seu", "seus", "sexta", "sexto", "si", "sido", "sim", "sistema", "só", "sob", "sobre", "sois", "somos", "sou", "sua", "suas", "tal", "talvez", "também", "tampouco", "tanta", "tantas", "tanto", "tão", "tarde", "te", "tem", "tém", "têm", "temos", "tendes", "tendo", "tenha", "tenham", "tenhamos", "tenho", "tens", "ter", "terá", "terão", "terceira", "terceiro", "terei", "teremos", "teria", "teriam", "teríamos", "teu", "teus", "teve", "ti", "tido", "tinha", "tinham", "tínhamos", "tive", "tivemos", "tiver", "tivera", "tiveram", "tivéramos", "tiverem", "tivermos", "tivesse", "tivessem", "tivéssemos", "tiveste", "tivestes", "toda", "todas", "todavia", "todo", "todos", "trabalho", "três", "treze", "tu", "tua", "tuas", "tudo", "última", "últimas", "último", "últimos", "um", "uma", "umas", "uns", "vai", "vais", "vão", "vários", "vem", "vêm", "vendo", "vens", "ver", "vez", "vezes", "viagem", "vindo", "vinte", "vir", "você", "vocês", "vos", "vós", "vossa", "vossas", "vosso", "vossos", "zero", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "_","i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "her", "hers", "herself", "it", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "should", "now"};
    public static List<String> StopWords = Arrays.asList(words);
    public static boolean livre;
    public static IServerRemoteInterface server;

    public static Connection connection = null;
    public static int id;
    public static MulticastClient client = new MulticastClient();
    public static HashMap<Integer, String> hashmapas = new HashMap<>();

    public Barrels() throws RemoteException {
        super();
    }

    /**
     * Function responsible to connect the barrel to the search module
     * @param client barrel a conectar
     * @param id id do barrel
     * @return
     */
    public int connectToServer(IBarrelRemoteInterface client, int id) {
        try {
            int valor;
            server = (IServerRemoteInterface) Naming.lookup("ServerObject");

            valor = server.registerClient(client, id);

            System.out.println("Conexão com o servidor estabelecida.");
            return valor;
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Function that verifies if the barrel is still online
     * @return true if the barrel is connected
     */
    public boolean Connected() {
        return true;
    }

    /**
     * Function that finds the urls (1) where we can find a certain token
     * If the user is looged in, he can also see the links that point to the first urls (1)
     * @param token token to find
     * @param logado 1 if is a logged in client 0 if not
     * @return arraylist of links + links that point to the first one where the token is located
     * @throws SQLException
     * @throws RemoteException
     */
    public ArrayList<String> ProcuraToken(String token,int logado) throws SQLException, RemoteException {
        connection.setAutoCommit(true);
        ArrayList<String> connectados = new ArrayList<>();
        ArrayList<String> news_nova = new ArrayList<>();
        String[] news = token.split(" ");
        int conta = 0;
        for(String i: news){
            if(!StopWords.contains(i)){
                news_nova.add(i);
                conta++;
            }
        }
        String[] news_nova1 = new String[news_nova.size()];
        for(int i = 0; i<news_nova.size(); i++){
            news_nova1[i]=news_nova.get(i);
        }

        String sql= "SELECT i.url, i.titulo, i.citacao ,COUNT(DISTINCT l.url1) FROM url_info i INNER JOIN token_url t ON i.url = t.url LEFT JOIN url_url l ON i.url = l.url2 WHERE l.url1!=l.url2 and t.token1 IN (" + String.join(",", Arrays.stream(news_nova1).map(t -> "?").toArray(String[]::new)) + ") " + " and t.barrel = ? GROUP BY i.url, i.titulo, i.citacao HAVING COUNT(DISTINCT t.token1) >= ? ORDER BY COUNT(DISTINCT l.url1) DESC";

        String updateContador = "update token_url SET contador = contador + 1 where token1 = ? and barrel = ?;";
        PreparedStatement stament = connection.prepareStatement(sql);
        PreparedStatement statementUpdate = connection.prepareStatement(updateContador);
        int i;
        System.out.println(news_nova.size());
        for (i = 0; i < news_nova.size(); i++) {
            stament.setString(i + 1, news_nova.get(i));
            statementUpdate.setString(1, news_nova.get(i));
            statementUpdate.setInt(2, id);
            int rs2 = statementUpdate.executeUpdate();

        }
        stament.setInt(i + 1, id);
        stament.setInt(i + 2, news_nova.size());

        ResultSet rs = stament.executeQuery();


        System.out.printf("Procura da palavra: %s\n", token);
        conta = 0;

        while (rs.next()) {

            connectados.add("URL: " + rs.getString(1) + "\n\tTITULO: " + rs.getString(2) + "\n\tCITAÇÃO: " + rs.getString(3) + "\n");
            if(logado==1){
                String connected = listPage(rs.getString(1));
                connectados.add(connected);
            }


            conta++;
        }
        if (conta == 0) {
            connectados = null;
        }
        rs.close();
        stament.close();
        connection.setAutoCommit(false);
        return connectados;

    }

    /**
     * Function that, given an url, returns a string of urls that have a connection to the link given
     * @param url url to search
     * @return a string that includes every url that point to the url given by the client
     * @throws RemoteException
     * @throws SQLException
     */
    public String listPage(String url) throws RemoteException, SQLException {


        StringBuilder connectados = new StringBuilder();
        String sql = "select distinct(url1) from url_url where url2 = ? and barrel = ? and url2 != url1;";
        PreparedStatement stament = connection.prepareStatement(sql);
        stament.setString(1, url);
        stament.setInt(2, id);
        ResultSet rs = stament.executeQuery();
        int conta = 0;

        while (rs.next()) {
            String composta = "Ligação: " + rs.getString(1) + "\n";
            connectados.append(composta);
            conta++;
        }
        if (conta == 0) {
            connectados = new StringBuilder("Sem resultados");
        }
        rs.close();
        stament.close();
        return connectados.toString();


    }

    /**
     * Function that returns a hashmap with the information that another barrel needs
     * @param a numember of urls already procecced of the barrel that is requesting the information
     * @return hashmap
     */
    public HashMap<Integer, String> sendHash(int a) {
        System.out.println("RECEBI O A COM VALOR DE " + a);
        HashMap<Integer, String> aux = new HashMap<>();

        int last = 0;
        for (Map.Entry<Integer, String> entry : client.sendHashtoBarrels().entrySet()) {
            Integer novo = entry.getKey();
            System.out.println( "_____" + novo + "  " + a);
            if (novo >= a) {
                System.out.println(novo + "  " + a);
                System.out.println(novo);
                aux.put(novo, entry.getValue());
            }
            last = novo;
        }
        System.out.println("TA FEITO COM " + last);


        return aux;
    }


    /**
     * Function that connects the barrel to the database. If it is a new barrel, it will fill his database with the
     * information that the others already have. If it is not new, it will request a hashmap with the information that
     * he don't have.
     * @param args
     * @throws SQLException
     */
    // =========================================================
    public static void main(String args[]) throws SQLException {
        String url = "jdbc:postgresql://localhost/sddb";
        String username = "adminsd";
        String password = "admin";

        id = Integer.parseInt(args[0]);


        DriverManager.registerDriver(new org.postgresql.Driver());
        connection = DriverManager.getConnection(url, username, password);
        connection.setAutoCommit(false);
        System.out.println("Connected to database");
        try {
            livre = true;

            Barrels clientObj = new Barrels();
            a = clientObj.connectToServer(clientObj, id);
            if (a == -1) {
                System.out.println("Id ocupado");
                System.exit(0);
            }

            String verifica = "Select count(distinct(url)) from token_url where barrel = ?";
            PreparedStatement stm = connection.prepareStatement(verifica);
            stm.setInt(1, id);
            ResultSet resultado = stm.executeQuery();

            if (resultado.next()) {
                conta = resultado.getInt(1);
            }
            System.out.println("O VALOR DE A --> " + a);
            a *= 2;
            System.out.println(a);

            if (a != 0) {
                ColocaInfoBarrel m = new ColocaInfoBarrel();
                m.Info(server, connection, hashmapas, id,a);
                client.myClient(connection, id, hashmapas, a);
                m.start();
                client.start();

            } else {
                client.myClient(connection, id, hashmapas, a);
                client.start();

            }

        } catch (
                RemoteException re) {

            System.out.println("Exception in HelloImpl.main: " + re);
        }
    }

}