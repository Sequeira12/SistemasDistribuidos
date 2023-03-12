package Message;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IClientRemoteInterface extends Remote {
    void clientMethod() throws RemoteException;
    void clientMethodWithArgument(String argument) throws RemoteException;

    void InsereUrl(String url) throws RemoteException;

}