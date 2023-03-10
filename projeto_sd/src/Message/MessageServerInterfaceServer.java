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

public class MessageServerInterfaceServer extends UnicastRemoteObject implements MessageServerInterface,MessageBarrelsInterface {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	HashMap<String, ArrayList<Element>> urls_ligacoes = new HashMap<String, ArrayList<Element>>();
	HashMap<String,ArrayList<String>> tokens_url = new HashMap<String, ArrayList<String>>();


	public MessageServerInterfaceServer() throws RemoteException {
		super();
	}


	public String TokenUrl(String token) throws RemoteException{
		ArrayList<String> a = tokens_url.get("coimbra");


		System.out.printf("O resultado dos tokens: %s\n",a.get(0));
		return a.get(1);
	}
	public void SendInfo(String url) throws RemoteException {
		try {
			Document doc = Jsoup.connect(url).get();
			StringTokenizer tokens = new StringTokenizer(doc.text());

			while (tokens.hasMoreElements() ) {
				System.out.println(tokens.nextToken().toLowerCase());
				String palavra = tokens.nextToken().toLowerCase();
				if(!tokens_url.containsKey(palavra)){

					int a = palavra.compareTo("coimbra");
					if(a > 0){
						System.out.print("YHHHH\n");
					}
					ArrayList<String> urls = new ArrayList<>();
					urls.add(url);
					tokens_url.put(palavra,urls);
				}else{
					tokens_url.get(palavra).add(url);
				}

			}
			Elements links = doc.select("a[href]");
			for (Element link : links) {
				System.out.println(link.text() + "\n" + link.attr("abs:href") + "\n");
				if (!urls_ligacoes.containsKey(url)) {
					ArrayList<Element> nova = new ArrayList<>();
					nova.add(link);
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
			/**
			 * Fazer por multicast a ligacao do Barrels com o Search Module
			 * ter uma array com os (ips portos) dos barrels que estão conectados
			 *
			 * Fazer funcao aqui para que o barrels invoce para adicionar a porta dele a um arraylist de porta globais do servidor
			 *
			 */
			MessageBarrelsInterface a = new MessageServerInterfaceServer() ;
			MessageServerInterfaceServer h = new MessageServerInterfaceServer();
			Registry r = LocateRegistry.createRegistry(7001);
			//System.out.println(RemoteServer.getClientHost());

			r.rebind("SD", h);



		} catch (RemoteException re) {
			System.out.println("Exception in HelloImpl.main: " + re);
		}
	}


}


