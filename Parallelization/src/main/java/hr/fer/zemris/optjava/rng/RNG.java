package hr.fer.zemris.optjava.rng;

import java.io.IOException;
import java.util.Properties;

public class RNG {
	
	private static IRNGProvider rngProvider;
	
	static {
		Properties properties = new Properties();
		try {
			properties.load(RNG.class.getClassLoader().getResourceAsStream("rng-config.properties"));
			String providerClass = properties.getProperty("rng-provider");
			rngProvider = (IRNGProvider) RNG.class.getClassLoader().loadClass(providerClass).newInstance();
		} catch (IOException e) {
			System.out.println("Properties datoteka se ne može otvoiti!");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			System.out.println("Klasa definirana u rng-config.properties nije dobro zadana!");
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		//Stvorite primjerak razreda Properties;
		//Nad Classloaderom razreda RNG tražite InputStream prema resursu rng-config.properties
		//recite stvorenom objektu razreda Properties da se učita podatcima iz tog streama.
		//Dohvatite ime razreda pridruženo ključu "rng-provider"; zatražite Classloader razreda
		//RNG da učita razred takvog imena i nad dobivenim razredom pozovite metodu newInstance()
		//kako biste dobili jedan primjerak tog razreda; castajte ga u IRNGProvider i zapamtite.
	}	
	
	public static IRNG getRNG() {
		return rngProvider.getRNG();
	}
}
