package Message;

import java.rmi.registry.LocateRegistry;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class MessageServerInterfaceClient {

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
							for (int i = 0; i < Objects.requireNonNull(linksAssociados).size(); i++) {
								System.out.printf("- %s\n", linksAssociados.get(i));
							}
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
				System.out.println("\nDigite um número:");
			}
		} catch (Exception e) {
			System.out.println("Exception in main: " + e);
			e.printStackTrace();
		}

	}

}



