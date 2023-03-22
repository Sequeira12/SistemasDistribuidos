package Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IQueueRemoteInterface extends Remote {
    String retira() throws RemoteException;

    void coloca(String e) throws RemoteException;

    void unregisterDownloader(int posicao) throws RemoteException;


    boolean ConnectDownload(InterfaceDownloaders iq, int porta) throws RemoteException;

    ArrayList<InterfaceDownloaders> info() throws RemoteException;
}