package pt.uc.sd;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.HashMap;

public interface IServerRemoteInterface extends Remote {
    int registerClient(IBarrelRemoteInterface client, int id) throws RemoteException, SQLException;
    void unregisterBarrel(int posicao) throws RemoteException;

    HashMap<Integer,String> PedidoHash(int a, int id) throws RemoteException, SQLException;

}