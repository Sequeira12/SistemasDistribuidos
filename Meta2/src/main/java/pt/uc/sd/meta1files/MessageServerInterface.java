package pt.uc.sd.meta1files;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public interface MessageServerInterface extends Remote, IServerRemoteInterface {

    void SendUrltoQueue(String url) throws RemoteException, SQLException;

    ArrayList<String> FindUrlWithToken(String url, int logado) throws RemoteException, SQLException;

    String sayHello(int login) throws RemoteException;
    void addClient(InterfaceClienteServer a) throws RemoteException;

    ArrayList<IBarrelRemoteInterface> getBarrels() throws RemoteException;
    ArrayList<InterfaceDownloaders> getDownloaders() throws RemoteException;
    boolean getUpdated() throws RemoteException;
    void setUpdated(boolean upd) throws RemoteException;
    boolean Register(String username, String password) throws RemoteException, SQLException;

    boolean Login(String username, String password) throws RemoteException, SQLException;

    String listPagesConnectedtoAnotherPage(String url) throws SQLException, RemoteException;

    String VerificaTop10() throws RemoteException,SQLException;


}