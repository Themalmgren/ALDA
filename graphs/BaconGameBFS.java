package alda.graphs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

import alda.graphs.BaconReader.Part;
import alda.graphs.BaconReader.PartType;

/**
 * https://en.wikipedia.org/wiki/Adjacency_list
 * 
 * @author Johan
 *
 */
public class BaconGameBFS {
	// läs in från fil
	// HashMap med film som nyckel och en ArrayList med skådespelare som värde
	// Iterera över HashMapen. Skapa ett HashSet med skådespelare som nyckel
	// associationer till andra skådespelare som värden (dubbletter ignoreras)
	//

	Map<String, Set<String>> actorCoactors = new HashMap<>();

	/**
	 * 
	 */
	private void run() {
		long start = System.currentTimeMillis();
		BaconReader reader;
		Map<String, Set<String>> filmActors = new HashMap<>();

		try {
			// actors_12.list
			reader = new BaconReader("actors_6.list");
			readFile(reader, filmActors);
			System.out.println("Namnen inlästa");

			// reader = new BaconReader("actresses.list");
			// readFile(reader, filmPersoner);
			// for (Entry<String, Set<String>> me : filmPersoner.entrySet()) {
			// System.out.println(me.getKey());
			// System.out.println(me.getValue());
			// System.out.println("------");
			// }

			System.out.println("Read time: " + (System.currentTimeMillis() - start));

			start = System.currentTimeMillis();
			createGraph(filmActors);
			System.out.println("Graph time: " + (System.currentTimeMillis() - start));
			System.out.println();

			// for (Entry<String, Set<String>> me : personPersoner.entrySet()) {
			// System.out.println(me.getKey());
			// System.out.println(me.getValue());
			// System.out.println("------");
			// }

			reader.close();
		} catch (FileNotFoundException e) {
			// TODO
			e.printStackTrace();
		} catch (IOException e) {
			// TODO
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param reader
	 * @param filmActors
	 * @throws IOException
	 */
	private void readFile(BaconReader reader, Map<String, Set<String>> filmActors) throws IOException {
		Part nextPart = reader.getNextPart();

		while (nextPart != null && nextPart.type == PartType.NAME) {
			String name = nextPart.text;
			nextPart = reader.getNextPart();

			// System.out.println(name);

			while (nextPart != null && nextPart.type != PartType.NAME) {
				if (nextPart.type == PartType.TITLE) {
					// titel + år
					String filmTitle = nextPart.text + reader.getNextPart().text;
					// skådisar som inte varit med i någon film läggs inte
					// till i hashmapen
					Set<String> actors = filmActors.get(filmTitle);
					if (actors == null) {
						actors = new HashSet<>();
						filmActors.put(filmTitle, actors);
					}
					actors.add(name);
				}
				nextPart = reader.getNextPart();
			}
		}
	}

	/**
	 * 
	 * @param filmActors
	 */
	private void createGraph(Map<String, Set<String>> filmActors) {
		for (Entry<String, Set<String>> entry : filmActors.entrySet()) {
			Set<String> actors = entry.getValue(); // alla actors för en film
			for (String actor : actors) {
				Set<String> coactors = actorCoactors.get(actor);
				if (coactors == null) { // händer alltid första gången för varje
										// gången
					coactors = new HashSet<>();
					actorCoactors.put(actor, coactors);
				}
				coactors.addAll(actors);
				coactors.remove(actor);
			}
		}
	}

	/**
	 * 
	 * @param fromName
	 * @param toName
	 * @return
	 */
	private int breadthFirst(String fromName, String toName) {
		if (fromName.equals(toName)) {
			return 0;
		}
		int baconNumber = 1;
		Set<String> visited = new HashSet<>();
		Set<String> coactors;
		Set<String> coCoactors = new HashSet<>();
		Queue<String> queue = new LinkedList<>();

//		if (coactors == null || coactors.isEmpty()) {
//			// fromName har inga direkta kopplingar
//			return -1;
//		}

		queue.add(fromName);

		while (!queue.isEmpty()) {
			String actor = queue.remove();
			visited.add(actor);
			coactors = actorCoactors.get(actor);
			int timesToRunBeforeIncBaconNumber = coactors.size();
//			queue.addAll(coactors);

			for (String coactor : coactors) {
				if (coactor.equals(toName)) {
					return baconNumber;
				}
				if (!visited.contains(coactor)) {
					queue.add(coactor);
					coCoactors = actorCoactors.get(coactor);
					for (String cocoactor : coCoactors) {
						
					}
					 //queue.addAll(coCoactors);
					//visited.add(person);
				}
			}
			
			baconNumber++;
		}

		return baconNumber;

	}

	public static void main(String[] args) {

		BaconGameBFS bg = new BaconGameBFS();
		bg.run();

		Scanner input = new Scanner(System.in);

		while (true) {
			System.out.print("Från: ");
			String from = input.nextLine();
			String to = "Bacon, Kevin (I)";

			long start = System.currentTimeMillis();
			System.out.println(bg.breadthFirst(from, to));
			System.out.println("Time to search: " + (System.currentTimeMillis() - start));
		}
	}

}
