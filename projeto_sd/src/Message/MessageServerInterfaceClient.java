package Message;

import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class MessageServerInterfaceClient extends UnicastRemoteObject implements InterfaceClienteServer {


    public static ArrayList<IClientRemoteInterface> Barrels = new ArrayList<>();
    public static ArrayList<InterfaceDownloaders> Downloaders = new ArrayList<>();

    public static MessageServerInterface h;
    public static int login;

    protected MessageServerInterfaceClient() throws RemoteException {
        super();
    }

    public static void showStatus() throws RemoteException, SQLException {
        System.out.println("|   ESTATISTICAS   |");
        System.out.println("Número de barrels ativos: ");
        for (int i = 0; i < Barrels.size(); i++) {
            String endpoint = RemoteObject.toStub(Barrels.get(i)).toString();
            // Extrai o IP e a porta do endpoint
            String[] endpointParts = endpoint.split(":");

            String ip = endpointParts[2].substring(1);
            String porta = endpointParts[3].substring(0, endpointParts[3].lastIndexOf("]"));

            // Exibe o IP e a porta do endpoint
            System.out.println((i + 1) + " - IP: " + ip + " Porta: " + porta);

        }
        System.out.println();
        System.out.println("Número de Downloaders ativos: ");
        for (int i = 0; i < Downloaders.size(); i++) {
            String endpoint = RemoteObject.toStub(Downloaders.get(i)).toString();
            // Extrai o IP e a porta do endpoint
            String[] endpointParts = endpoint.split(":");

            String ip = endpointParts[2].substring(1);
            String porta = endpointParts[3].substring(0, endpointParts[3].lastIndexOf("]"));

            // Exibe o IP e a porta do endpoint
            System.out.println((i + 1) + " - IP: " + ip + " Porta: " + porta);
        }
        System.out.println();
        System.out.println(h.VerificaTop10());


    }

    public void atualizaStatus(ArrayList<IClientRemoteInterface> b, ArrayList<InterfaceDownloaders> d) {
        Barrels = b;
        Downloaders = d;
    }


    public boolean Connected() throws RemoteException {
        return true;
    }


    public static void Imprime10_10(ArrayList<String> links) {
        int conta = 0;
        System.out.printf("Links Associados à sua procura: (PAGINA %d) \n", conta + 1);
        for (int i = 0; i < 10 && i < links.size() / 2; i++) {
            System.out.printf(" (%d) - %s \n", (i + 1), links.get(i * 2));


        }
        if (login == 1) {
            for (int i = 0; i < 10 && i < links.size() / 2; i++) {

                System.out.printf(" (%d) - %s \n", (i + 1), links.get(i * 2));
            }
        } else {
            for (int i = 0; i < 10  && i < links.size(); i++) {

                System.out.printf(" (%d) - %s \n", (i + 1), links.get(i));
            }
        }
        if (login == 1) {
            System.out.println("Digite o nº do url que deseja ver as ligações\nDigite '+' se quer ir para a seguinte\nDigite '-' se deseja ir para a anterior\nDigite 0 se quer terminar a pesquisa");
        } else {
            System.out.println("Digite '+' se quer ir para a seguinte\nDigite '-' se deseja ir para a anterior\nDigite 0 se quer terminar a pesquisa");
        }
        Scanner client = new Scanner(System.in);
        int numMin, numMax;
        int num = -1;
        while (client.hasNextLine()) {
            String s = client.nextLine();
            if (s.compareTo("+") == 0) {
                conta++;
            } else if (s.compareTo("-") == 0) {
                conta--;
            } else if (s.compareTo("0") == 0) {
                break;
            } else {
                num = Integer.parseInt(s);
            }
            if (conta < 0) {
                conta = 0;
            }
            if (conta > (links.size() / 2) / 10) {
                conta--;
            }
            numMin = 10 * conta;
            numMax = 10 * (conta + 1);
            if (num > numMin && num < numMax && login == 1) {
                System.out.println("RESULTADO DAS LIGAÇÕES!!");
                System.out.println(links.get(num + (num - 1)));
                System.out.println("Digite qualquer tecla se quer ver a restante pesquisa\n");
                client.hasNextLine();
                client.nextLine();

            }

            System.out.printf("\nLinks Associados à sua procura: (PAGINA %d) \n", conta + 1);
            if (login == 1) {
                for (int i = 10 * conta; i < 10 * (conta + 1) && i < links.size() / 2; i++) {

                    System.out.printf(" (%d) - %s \n", (i + 1), links.get(i * 2));
                }
            } else {
                for (int i = 10 * conta; i < 10 * (conta + 1) && i < links.size(); i++) {

                    System.out.printf(" (%d) - %s \n", (i + 1), links.get(i));
                }
            }

            if (login == 1) {
                System.out.println("Digite o nº do url que deseja ver as ligações\nDigite '+' se quer ir para a seguinte\nDigite '-' se deseja ir para a anterior\nDigite 0 se quer terminar a pesquisa");
            } else {
                System.out.println("Digite '+' se quer ir para a seguinte\nDigite '-' se deseja ir para a anterior\nDigite 0 se quer terminar a pesquisa");
            }
        }


    }


    public static void main(String args[]) {

		/* This might be necessary if you ever need to download classes:
		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());
		*/

        try {

            h = (MessageServerInterface) LocateRegistry.getRegistry(7001).lookup("SD");

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
                        System.out.println("LOGIN:\nColoque o seu Username:");
                        if (nova.hasNextLine()) {

                            nome = nova.nextLine();
                        }
                        Scanner nova2 = new Scanner(System.in);
                        System.out.println("Coloque a pass: ");
                        if (nova2.hasNextLine()) {
                            pass = nova2.nextLine();
                        }
                        if (h.Login(nome, pass)) {
                            System.out.println("Login realizado com Sucesso!!!\n");
                            Existe = false;
                        }
                    }
                    login = 1;
                } else if (num == 2) {
                    boolean Existe = true;
                    while (Existe) {
                        String nome = null, pass = null;
                        Scanner nova = new Scanner(System.in);
                        System.out.println("REGISTO:\nColoque o seu Username:");
                        if (nova.hasNextLine()) {

                            nome = nova.nextLine();
                        }
                        Scanner nova2 = new Scanner(System.in);
                        System.out.println("Coloque a password: ");
                        if (nova2.hasNextLine()) {
                            pass = nova2.nextLine();
                        }
                        if (h.Register(nome, pass)) {
                            System.out.println("Registo realizado com Sucesso!!\n");
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

                    ArrayList<String> linksAssociados = h.FindUrlWithToken(palavra, login);

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
                    String linksAssociados = h.listPagesConnectedtoAnotherPage(palavra);
                    if (linksAssociados == null) {
                        System.out.println("Links Associados à sua procura:");
                        System.out.println("Sem resultados encontrados!!");
                    } else {
                        if (linksAssociados.compareTo("Sem Resultados") == 0) {
                            System.out.println("Links Associados à sua procura:");
                            System.out.println("Barrels Indisponiveis!!");
                        } else {
                            System.out.println(linksAssociados);

                        }
                    }

                } else {
                    System.exit(0);
                    break;
                }
                System.out.println(h.sayHello(login));
                System.out.println("\nDigite um número:");
            }
        } catch (Exception e) {
            System.out.println("Exception in main: " + e);
            e.printStackTrace();
        }

    }

}



