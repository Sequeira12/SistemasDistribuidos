package pt.uc.sd;

import java.rmi.RemoteException;
import java.rmi.server.RemoteObject;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;


public class ClientWeb extends UnicastRemoteObject implements InterfaceClienteServer {


    public static boolean Estat = true;
    public static ArrayList<IBarrelRemoteInterface> Barrels = new ArrayList<>();
    public static ArrayList<InterfaceDownloaders> Downloaders = new ArrayList<>();


    public static ArrayList<IBarrelRemoteInterface> BarrelsAnt = new ArrayList<>();
    public static ArrayList<InterfaceDownloaders> DownloadersAnt = new ArrayList<>();

    public static MessageServerInterface h;
    public static int login;

    protected ClientWeb() throws RemoteException {
        super();
    }


    /**
     * Function used to show the barrels and downloaders information and top 10 searches
     *
     * @throws RemoteException
     * @throws SQLException
     */
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

    /**
     * Update arrayList of Barrels and Downloaders on the client side
     *
     * @param b arraylist of barrels
     * @param d arraylist of downloaders
     */
    public void atualizaStatus(ArrayList<IBarrelRemoteInterface> b, ArrayList<InterfaceDownloaders> d) {
        Barrels = b;
        Downloaders = d;
    }

    /**
     * Show status in Real-time
     *
     * @throws RemoteException
     * @throws SQLException
     */
    public static void Estatistica() throws SQLException, RemoteException {
        int contador = 0;
        while (Estat) {
            if (contador == 0) {
                showStatus();
                System.out.println("Para voltar ao Menu clique em ^C" + "\n");
            }

            if ((Barrels.size() != BarrelsAnt.size() || Downloaders.size() != DownloadersAnt.size()) && contador > 0) {
                try {
                    showStatus();
                    System.out.println("Para voltar ao Menu clique em ^C" + "\n");
                } catch (SQLException | RemoteException e) {
                    throw new RuntimeException(e);
                }
                BarrelsAnt = Barrels;
                DownloadersAnt = Downloaders;

            }
            contador++;

            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    /**
     * Verifies if the client is still active
     *
     * @return
     * @throws RemoteException
     */
    public boolean Connected() throws RemoteException {
        return true;
    }

    /**
     * Function that displays an arraylist of links in groups of ten
     *
     * @param links arraylist of links to be displayed
     */
    public static void Imprime10_10(ArrayList<String> links) {
        int conta = 0;
        System.out.printf("Links Associados à sua procura: (PAGINA %d) \n", conta + 1);

        if (login == 1) {
            for (int i = 0; i < 10 && i < links.size() / 2; i++) {

                System.out.printf(" (%d) - %s \n", (i + 1), links.get(i * 2));
            }
        } else {
            for (int i = 0; i < 10 && i < links.size(); i++) {

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
            if (conta > (links.size() / 2) / 10 && login == 1) {
                conta--;
            }
            if (conta > (links.size()) / 10 && login == 0) {
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

}





