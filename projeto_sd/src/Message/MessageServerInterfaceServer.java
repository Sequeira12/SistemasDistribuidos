package Message;

import java.lang.reflect.Array;
import java.rmi.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.*;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class MessageServerInterfaceServer extends UnicastRemoteObject implements MessageServerInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	HashMap<String, ArrayList<Element>> urls_ligacoes = new HashMap<String, ArrayList<Element>>();
	HashMap<String,String> tokens_url = new HashMap<String, String>();
	public MessageServerInterfaceServer() throws RemoteException {
		super();
	}
	public String TokenUrl(String token) throws RemoteException{
		String a = tokens_url.get("coimbra");
		System.out.printf("O resultado dos tokens: %s\n",a);
		return a;
	}
	public void SendInfo(String url) throws RemoteException {
		try {
			Document doc = Jsoup.connect(url).get();
			StringTokenizer tokens = new StringTokenizer(doc.text());
			int countTokens = 0;
			while (tokens.hasMoreElements() && countTokens++ < 100) {
				System.out.println(tokens.nextToken().toLowerCase());
				tokens_url.put(tokens.nextToken().toLowerCase(),url);

			}
			Elements links = doc.select("a[href]");
			for (Element link : links) {
				System.out.println(link.text() + "\n" + link.attr("abs:href") + "\n");
				if (!urls_ligacoes.containsKey(url)) {
					ArrayList<Element> nova = new ArrayList<>();
					urls_ligacoes.put(url, nova);
				}else {
					urls_ligacoes.get(url).add(link);

				}
			}
			ArrayList<Element> fim = urls_ligacoes.get(url);
			System.out.println("RESULTADO\n");
			for(Element link: fim){
				System.out.println(link.attr("abs:href"));
			}


		} catch (IOException e) {
			e.printStackTrace();
		}






	}

	public String sayHello() throws RemoteException {

		return "Bem-vindo\nEscolha as opções:\n1-Url para indexar\n2-token para procurar\n0-exit\nObrigado!!\n";
	}

	// =========================================================
	public static void main(String args[]) {

		try {
			MessageServerInterfaceServer h = new MessageServerInterfaceServer();
			Registry r = LocateRegistry.createRegistry(7001);
			r.rebind("SD", h);
			System.out.println("Hello Server ready.");

		} catch (RemoteException re) {
			System.out.println("Exception in HelloImpl.main: " + re);
		}
	}

}