package Message;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServerRemoteInterface extends Remote {
    int registerClient(IClientRemoteInterface client, int id) throws RemoteException;
    void unregisterBarrel(int posicao) throws RemoteException;

}