package Message;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface IClientRemoteInterface extends Remote {

    ArrayList<String> ProcuraToken(String url) throws RemoteException, SQLException;
     boolean Connected() throws RemoteException;

     Connection conetor() throws RemoteException;

}