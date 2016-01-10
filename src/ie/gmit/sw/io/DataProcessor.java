package ie.gmit.sw.io;

import java.util.*;

/**
 * 
 * Used for reading data from URLs, or Files locally. Sorts and parses the data
 * into lists and blocks unnecessary words. Sorts the data using the
 * ValueComparator class.
 * 
 */
public class DataProcessor {
	// readers
	private DataReader dataReader;
	private DataReaderFactory dataReaderFactory;
	private Stopwords sdr;
	private String stopWordsFileName = "stopwords.txt";

	// collections
	private Map<String, Integer> validWords = new HashMap<String, Integer>();

	// sorting
	private ValueComparator vc = new ValueComparator(validWords);
	private TreeMap<String, Integer> sortedWords = new TreeMap<String, Integer>(vc);

	public DataProcessor() {
		dataReaderFactory = DataReaderFactory.getInstance();
		sdr = new Stopwords(stopWordsFileName);
		sdr.addWords();
	}
	
	public TreeMap<String, Integer> getSortedWords() {
		return sortedWords;
	}

	public void clearValidWords() {
		validWords.clear();
	}

	/**
	 * Prints out the parsed words (with the unnecessary words removed).
	 */
	public void printValidWords() {
		System.out.println("words: " + validWords);
		System.out.println(validWords.size() + " unique words");
	}

	/**
	 * Prints out the valid words in sorted order.
	 */
	public void printSortedWords() {
		System.out.println("sorted words: " + sortedWords);
		System.out.println(sortedWords.size() + " unique words");
	}

	/**
	 * Reads in data from a file. Returns the result.
	 * 
	 * @param inputDataFileName
	 *            is the path to the local file.
	 */
	public void fileReader(String fileLocation) throws Exception {
		dataReader = dataReaderFactory.getReader("file", fileLocation);
		processReaderData(fileLocation);
	}

	/**
	 * Reads in data from a URL. Returns the result.
	 * 
	 * and parses it using a Third party library called JSOUP.
	 * 
	 * @param inputDataUrlname
	 *            is the URL to be retrieved.
	 * @exception Exception
	 *                on input error.
	 * @see Exception
	 */
	public void urlReader(String fileLocation) throws Exception {
		dataReader = dataReaderFactory.getReader("url", fileLocation);
		processReaderData(fileLocation);
	}
	
	private void processReaderData(String fileLocation) throws Exception {
		process(dataReader.getData());
		sortWords();
	}

	/**
	 * Removes punctuation and calls the add word method.
	 */
	public void process(String data) {
		// removes punctuation + to_lowercase
		String[] words = data.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");

		for (String word : words) {
			// System.out.println(word);
			if (word.length() > 1) {
				addWord(word);
			}
		}
	}

	/**
	 * Adds a word to the map or else increases its count. Checks words against
	 * the stop word list.
	 */
	public void addWord(String word) {
		if (!sdr.getStopWords().contains(word)) {

			if (validWords.containsKey(word)) {
				int count = validWords.get(word);
				count++;
				validWords.replace(word, count);
			} else {
				validWords.put(word, 1);
			}
		} else {
			sdr.setStopWordsFound(sdr.getStopWordsFound() + 1);
		}
	}

	/**
	 * Uses the ValueComparator class to sort the Map into a Treeset.
	 */
	public void sortWords() {
		sortedWords.clear();
		sortedWords.putAll(validWords);
	}

	public String getStopWordsFileName() {
		return stopWordsFileName;
	}

	public void setStopWordsFileName(String stopWordsFileName) {
		this.stopWordsFileName = stopWordsFileName;
	}
	
}
