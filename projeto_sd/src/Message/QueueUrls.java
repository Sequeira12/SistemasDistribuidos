package Message;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public class QueueUrls {

    public static Queue<String> Urls_To_Downloaders = new ConcurrentLinkedQueue<>();

    public synchronized void retira(){

    }
    public synchronized void coloca(){

    }
    public static void main(String[] args) {

        Urls_To_Downloaders.add("Ola");
        Urls_To_Downloaders.add("A ");
        Urls_To_Downloaders.add("TUA");
        Urls_To_Downloaders.add("MAE");


        Iterator<String> iterator = Urls_To_Downloaders.iterator();
        while(iterator.hasNext()){
            String element = (String) iterator.next();
            System.out.print(element + ' ' + "\n"); }
    }
}
