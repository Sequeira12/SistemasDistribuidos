package Message;

import java.rmi.*;

public interface MessageServerInterface extends Remote, IServerRemoteInterface {
	String sayHello() throws RemoteException;

	String TokenUrl(String token) throws RemoteException;

	void SendInfo(String url) throws RemoteException;

}