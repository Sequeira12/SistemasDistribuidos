package Message;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public interface IBarrelRemoteInterface extends Remote {

    String listPage(String url) throws RemoteException, SQLException;
    ArrayList<String> ProcuraToken(String url, int logado) throws RemoteException, SQLException;

    boolean Connected() throws RemoteException;
    HashMap<Integer,String> sendHash(int a) throws RemoteException;

    Connection conetor() throws RemoteException;


}