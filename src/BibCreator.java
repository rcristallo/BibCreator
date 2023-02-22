/**
 * ---------------------------------------------
 * @assignment
 * 	Advanced Object Oriented Programming
 * @teacher
 *  Salar Nasr Azadani
 * 
 * @authors
 * 	ID (1738452)
 *  Sarah Caron-Dadoun
 *  
 *  ID (2031354)
 *  Raffaelle Cristallo
 * 
 * @date completed
 * 	Apr 19, 2022 8:30pm
 * 
 * ---------------------------------------------
 * @moreInfo
 * 	I removed whitespaces to validate but did not re-add them.
 * 
 *  Instead of creating files for all .bib files I only created files for VALID .bib files.
 *  
 */


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

public class BibCreator {
	
	/*
	 * VARIABLES
	 */
	public static final String PATH = "Resources";
	public static final String END_OF_ARTICLE = "}";
	public static ArrayList<String> invalidFiles = new ArrayList<String>();
	public static ArrayList<String> validFiles = new ArrayList<String>();
	
	// Maximum number of chances user has to input the name of the file in the console
	public static final int NUM_OF_CHANCES = 2;
	
	File[] bibFiles;
	ArrayList<Article> articleObjects;
	
	// EXIT APP METHOD
	public void terminate() {
		System.exit(0);
	}
	
	/*
	 * ============ PUBLIC METHODS
	 */
	public void runFileValidation() {
		// Save file paths in Array
		File dirFile = new File(PATH);
		bibFiles = dirFile.listFiles();
		
				
		// Go through all files and validate each
		for(File f : bibFiles) {
			
			
			if(isValidFileType(f)) { // if file is .bib file
				
				try {
					articleObjects = getArticlesFromFile(f); // fill ArrayList with article Objects
					
					// loop through all article objects. 
					// If article object == null, do not create files.
					if(articleObjects != null) {
						
						int counter = 1;
						
						createNewJSON(articleObjects, f, "IEEE", counter);
						createNewJSON(articleObjects, f, "ACM", counter);
						createNewJSON(articleObjects, f, "NJ", counter);
						
						counter++;
						
						validFiles.add(f.toString());
					}
					else {
						invalidFiles.add(f.toString());
					}
					
				}
				catch(FileInvalidException e) {
					// continue loop even if error occurs
					continue;
				}
			}
			
			
		} // End of for loop
		System.out.println("\nInvalid files were not processed. Valid files have been created. See details below:");
		System.out.println("\nInvalid files : ");
		for(String item : invalidFiles) {
			System.out.println("\t" + item);
		}
		
		System.out.println("\nValid files : ");
		for(String item : validFiles) {
			System.out.println("\t" + item);
		}
		
		
		// Ask user if they want to review file or exit program.
		displayJSON();
	}


	/*
	 * ============ PRIVATE METHODS
	 */
	
	private boolean isValidFileType(File path)
	{
		if(path.isDirectory())
		{
			return false;
		}
		
		String[] fileName = path.getName().split("\\.");
		String fileType = fileName[1];
		String checkIfValidLatex = fileName[0].replaceAll("Latex", "");
		
		if(!checkIfValidLatex.matches("[0-9]+") || !fileType.equals("bib"))
		{
			return false;
		}
	
		return true;
	}
	
	
	private void displayJSON() {
		
		Scanner scanner = new Scanner(System.in); 
		String fileName;
		FileReader fileReader;
		int chances = 2;
		
		while(chances > 0) {	
			
			System.out.println("\nPlease, enter the name of the file you wish to review (type E to exit program): ");
			fileName = scanner.next().trim(); // Trim removes whitespace chars
			
			if(fileName.equals("E") || fileName.equals("e")) { // Exit program if user inputs "E" or "e"
				System.out.println("Exiting program...Bye!");
				scanner.close();
				terminate();
			}
			
			String path = PATH + "/" + fileName;
			System.out.println("File : " + fileName );
			File jsonFile = new File(path);
			
			// Verify if file exists
			if(jsonFile.exists()) {
				// Display IEEE, ACM, NJ Formats
				try {
					
					fileReader = new FileReader(jsonFile);
					BufferedReader br = new BufferedReader(fileReader);
					
					String line;
								
					while((line = br.readLine()) != null)
					{
						// Display line content
						System.out.println(line);	
					}
					
					br.close();
					
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
			else {
				// If file doesn't exist
				chances = chances -1;
				
				if(chances == 1) {
					System.out.println("File could not be found or does not exist.\nPossible file was not created because "
							+ "file was invalid (empty field was present).");
				}
				if(chances == 0) {
					System.out.println("File could not be found or does not exist AGAIN. Possible file was not created.\n"
							+ "Sorry! I am unable to display your desired files. Program will exit.");
				}
				
				
			}
		} // end of while loop
		
		scanner.close();
	}	
	
	
	private void createNewJSON(ArrayList<Article> aObjects, File f, String fileNameCharacters, int counter) {
		String strFileName = f.getName();
		String format = fileNameCharacters;
		String fileNameDigit = strFileName.replaceAll("[_Latex]|[_.bib]", ""); // from "Latex1.bib" -> "1"
	
		//int fileDigit = Integer.parseInt(fileDigitStr);	// Convert to integer
		
		String newFileName = "/" + fileNameCharacters + fileNameDigit + ".json";
		
		try {
			FileWriter writer = new FileWriter(PATH + newFileName);
			PrintWriter printWriter = new PrintWriter(writer);
			
			switch (format.toLowerCase()) {
			
			case "ieee":
				for(Article a : aObjects) {
					Map<String, String> articleMap = a.getArticleMap();
					
					String author = articleMap.get("author");
					String title = articleMap.get("title");
					String journal = articleMap.get("journal");
					String volume = articleMap.get("volume");
					String number = articleMap.get("number");
					String pages = articleMap.get("pages");
					String month = articleMap.get("month");
					String year = articleMap.get("year");
					
					printWriter.println( author + ". \"" + title + "\"," + journal + ", vol. " + volume + ", no. " + number
							+ ", p. " + pages +", " + month + " " + year + ".\n");					
				}
				break;
				
				
			case "acm":
				for(Article a : aObjects) {
					Map<String, String> am = a.getArticleMap();
					
					String author = am.get("author");
					String firstAuthor = author.split(",")[0];
					String title = am.get("title");
					String journal = am.get("journal");
					String volume = am.get("volume");
					String number = am.get("number");
					String pages = am.get("pages");
					String year = am.get("year");
					String doi = am.get("doi");
					
					printWriter.println("[" + counter +"] " + firstAuthor + " et al. " + year + ". " + title + ". " + journal + ". " + volume + ", " 
							+ number + "(" + year + "), " + pages + ". DOI:http:" + doi + ".\n");
				}
				break;
				
				
			case "nj":
				for(Article a: aObjects) {
					Map<String, String> am = a.getArticleMap();
					
					String author = am.get("author");
					String title = am.get("title");
					String journal = am.get("journal");
					String volume = am.get("volume");
					String pages = am.get("pages");
					String year = am.get("year");
					
					printWriter.println(author + ". " + title + ". " + journal + ". " + volume + ", " + pages + "(" + year + ").\n");
				}
				break;
				
				
			default:
				printWriter.close();
				throw new IllegalArgumentException("Unexpected value: " + format.toLowerCase());
			}
			
			printWriter.close();
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}
	
	
	private ArrayList<Article> getArticlesFromFile(File thisFile) throws FileInvalidException{
		
		ArrayList<Article> allArticles = new ArrayList<Article>();			  // List of @ARTICLES in this file
		ArrayList<KeyValuePair> articleInfo = new ArrayList<KeyValuePair>();  // List of Info in each @ARTICLE in this file
		KeyValuePair kv;
		
		BufferedReader fileReader = null; // Declare variable of type BufferedReader to read this file
		
		try {
			
			// Initialize variable of type BufferedReader to read this 
			// file here because we want to catch the error if there are any
			fileReader = new BufferedReader(new FileReader(thisFile));
			
			// Set variable equal to the current line
			String thisLine = fileReader.readLine();
			StringTokenizer tokenize = new StringTokenizer((thisFile.getName()), "/");
			String fileName = tokenize.nextToken();
			
			while ((thisLine = fileReader.readLine()) != null) {	// Read next line as long as there is one
				
				
				if(thisLine.startsWith("@ARTICLE{")) {
					// Skip rest of code & Go to next line
					continue;
				}
				if(thisLine.equals("") || thisLine.matches("[_ ]")) {
					// Skip empty line
					continue;
				}
				if(thisLine.startsWith(END_OF_ARTICLE)) { // If end of @ARTICLE is reached
					
					// Create new Article Object
					Article a = new Article(articleInfo);
					
					// Add object to the list of @ARTICLES
					allArticles.add(a);
					
					// Reset list of key-value pairs to empty list for next @ARTICLE
					articleInfo = new ArrayList<KeyValuePair>();
				}
				else {
					// Remove unwanted chars from line
					String strTemp = thisLine.replaceAll("[_{}, ]", "");
					
					if(strTemp.matches("[0-9]+")) {
						// If string only contains digits it means it is reading the ID.
						// We will create a key and a value for this ID as it does not come 
						// with a key already.
						KeyValuePair idPair = new KeyValuePair("ID", strTemp);
						
						// Add it to key-value pair List
						articleInfo.add(idPair);
						
						// Skip rest of code & Go to next line
						continue;
					}
					else {
						// Split strings
						String arrTemp[] = strTemp.split("=");
						
						// If array only has 1 index (0) it means article is Invalid
						if(arrTemp.length < 2) {
							System.out.println("\nERROR : Detected Empty Field!\n=============================\n" 
												+ "Problem detected with input file: " + fileName + ".\nFile is "
												+ "Invalid: Field '" + arrTemp[0].toUpperCase() + "' is empty. Processing stopped "
												+ "at this point.\nOther empty fields may be present as well.");
							
							// Return empty arrayList
							allArticles = null;
							break;
						}
						else {
							if(arrTemp[1] == "" || arrTemp[1] == "..." || arrTemp[1] == " ") {
								// error message to stop processing this file and move on to next .bib file
								System.out.println("\nERROR : Detected Empty Field!\n=============================\n"
										+ "Problem detected with input file: " + fileName + ".\nFile is "
										+ "Invalid: Field '" + arrTemp[0].toUpperCase() + "' is empty. Processing stopped "
										+ "at this point.\nOther empty fields may be present as well.");
								
								// Return empty arrayList
								allArticles = null;
								break;
							}
							else {
								// Initialize KeyValuePair Object
								kv = new KeyValuePair(arrTemp[0], arrTemp[1]);
								
								// Add Pairs to articleInfoList
								articleInfo.add(kv);
							}
						}
						
					}
				}
			} // end of while loop
			
			// Close bufferReader
			fileReader.close();			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return allArticles;
	}
	
	
	/*
	 * MAIN METHOD
	 */
	
	public static void main(String[] args) {
		
		System.out.println("Welcome to BibCreator!\n\n");
		
		BibCreator bib = new BibCreator();
		bib.runFileValidation();
		
		
	}

}
