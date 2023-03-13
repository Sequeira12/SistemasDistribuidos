package Message;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface IClientRemoteInterface extends Remote {

    ArrayList<String> InsereUrl(String url) throws RemoteException;
     boolean Connected() throws RemoteException;

}