package Message;

import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Random;
import java.util.ArrayList;

public class MessageServerInterfaceServer extends UnicastRemoteObject implements MessageServerInterface, IServerRemoteInterface, ISearcheQueue {

    public static ArrayList<IClientRemoteInterface> Barrels = new ArrayList<>();
    public static IServerRemoteInterface Servidor;

    public static ISearcheQueue QueueSearche;
    public static ArrayList<Integer> Download = new ArrayList<>();

    public static IQueueRemoteInterface iq;


    public MessageServerInterfaceServer() throws RemoteException {
        super();
    }

    public void SendInfoDownloaders(ArrayList<Integer> Download) {
        int contador = 0;
        for (int i = 0; i < Download.size(); i++) {
            for (int j = 0; j < MessageServerInterfaceServer.Download.get(j); j++) {
                if (!Objects.equals(Download.get(i), MessageServerInterfaceServer.Download.get(i))) {
                    contador++;
                }
            }
            if (contador == MessageServerInterfaceServer.Download.size()) {
                MessageServerInterfaceServer.Download.add(Download.get(i));
                //CHAMAR AQUI A TREAD DOS BARRELS COM O NOVO DOWNLOADER
            }
            contador = 0;
        }
    }

    public void registerClient(IClientRemoteInterface client) throws RemoteException {
        Barrels.add(client);
        System.out.println("Barrel registrado no servidor.");
    }


    public void run() throws RemoteException {
        int i = 0;
        while (true) {
            try {
                System.out.printf("Tamanho de Barrels Disponiveis %d\n", Barrels.size());
                for (i = 0; i < Barrels.size(); i++) {

                    Barrels.get(i).Connected();

                }
                Thread.sleep(10000);


            } catch (ServerException a) {
                System.out.println("Server Exception \n");
            } catch (ConnectException a) {
                // Se a conexão não existir retiro da lista
                unregisterClient(i);

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void unregisterClient(int posicao) throws RemoteException {
        Barrels.remove(posicao);
        for (int k = posicao; k < Barrels.size() - 1; k++) {
            Barrels.set(k, Barrels.get(k + 1));
            if (k == Barrels.size() - 1) {
                Barrels.set(k, null);

            }
        }
        System.out.println("Barrel removido do servidor.");
    }


    public ArrayList<String> FindUrlWithToken(String url) throws RemoteException, SQLException {
        ArrayList<String> connectados = new ArrayList<>();
        System.out.println(Barrels.size());
        if (Barrels.size() != 0) {


            Random gerador = new Random();
            int numero = gerador.nextInt((Barrels.size()));
            System.out.printf("Barrel que vai executar a Procura ---> %d\n", numero);
            if (Barrels.get(numero) != null) {
                connectados = Barrels.get(numero).ProcuraToken(url);
            }
        } else {
            String No = "Sem Resultados";
            connectados.add(No);
        }
        return connectados;
    }

    public void SendUrltoQueue(String url) throws RemoteException {
        iq.coloca(url);
    }

    public String sayHello() throws RemoteException {

        return "Bem-vindo\nEscolha as opções:\n1-Token para procurar\n2-URL para indexar\n0-exit\nObrigado!!\n";
    }

    public String SendUrlQueue(String token) throws RemoteException {

        return null;
        //return mensagem;
    }

    // =========================================================
    public static void main(String args[]) {

        try {
            /**
             * Fazer por multicast a ligacao do Barrels com o Search Module
             * ter uma array com os (ips portos) dos barrels que estão conectados
             *
             * Fazer funcao aqui para que o barrels invoce para adicionar a porta dele a um arraylist de porta globais do servidor
             *
             */


            iq = (IQueueRemoteInterface) LocateRegistry.getRegistry(7003).lookup("QD");

            MessageServerInterfaceServer h = new MessageServerInterfaceServer();
            Registry r = LocateRegistry.createRegistry(7001);
            r.rebind("SD", h);


            QueueSearche = (ISearcheQueue) LocateRegistry.getRegistry(7005).lookup("QS"); // LIGACAO RMI




            Servidor = new MessageServerInterfaceServer();
            java.rmi.registry.LocateRegistry.createRegistry(1099);
            java.rmi.Naming.rebind("ServerObject", Servidor);
            System.out.println("Servidor pronto para receber chamadas remotas.");
            h.run();


        } catch (RemoteException re) {
            System.out.println("Exception in HelloImpl.main: " + re);
        } catch (MalformedURLException | NotBoundException e) {
            throw new RuntimeException(e);
        }
    }


}


