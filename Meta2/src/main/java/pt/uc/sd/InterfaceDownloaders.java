package pt.uc.sd;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface InterfaceDownloaders extends Remote {
    boolean Connected() throws RemoteException;

}