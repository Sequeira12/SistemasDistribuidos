package pt.uc.sd.meta1files;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceDownloaders extends Remote {
    boolean Connected() throws RemoteException;

}