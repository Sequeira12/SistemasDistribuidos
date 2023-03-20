package Message;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IQueueRemoteInterface extends Remote {
    String retira() throws RemoteException;
    void coloca(String e) throws RemoteException;

    boolean ConnectDownload(IQueueRemoteInterface iq,int porta) throws RemoteException;
}