package Message;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface InterfaceClienteServer extends Remote {
    void atualizaStatus(ArrayList<IClientRemoteInterface> b, ArrayList<InterfaceDownloaders> d) throws RemoteException;

    boolean Connected() throws RemoteException;

}