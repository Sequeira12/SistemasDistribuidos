package Message;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

public class Barrels extends UnicastRemoteObject implements IClientRemoteInterface {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	private IServerRemoteInterface server;
	HashMap<String, ArrayList<Element>> urls_ligacoes = new HashMap<String, ArrayList<Element>>();
	HashMap<String, ArrayList<String>> tokens_url = new HashMap<String, ArrayList<String>>();

	public Barrels() throws RemoteException {
		super();
	}
	public void connectToServer(IClientRemoteInterface client) {
		try {
			server = (IServerRemoteInterface) Naming.lookup("ServerObject");

			server.registerClient(client);
			System.out.println("Conexão com o servidor estabelecida.");
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public String TokenUrlBarrel(String valor){
		ArrayList<String> a = tokens_url.get(valor);


		System.out.printf("O resultado dos tokens: %s\n",a.get(0));
		return a.get(0);
	}

	public void InsereUrl(String url){
		System.out.println("HEYYY\n");
		try {
			Document doc = Jsoup.connect(url).get();
			StringTokenizer tokens = new StringTokenizer(doc.text());
			int conta = 0;
			while (tokens.hasMoreElements() && ++conta<20) {
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

	public void disconnectFromServer() {
		try {
			if (server != null) {
				server.unregisterClient(this);
				System.out.println("Desconectado do servidor.");
			}
		} catch (Exception e) {
			System.out.println("Erro: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void clientMethod() throws RemoteException {
		System.out.println("Método do cliente chamado pelo servidor.");
	}

	public void clientMethodWithArgument(String argument) throws RemoteException {
		System.out.println("Método do cliente chamado pelo servidor com argumento: " + argument);
	}


	// =========================================================
	public static void main(String args[]) {

		try {
			Barrels clientObj = new Barrels();
			clientObj.connectToServer(clientObj);
			//clientObj.InsereUrl("http://www.uc.pt");
		//	String a = clientObj.TokenUrlBarrel("coimbra"); FUNCIONA COM ESTA MERDA(MAS NAO PODE)

			//System.out.println(a);

		} catch (RemoteException re) {
			System.out.println("Exception in HelloImpl.main: " + re);
		}
	}

}