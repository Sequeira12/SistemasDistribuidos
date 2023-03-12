package Message;

import java.rmi.registry.LocateRegistry;
import java.util.Scanner;

public class MessageServerInterfaceClient {

	public static void main(String args[]) {

		/* This might be necessary if you ever need to download classes:
		System.getProperties().put("java.security.policy", "policy.all");
		System.setSecurityManager(new RMISecurityManager());
		*/

		try {

			MessageServerInterface h = (MessageServerInterface) LocateRegistry.getRegistry(7001).lookup("SD");

			String message = h.sayHello();

			System.out.println("HelloClient: " + message);
			String url = "http://www.uc.pt";

			Scanner scanner = new Scanner(System.in);
			System.out.println("Digite um número:");



			while(scanner.hasNextInt()){

				int numero = scanner.nextInt();


				if (numero == 1) {

					h.SendInfo("http://www.uc.pt");
				}else if(numero == 2){
					String Coimbra = "Coimbra";
					String mensagem = h.TokenUrl(Coimbra);
					System.out.println("HelloClient: " + mensagem);
				}else{
					break;
				}
				System.out.println("Digite um número:");
			}
		} catch (Exception e) {
			System.out.println("Exception in main: " + e);
			e.printStackTrace();
		}

	}

}