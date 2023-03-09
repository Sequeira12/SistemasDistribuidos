package Message;
import java.rmi.*;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessageBarrelsInterface extends Remote {
	String sayHello2() throws RemoteException;

	String TokenUrl2(String token) throws RemoteException;

	void SendInfo2(String url) throws RemoteException;
}