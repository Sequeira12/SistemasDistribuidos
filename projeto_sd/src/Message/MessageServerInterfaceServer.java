package Message;

import java.lang.reflect.Array;
import java.net.MalformedURLException;
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

public class MessageServerInterfaceServer extends UnicastRemoteObject implements MessageServerInterface,IServerRemoteInterface {

	public static IClientRemoteInterface clientOn;
	public static IServerRemoteInterface Servidor;



	public MessageServerInterfaceServer() throws RemoteException {
		super();
	}


	public void registerClient(IClientRemoteInterface client) throws RemoteException {
		this.clientOn = client;
		System.out.println("Barrel registrado no servidor.");


	}

	public void unregisterClient(IClientRemoteInterface client) throws RemoteException {
		this.clientOn = null;
		System.out.println("Barrel removido do servidor.");
	}


	public ArrayList<String> FindUrlWithToken(String url) throws RemoteException  {

		ArrayList<String> connectados = null;
		if(this.clientOn != null) {
			connectados = clientOn.InsereUrl(url);

		}
		return connectados;
	}

	public String sayHello() throws RemoteException {

		return "Bem-vindo\nEscolha as opções:\n1-Url para indexar\n2-token para procurar\n0-exit\nObrigado!!\n";
	}

	public String SendUrlQueue(String token) throws RemoteException {

		return  null;
		//return mensagem;
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

			MessageServerInterfaceServer h = new MessageServerInterfaceServer();
			Registry r = LocateRegistry.createRegistry(7001);
			r.rebind("SD", h);




			Servidor =  new MessageServerInterfaceServer();
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			java.rmi.Naming.rebind("ServerObject", Servidor);
			System.out.println("Servidor pronto para receber chamadas remotas.");


		} catch (RemoteException re) {
			System.out.println("Exception in HelloImpl.main: " + re);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}






}


