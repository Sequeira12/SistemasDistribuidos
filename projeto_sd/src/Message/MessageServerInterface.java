package Message;

import java.rmi.*;
import java.sql.SQLException;
import java.util.ArrayList;

public interface MessageServerInterface extends Remote, IServerRemoteInterface {


    String SendUrlQueue(String token) throws RemoteException;

    ArrayList<String> FindUrlWithToken(String url) throws RemoteException, SQLException;

    String sayHello(int login) throws RemoteException;

    void SendUrltoQueue(String url) throws RemoteException;

    void addClient(InterfaceClienteServer a) throws RemoteException;
    boolean Register(String username, String password) throws RemoteException, SQLException;

    boolean Login(String username, String password) throws RemoteException, SQLException;

    ArrayList<String> listPagesConnectedtoAnotherPage(String url) throws SQLException, RemoteException;


}