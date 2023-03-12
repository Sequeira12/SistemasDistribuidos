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

	public IClientRemoteInterface clientOn;



	public MessageServerInterfaceServer() throws RemoteException {
		super();
	}


	public void registerClient(IClientRemoteInterface client) throws RemoteException {
		this.clientOn = client;

		System.out.println("Barrel registrado no servidor.");


	}

	public void unregisterClient(IClientRemoteInterface client) throws RemoteException {

		System.out.println("Barrel removido do servidor.");
	}

	public void serverMethod() throws RemoteException {
		System.out.println("Método do servidor chamado.");
		if (this.clientOn != null) {
			this.clientOn.clientMethod();
		}
	}


	public void callClientMethod(String argument) throws RemoteException {
		System.out.println("Método do servidor chamando método do Barrel com argumento: " + argument);
		if (this.clientOn  != null) {
			this.clientOn.clientMethodWithArgument(argument);
		}
	}



	public void SendInfo(String url) throws RemoteException  {

		System.out.printf("SEND Info %s\n",url);
		if(this.clientOn != null) {
			System.out.println("HHAHA\n");
			System.out.printf("%s\n",url);
			clientOn.InsereUrl(url);
		}else{
			System.out.println("NULL\n");
		}
	}
	public String sayHello() throws RemoteException {
		if(clientOn != null){

			System.out.println("AHHAHAAH\n");
		}
		return null;
	}


	public String TokenUrl(String token) throws RemoteException {
		Barrels novo = new Barrels();
		if(novo == null){
			System.out.println("OKKKK\n");
		}
		return null;
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

/*

			MessageBarrelsInterface g = new MessageServerInterfaceServer();
			Registry b = LocateRegistry.createRegistry(7002);
			b.rebind("SB",g);

			ClientObjetoRemoto client = null;

			while( client == null) {
				try {
					client = (ClientObjetoRemoto) Naming.lookup("SB");
					System.out.println();
				} catch (Exception e) {
					System.out.println("Waiting for client to connect...");

				}
			}

			System.out.println("OAOAOAO\n");

*/
			IServerRemoteInterface serverObj =  new MessageServerInterfaceServer();
			java.rmi.registry.LocateRegistry.createRegistry(1099);
			java.rmi.Naming.rebind("ServerObject", serverObj);
			System.out.println("Servidor pronto para receber chamadas remotas.");


		} catch (RemoteException re) {
			System.out.println("Exception in HelloImpl.main: " + re);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}






}


