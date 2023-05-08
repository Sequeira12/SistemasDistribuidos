package pt.uc.sd.meta1files;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface InterfaceClienteServer extends Remote {
    void atualizaStatus(ArrayList<IBarrelRemoteInterface> b, ArrayList<InterfaceDownloaders> d) throws RemoteException;

    boolean Connected() throws RemoteException;

}