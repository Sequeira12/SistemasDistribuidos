package Message;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;

public interface InterfaceClienteServer extends Remote {
    void atualizaStatus(ArrayList<IClientRemoteInterface> b,ArrayList<Integer> d) throws RemoteException;

    boolean Connected() throws RemoteException;

}