package Message;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface InterfaceDownloaders extends Remote {
    boolean Connected() throws RemoteException;

}