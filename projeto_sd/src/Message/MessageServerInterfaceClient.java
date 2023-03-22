package Message;

import javax.swing.text.Element;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class MessageServerInterfaceClient extends UnicastRemoteObject implements InterfaceClienteServer{


    public static ArrayList<IClientRemoteInterface> Barrels = new ArrayList<>();
    public static ArrayList<Integer> Downloaders = new ArrayList<>();

    protected MessageServerInterfaceClient() throws RemoteException {
        super();
    }

    public static void showStatus() {
        System.out.println("Número de barrels ativos: " + Barrels.size());
        System.out.println("Número de Downloaders ativos: " + Downloaders.size());
    }

    public void atualizaStatus(ArrayList<IClientRemoteInterface> b,ArrayList<Integer> d){
        Barrels=b;
        Downloaders=d;
    }

    public static int login;

    public static void Imprime10_10(ArrayList<String> links) {
        int conta = 0;
        System.out.printf("Links Associados à sua procura: (PAGINA %d) \n", conta + 1);
        for (int i = 0; i < 10 && i < links.size(); i++) {
            System.out.printf(" - %s\n", links.get(i));
        }
        System.out.println("Digite '+' se quer ir para a seguinte\nDigite '-' se deseja ir para a anterior\nDigite 0 se quer terminar a pesquisa");
        Scanner client = new Scanner(System.in);

        while (client.hasNextLine()) {
            String s = client.nextLine();
            if (s.compareTo("+") == 0) {
                conta++;
            } else if (s.compareTo("-") == 0) {
                conta--;
            } else if (s.compareTo("0") == 0) {
                break;
            }
            if (conta < 0) {
                conta = 0;
            }
            if (conta > links.size() / 10) {
                conta--;
            }
            System.out.printf("Links Associados à sua procura: (PAGINA %d) \n", conta + 1);
            for (int i = 10 * conta; i < 10 * (conta + 1) && i < links.size(); i++) {
                System.out.printf(" - %s\n", links.get(i));
            }
            System.out.println("Digite '+' se quer ir para a seguinte\nDigite '-' se deseja ir para a anterior\nDigite 0 se quer terminar a pesquisa\n");

        }


    }


    public static void main(String args[]) {

		/* This might be necessary if you ever need to download classes:
		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());
		*/

        try {

            MessageServerInterface h = (MessageServerInterface) LocateRegistry.getRegistry(7001).lookup("SD");

            MessageServerInterfaceClient a = new MessageServerInterfaceClient();
            h.addClient(a);

            Scanner scanner = new Scanner(System.in);
            //System.out.println("Digite um número:");
            System.out.println("Bem vindo:\nEscolha as seguintes opções:\n1 - Login \n2 - Registar\n3 - Continuar como convidado\n");
            if (scanner.hasNextInt()) {
                int num = scanner.nextInt();

                if (num == 1) {
                    boolean Existe = true;
                    while (Existe) {
                        String nome = null, pass = null;
                        Scanner nova = new Scanner(System.in);
                        System.out.println("LOGIN:\nColoque o seu Usename:");
                        if (nova.hasNextLine()) {

                            nome = nova.nextLine();
                        }
                        Scanner nova2 = new Scanner(System.in);
                        System.out.println("Coloque a pass");
                        if (nova2.hasNextLine()) {
                            pass = nova2.nextLine();
                        }
                        if (h.Login(nome, pass)) {
                            System.out.println("Login realizado com Sucesso!!!");
                            Existe = false;
                        }
                    }
                    login = 1;
                } else if (num == 2) {
                    boolean Existe = true;
                    while (Existe) {
                        String nome = null, pass = null;
                        Scanner nova = new Scanner(System.in);
                        System.out.println("REGISTO:\nColoque o seu Usename:");
                        if (nova.hasNextLine()) {

                            nome = nova.nextLine();
                        }
                        Scanner nova2 = new Scanner(System.in);
                        System.out.println("Coloque a pass");
                        if (nova2.hasNextLine()) {
                            pass = nova2.nextLine();
                        }
                        if (h.Register(nome, pass)) {
                            System.out.println("Registo realizado com Sucesso!!!");
                            Existe = false;
                        }
                    }
                    login = 1;
                } else if (num == 3) {
                    login = 0;
                }
            }


            System.out.println(h.sayHello(login));
            while (scanner.hasNextInt()) {

                int numero = scanner.nextInt();


                if (numero == 1) {
                    System.out.println("Coloque o token:");
                    Scanner scanner2 = new Scanner(System.in);
                    String palavra = scanner2.nextLine();

                    ArrayList<String> linksAssociados = h.FindUrlWithToken(palavra);

                    if (linksAssociados == null) {
                        System.out.println("Links Associados à sua procura:");
                        System.out.println("Sem resultados encontrados!!");
                    } else {
                        if (linksAssociados.get(0).compareTo("Sem Resultados") == 0) {
                            System.out.println("Links Associados à sua procura:");
                            System.out.println("Barrels Indisponiveis!!");
                        } else {
                            Imprime10_10(linksAssociados);

                        }
                    }
                } else if (numero == 2) {
                    System.out.println("COLOQUE O SITE:");
                    Scanner scanner2 = new Scanner(System.in);
                    String palavra = scanner2.nextLine();
                    h.SendUrltoQueue(palavra);
                } else if (numero == 3) {
                    showStatus();


                } else if (numero == 4 && login == 1) {
                    System.out.println("COLOQUE O SITE:");
                    Scanner scanner2 = new Scanner(System.in);
                    String palavra = scanner2.nextLine();
                    ArrayList<String> linksAssociados = h.listPagesConnectedtoAnotherPage(palavra);
                    if (linksAssociados == null) {
                        System.out.println("Links Associados à sua procura:");
                        System.out.println("Sem resultados encontrados!!");
                    } else {
                        if (linksAssociados.get(0).compareTo("Sem Resultados") == 0) {
                            System.out.println("Links Associados à sua procura:");
                            System.out.println("Barrels Indisponiveis!!");
                        } else {
                            Imprime10_10(linksAssociados);

                        }
                    }

                } else {
                    break;
                }
                System.out.println(h.sayHello(login));
                System.out.println("\nDigite um número:");
            }
        } catch (Exception e) {
            System.out.println("Exception in main: PUTAAAA " + e);
            e.printStackTrace();
        }

    }

}



