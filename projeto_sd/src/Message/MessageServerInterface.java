package Message;

import java.rmi.*;
import java.sql.SQLException;
import java.util.ArrayList;

public interface MessageServerInterface extends Remote, IServerRemoteInterface {


	String SendUrlQueue(String token) throws RemoteException;

	ArrayList<String> FindUrlWithToken(String url) throws RemoteException, SQLException;
	String sayHello() throws RemoteException;

	void SendUrltoQueue(String url) throws RemoteException;


}