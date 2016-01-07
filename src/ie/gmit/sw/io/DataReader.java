package ie.gmit.sw.io;

import java.io.*;
import java.net.*;
import java.util.*;

import org.jsoup.*;
import org.jsoup.nodes.*;

public class DataReader {
	private static Map<String, Integer> validWords = new HashMap<String, Integer>();
	private static TreeSet<String> stopWords = new TreeSet<String>();
	
	private static int stopWordsFound;

	private static String inputDataFileName;
	private static String stopWordsFileName = "stopwords.txt";
	
	// sorting
	private static ValueComparator vc = new ValueComparator(validWords);
	private static TreeMap<String, Integer> sortedWords = new TreeMap<String, Integer>(vc);
	
	public static TreeMap<String, Integer> getSortedWords() {
		return sortedWords;
	}

	public static void clearValidWords() {
		validWords.clear();
	}


	public DataReader() {
		//inputDataFileName = "SampleText.txt";
		stopwordsReader();
	}
	
	
	
//	public DataReader(String fileName) {
//		inputDataFileName = fileName;
//		stopwordsReader();
//	}
	
	public static void main(String[] args) throws Exception {
		stopWordsFound = 0;
		inputDataFileName = "SampleText.txt";
		String inputDataUrlname = "http://www.ronanconnolly.ie/";
		
		System.out.println("Reading in stopwords file.");
		stopwordsReader();
		
//		System.out.println("Reading in sample text file.");
//		fileReader(inputDataFileName);
		
		System.out.println("Reading in sample url.");
		urlReader(inputDataUrlname);

		System.out.println("\nprinting valid words");
		printValidWords();
		
		System.out.println("\nprinting sorted words");
		printSortedWords();
		
		System.out.println("\nfin.");
		
		// paint
		
		// read data from url
		
		// implement design patterns
		
	}
	
	public static void printValidWords() {
		System.out.println("words: " + validWords);
		System.out.println(validWords.size() + " unique words");
	}

	public static void printSortedWords() {
		System.out.println("sorted words: " + sortedWords);
		System.out.println(sortedWords.size() + " unique words");
	}
	
	private static void stopwordsReader() {
		// read stopwords in from file
		try{
			BufferedReader in = new BufferedReader(new FileReader(stopWordsFileName));
			String word;
			// one word per line
			while((word = in.readLine()) != null){
				//System.out.println(word);
				stopWords.add(word);
			}
			in.close();
		}
		catch(IOException e){
			System.out.println("Error: " + e.getMessage());
		}
	}

	
	public static void fileReader(String inputDataFileName) {
		// read sample text in from file
		try{
			BufferedReader in = new BufferedReader(new FileReader(inputDataFileName));
			wordLooper(in);
		}
		catch(IOException e){
			System.out.println("Error: " + e.getMessage());
		}
		
	}

	private static void wordLooper(BufferedReader in) throws IOException {
		String sentence;
		while((sentence = in.readLine()) != null){
			process(sentence);
		}
		in.close();
		
		sortWords();
	}
	
	public static void urlReader(String inputDataUrlname) throws Exception {
		URL oracle;
		String html = "";
//		try {
			oracle = new URL(inputDataUrlname);
			BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
			
			String sentence;
			while((sentence = in.readLine()) != null){
				html += sentence;
			}
			in.close();
			
			// Jsoup
			Document doc = Jsoup.parse(html);
			Element link = doc.select("a").first();
			
			String text = doc.body().text(); // "An example link"
			System.out.println("\nJsoup text: " + text);
			
			// OTHER EXAMPLE USE OF JSOUP
//			String linkHref = link.attr("href"); // "http://example.com/"
//			String linkText = link.text(); // "example""

//			String linkOuterH = link.outerHtml();  // "<a href="http://example.com"><b>example</b></a>"
//			String linkInnerH = link.html(); // "<b>example</b>"
			
//			System.out.println("link: " + link);
//			
//			System.out.println("linkHref: " + linkHref);
//			System.out.println("linkText: " + linkText);
//			
//			System.out.println("linkOuterH: " + linkOuterH);
//			System.out.println("linkInnerH: " + linkInnerH);
			
			process(text);
			sortWords();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	private static void process(String sentence) {
		// removes punctuation + to_lowercase
		String[] words = sentence.replaceAll("[^a-zA-Z ]", "").toLowerCase().split("\\s+");
		
		for (String word : words) {
			//System.out.println(word);
			if(word.length() > 1){
				addWord(word);
			}
		}
	}

	private static void addWord(String word) {
		if(!stopWords.contains(word)){
			
			if(validWords.containsKey(word)){
				int count = validWords.get(word);
				count++;
				validWords.replace(word, count);
			}
			else{
				validWords.put(word, 1);
			}
		}
		else{
			stopWordsFound++;
		}
	}
	
	private static void sortWords(){
		sortedWords.clear();
		sortedWords.putAll(validWords);
	}
	
	public static int getStopWordsFound() {
		return stopWordsFound;
	}
}
