package alda.graphs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;

import alda.graphs.BaconReader.Part;
import alda.graphs.BaconReader.PartType;

/**
 * BaconGame är ett program som frågar efter namnet på en skådespelare 
 * och skriver ut dennes Baconnummer. En skådespelares Baconnummer 
 * räknas ut genom att räkna det minsta antalet länkar av delade 
 * filmroller mellan skådespelaren i fråga och Kevin Bacon.
 * 
 * Exempelvis har Tom Hanks ett Baconnummer på 1 då han spelade
 * i filmen Apollo 13 tillsammans med Kevin Bacon. Sally Field har
 * ett Baconnummer på 2 då hon var med i Forrest Gump med Tom Hanks,
 * som i sin tur alltså var med i Apollo 13 med Kevin Bacon.
 * 
 * För att läsa in skådespelare och skådespelerskor från varsin fil
 * och skapa en graf med relationerna de har sinsemellan används
 * metoden {@link #buildActorRelations()}.
 * 
 * För att ta reda på Bacon-numret från en skådespelare till
 * Kevin Bacon används metoden {@link #breadthFirst(String, String)}.
 * 
 * @author Johan Ekh, joek7107
 * @author Alexanderi Malmgren, alma1060
 * @see #buildActorRelations()
 * @see #breadthFirst(String, String)
 * @see <a href="http://oracleofbacon.org/">http://oracleofbacon.org/</a>
 */
public class BaconGame {
	Map<String, Set<String>> actorCoactors = new HashMap<>();
	Map<String, Set<String>> filmActors = new HashMap<>();

	/**
	 * Skapar en ny instans av BaconReader vilken används för att
	 * läsa in actors.list respektive actresses.list. Kallar för varje fil
	 * sedan på metoden {@link #buildFilmActorHashMap(reader)} som bygger
	 * upp en HashMap med filmtitel som nyckel och ett set med
	 * skådespelare som värde.
	 * 
	 * Därefter anropas metoden {@link #buildActorCoactorGraph()}. 
	 * 
	 * @see BaconReader
	 * @see #buildFilmActorHashMap(reader)
	 * @see #buildActorCoactorGraph()
	 */
	private void buildActorRelations() {
		BaconReader reader;
		try {
			reader = new BaconReader("actors_6.list");
			buildFilmActorHashMap(reader);
			reader = new BaconReader("actresses_6.list");
			buildFilmActorHashMap(reader);
			reader.close();
			buildActorCoactorGraph();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	/**
	 * Hämtar nästa del (namn på skådespelare, filmtitel, år, id eller info) 
	 * av filen med hjälp av metoden getNextPart() från den medskickade
	 * instansen av {@link #BaconReader}. 
	 * 
	 * Metoden befolkar sedan HashMapen filmActors med filmtitel som nycklar
	 * och HashSet av skådespelare som värden. 
	 * 
	 * @param reader		instans av läsaren BaconReader 
	 * @throws IOException	signalerar att någon form av I/O exception har
	 * 						inträffat
	 * @see BaconReader
	 */
	private void buildFilmActorHashMap(BaconReader reader) throws IOException {
		Part nextPart = reader.getNextPart();
		// första raden i filen är alltid ett namn
		while (nextPart != null) {
			String actor = nextPart.text;
			nextPart = reader.getNextPart();
			// nästa skådespelare börjar med ett nytt namn
			while (nextPart != null && nextPart.type != PartType.NAME) {
				if (nextPart.type == PartType.TITLE) {
					// titel + år
					String film = nextPart.text + reader.getNextPart().text;
					// skådespelare som inte varit med i någon film 
					// läggs inte till i hashmapen
					Set<String> actors = filmActors.get(film);
					if (actors == null) {
						actors = new HashSet<>();
						filmActors.put(film, actors);
					}
					actors.add(actor);
				}
				nextPart = reader.getNextPart();
			}
		}
	}
	
	/**
	 * Använder filmActors för att bygga en en adjecency list actorCoactors 
	 * med skådespelare som nyckel och ett HashSet med dess medskådespelare
	 * som värde.
	 */
	private void buildActorCoactorGraph() {
		for (Entry<String, Set<String>> entry : filmActors.entrySet()) {
			// alla actors i en och samma film
			Set<String> actors = entry.getValue();
			// för varje skådespelare i filmen, lägg till alla andra
			// som dess medskådespelare
			for (String actor : actors) {
				Set<String> coactors = actorCoactors.get(actor);
				// händer alltid första gången för varje actor
				if (coactors == null) {
					coactors = new HashSet<>();
					actorCoactors.put(actor, coactors);
				}
				coactors.addAll(actors);
				coactors.remove(actor);
			}
		}
	}
	
	/**
	 * Gör en bredden först-sökning i grafen actorCoactors och returnerar
	 * det minsta antalet länkar av delade filmroller mellan skådespelaren 
	 * fromName och toName (Kevin Bacon).
	 * 
	 * @param fromName	skådespelare att söka från
	 * @param toName	skådespelare att söka till (Kevin Bacon)
	 * @return			returnerar skådespelarens Bacon-nummer.
	 * 					Har skådespelaren fromName inga relationer
	 * 					över huvud taget returnas -1.
	 * 					Hittas ingen koppling mellan skådespelarna
	 * 					returneras -2.
	 * @see BaconGame#actorCoactors
	 */
	private int breadthFirst(String fromName, String toName) {
		if (fromName.equals(toName)) {
			return 0;
		}
		int baconNumber = 1;
		Set<String> visited = new HashSet<>();
		Set<String> coactors = actorCoactors.get(fromName);

		if (coactors == null || coactors.isEmpty()) {
			// skådespelaren har inga relationer över huvud taget
			return -1;
		}

		visited.add(fromName);

		// kollar för varje nivå på bredden om någon av skådespelarna
		// är Kevin Bacon. 
		while (true) {
			if (coactors.contains(toName)) {
				return baconNumber;
			}
			// Kevin Bacon fanns inte bland coactors
			visited.addAll(coactors);
			
			// går därför vidare till nästa nivå
			baconNumber++;
			Set<String> coCoactors = new HashSet<>();
			// lägger till alla coactors medskådespelare (nästa nivå) till coCoactors
			for (String actor : coactors) {
				coCoactors.addAll(actorCoactors.get(actor));
			}
			// tar bort alla redan besökta skådespelare
			coCoactors.removeAll(visited);
			
			if (coCoactors.isEmpty()) {
				// skådespelaren har ingen koppling till Kevin Bacon
				return -2;
			}
			// obesökta coactors i nästa nivå
			coactors = coCoactors;
		}
		// kommer aldrig hit
	}

	public static void main(String[] args) {
		BaconGame bg = new BaconGame();
		bg.buildActorRelations();
		
		Scanner input = new Scanner(System.in);
		
		while (true) {
			System.out.print("Från: ");
			String from = input.nextLine();
			String to = "Bacon, Kevin (I)";

			System.out.println(bg.breadthFirst(from, to));
		}
	}
}