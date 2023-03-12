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
			System.out.println(h.sayHello());
			String url = "http://www.uc.pt";

			Scanner scanner = new Scanner(System.in);
			System.out.println("Digite um número:");



			while(scanner.hasNextInt()){

				int numero = scanner.nextInt();


				if (numero == 1) {
					System.out.println("insira a sua procura:");
					Scanner scanner2 = new Scanner(System.in);
					String palavra = scanner2.nextLine();

					ArrayList<String> linksAssociados = h.FindUrlWithToken(palavra);
					System.out.println("Links Associados à sua procura:");
					if(linksAssociados == null){
						System.out.println("Sem resultados encontrados!!");
					}else {
						for (int i = 0; i < Objects.requireNonNull(linksAssociados).size(); i++) {
							System.out.printf("- %s\n", linksAssociados.get(i));
						}
					}
				/*	else if(numero == 2){
				tring Coimbra = "Coimbra";
					String mensagem = h.SendUrlQueue(Coimbra);
					System.out.println("HelloClient: " + mensagem);
				*/
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



