package hr.fer.zemris.trisat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Parsira ulaznu datoteku.
 * @author filip
 *
 */
public class Parser {
	
	/**
	 * Broj vraijabli u formuli.
	 */
	private int numberOfVariables;
	
	/**
	 * Broj klauzula u formuli.
	 */
	private int numberOfClauses;
	
	/**
	 * Klauzule formule.
	 */
	private Clause[] clauses;
	
	/**
	 * Ulazna datoteka.
	 */
	private File file;
	
	/**
	 * Stvara novi parser i parsira ulaznu datoteku.
	 * @param path Putanja do datoteke
	 */
	public Parser(String path){
		file = new File(path);
		try {
			parse();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Parsira ulaznu datoteku.
	 * @throws IOException
	 */
	private  void parse() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		String line;
		int currentClause = 0;
		while((line = br.readLine()) != null) {
			if(line.startsWith("c")) { continue; }
			if(line.startsWith("%")) { break; }
			if(line.startsWith("p")) {
				String[] split = line.split("\\s+");
				numberOfVariables = Integer.parseInt(split[2]);
				numberOfClauses = Integer.parseInt(split[3]);
				clauses = new Clause[numberOfClauses];
			} else {
				String[] stringIndexes = line.trim().split("\\s+");
				int[] indexes = new int[stringIndexes.length - 1];
				for(int i = 0; i < stringIndexes.length - 1; i++) {
					indexes[i] = Integer.parseInt(stringIndexes[i]);
				}
				clauses[currentClause] = new Clause(indexes);
				currentClause++;
			}		
		}
		br.close();
	}

	/**
	 * @return Broj vraijabli formule.
	 */
	public int getNumberOfVariables() {
		return numberOfVariables;
	}
	
	/**
	 * @return Broj klauzula formule.
	 */
	public int getNumberOfClauses() {
		return numberOfClauses;
	}
	
	/**
	 * @return Klauzule formule.
	 */
	public Clause[] getClauses() {
		return clauses;
	}
}
