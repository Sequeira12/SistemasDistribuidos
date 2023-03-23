package Message;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;

public interface IServerRemoteInterface extends Remote {
    int registerClient(IClientRemoteInterface client, int id) throws RemoteException, SQLException;
    void unregisterBarrel(int posicao) throws RemoteException;

}