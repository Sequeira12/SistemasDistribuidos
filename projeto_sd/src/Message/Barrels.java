package Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Barrels extends UnicastRemoteObject implements IClientRemoteInterface {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private IServerRemoteInterface server;
    public static HashMap<String, ArrayList<Element>> urls_ligacoes = new HashMap<String, ArrayList<Element>>();
    public static HashMap<String, ArrayList<String>> tokens_url = new HashMap<String, ArrayList<String>>();

    public Barrels() throws RemoteException {
        super();
    }

    public void connectToServer(IClientRemoteInterface client) {
        try {
            server = (IServerRemoteInterface) Naming.lookup("ServerObject");

            server.registerClient(client);
            System.out.println("Conexão com o servidor estabelecida.");
        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public boolean Connected() {
        return true;
    }

    public ArrayList<String> InsereUrl(String url) {


        ArrayList<String> connectados = new ArrayList<>();
        connectados = tokens_url.get(url);
        System.out.printf("Procura da palavra: %s\n", url);
        return connectados;
    }


    // =========================================================
    public static void main(String args[]) {

        try {
            String El1 = "www.uc.pt";
            String El2 = "www.dei.uc.pt";
            ArrayList<String> arraypalavra = new ArrayList<String>();
            arraypalavra.add(El1);
            arraypalavra.add(El2);
            System.out.println("IMIMIMIM");
            String teste = "de | https://apps.uc.pt/courses/pt/index?designacao=&uno_sigla=&cic_tipo=TERCEIRO&submitform=Pesquisar#courses_list ; Ciências | https://apps.uc.pt/courses/pt/index?designacao=&uno_sigla=&cic_tipo=TERCEIRO&submitform=Pesquisar#courses_list ; Ambiente | https://apps.uc.pt/courses/pt/index?designacao=&uno_sigla=&cic_tipo=TERCEIRO&submitform=Pesquisar#courses_list ; do | https://apps.uc.pt/courses/pt/index?designacao=&uno_sigla=&cic_tipo=TERCEIRO&submitform=Pesquisar#courses_list ; Cursos | https://apps.uc.pt/courses/pt/index?designacao=&uno_sigla=&cic_tipo=TERCEIRO&submitform=Pesquisar#courses_list ; ";
            int contador = 0;
            // divide a string em tokens usando o caractere "|"
            if (teste.contains(" ;")) {
                String[] tokens = teste.split(" ;");
                for (String token : tokens) {
                    if (token != null && token.contains(" | ")) {
                        String[] news = token.split(" \\| ");
                        for (String nova : news) {
                            if(contador == 0){
                                String trimmedToken = nova.trim();
                                System.out.println("TOKEN"+trimmedToken);
                            }else{
                                String trimmedToken = nova.trim();
                                System.out.println("URL"+trimmedToken);
                            }
                            contador++;



                        }
                        contador = 0;

                    }
                }
            }
            tokens_url.put("Coimbra", arraypalavra);

            Barrels clientObj = new Barrels();
            clientObj.connectToServer(clientObj);

            MulticastClient cliente = new MulticastClient();
            cliente.start();


        } catch (RemoteException re) {

            System.out.println("Exception in HelloImpl.main: " + re);
        }
    }

}