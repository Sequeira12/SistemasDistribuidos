package Message;

import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class MessageServerInterfaceClient {


	public static void Imprime10_10(ArrayList<String> links){
		int conta = 0;
		for(int i = 0; i < 10 && i < links.size(); i++){
			System.out.printf(" - %s\n",links.get(i));
		}
		System.out.println("Digite '+' se quer ir para a seguinte\nDigite '-' se deseja ir para a anterior\nDigite 0 se quer terminar a pesquisa");
		Scanner client = new Scanner(System.in);

		while(client.hasNextLine()){
			String s = client.nextLine();
			if(s.compareTo("+") == 0){
				conta++;
			}else if(s.compareTo("-") == 0){
				conta--;
			}else if(s.compareTo("0") == 0){
				break;
			}
			if(conta < 0){
				conta = 0;
			}
			for(int i = 10 * conta; i < 10*(conta+1) && i < links.size(); i++){
				System.out.printf(" - %s\n",links.get(i));
			}
			System.out.println("Digite '+' se quer ir para a seguinte\nDigite '-' se deseja ir para a anterior\nDigite 0 se quer terminar a pesquisa\n");

		}




	}

	public static void main(String args[]) {

		/* This might be necessary if you ever need to download classes:
		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());
		*/

		try {

			MessageServerInterface h = (MessageServerInterface) LocateRegistry.getRegistry(7001).lookup("SD");


			String url = "http://www.uc.pt";

			Scanner scanner = new Scanner(System.in);
			System.out.println("Digite um número:");


			System.out.println(h.sayHello());
			while(scanner.hasNextInt()){

				int numero = scanner.nextInt();


				if (numero == 1) {
					System.out.println("Coloque o token:");
					Scanner scanner2 = new Scanner(System.in);
					String palavra = scanner2.nextLine();

					ArrayList<String> linksAssociados = h.FindUrlWithToken(palavra);
					System.out.println("Links Associados à sua procura:");
					if(linksAssociados == null ){
						System.out.println("Sem resultados encontrados!!");
					}else {
						if( linksAssociados.get(0).compareTo("Sem Resultados") == 0){
							System.out.println("Barrels Indisponiveis!!");
						}else {
							Imprime10_10(linksAssociados);

						}
					}
				}else if(numero == 2){
					System.out.println("COLOQUE O SITE:");
					Scanner scanner2 = new Scanner(System.in);
					String palavra = scanner2.nextLine();
					h.SendUrltoQueue(palavra);


				}else{
					break;
				}
				System.out.println(h.sayHello());
				System.out.println("\nDigite um número:");
			}
		} catch (Exception e) {
			System.out.println("Exception in main: " + e);
			e.printStackTrace();
		}

	}

}



