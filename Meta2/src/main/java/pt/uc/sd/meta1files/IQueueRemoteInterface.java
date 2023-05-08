package pt.uc.sd.meta1files;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IQueueRemoteInterface extends Remote {
    String retira() throws RemoteException;

    void coloca(String e, int i) throws RemoteException, SQLException;

    void unregisterDownloader(int posicao) throws RemoteException;


    boolean ConnectDownload(InterfaceDownloaders iq, int porta) throws RemoteException;

    void atualizaNumeroBarrels(int n) throws RemoteException;

    int giveNumeroBarrels() throws RemoteException;

    ArrayList<InterfaceDownloaders> info() throws RemoteException;
}