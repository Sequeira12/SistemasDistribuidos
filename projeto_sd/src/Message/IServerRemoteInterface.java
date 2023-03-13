package Message;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IServerRemoteInterface extends Remote {
    void registerClient(IClientRemoteInterface client) throws RemoteException;
    void unregisterClient(int posicao) throws RemoteException;

}