package Message;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface ISearcheQueue extends Remote {
    public void SendInfoDownloaders(ArrayList<Integer> Downloaders) throws RemoteException;

}