package Message;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.net.MalformedURLException;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

public class QueueUrls extends UnicastRemoteObject implements IQueueRemoteInterface{

    public static Queue<String> Urls_To_Downloaders = new ConcurrentLinkedQueue<>();

    public QueueUrls() throws RemoteException{
        super();
    }

    public synchronized String retira(){
        String ret = null;
        if(!Urls_To_Downloaders.isEmpty()){
            ret = Urls_To_Downloaders.peek();
            Urls_To_Downloaders.remove();
        }
        return ret;
    }
    public synchronized void coloca(String e){
        Urls_To_Downloaders.add(e);
    }
    public static void main(String[] args) throws RemoteException {

        //Urls_To_Downloaders.add("https://www.uc.pt");

        QueueUrls h = new QueueUrls();
        Registry r = LocateRegistry.createRegistry(7003);
        r.rebind("QD", h);

    }
}
