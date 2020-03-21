package edu.ilstu;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class UserPrompt	{
	
	//PLEASE CHANGE THIS IF THERE IS A DIFFERENT PATH FOR THE CSV FILES.
	private String fileLocation = "c:/users/willp/Desktop/";
	private final String error = "Unable to find file. Please make sure you have the right filepath and filename.";
	
	//holds the data read from relationship files
	private ArrayList<String> dataList = new ArrayList<>();
	
	//holds the files that have been read 
	private ArrayList<String> filesRead = new ArrayList<>();
	
	//this holds the info from the Terms.csv file
	private ArrayList<String> diagnosis = new ArrayList<>();
	
	//menu for the user to see
	public void greeting() {
		System.out.println("-----------------------WELCOME------------------------------");
		System.out.println(" 	The list of commands goes as follows");
		System.out.println("To perform an action, press one of the keys and hit enter.");
		System.out.println("------------------------------------------------------------");
		System.out.println("*Add data: A ");
		System.out.println("*Save data for a relationship: R");
		System.out.println("*Save data for a medical terms: T");
		System.out.println("*Exit terminal: E ");
	}
	
	//reads the file and fills the arrays
	//reads up to 10 blank spaces until it stops reading the file
	public void addData() {
	Scanner userInput = new Scanner(System.in);
	String fileName; 
	File file;
	int count = 0;
	int limit = 0;
	String header;
		System.out.println("Please enter the name of the file that you want to read");
		fileName = userInput.next();
		file = new File(fileLocation+fileName);
		if(filesRead.indexOf(fileName)!=-1) {
			System.out.println(fileName + " Has already been read");
			return;
		}
		try {
			Scanner readFile = new Scanner(file);
			header = readFile.nextLine();
			while(limit<=10) {
				if(readFile.hasNextLine()) {
					String data = readFile.nextLine();
						if(header.equals("CUI,Name")) {
							diagnosis.add(data);
							count++;
							limit = 0;
					}	
					else {
						dataList.add(data);
						count++;
						limit = 0;
				}
				}
				else {
				readFile.skip("");	
				limit++;
				}
			}
			System.out.println("\n"+count+" line(s) read from "+fileName);
			System.out.println("---------------------------------");
			filesRead.add(fileName);
		}
		catch (FileNotFoundException e) {
			System.out.println(error);
		}
	}
	
	//write to a CSV file
	public void writeRelationships() {
		Scanner userInput = new Scanner(System.in);
		String relationship,fileName,item;
		File file;
		System.out.println("Please enter a relationship name");
		relationship = userInput.nextLine();
		if(!isRelationship(relationship)) {
			System.out.println("Unable to find "+relationship+" In the database");
			return;
		}
		System.out.println("Please enter a file name to export the information");
		fileName = userInput.nextLine();
		file = new File(fileLocation+fileName);
		try {
			FileWriter writeFile = new FileWriter(file,true);
			BufferedWriter br = new BufferedWriter(writeFile);
			PrintWriter pw = new PrintWriter(br);
			pw.println("STR,Relationship,STR2");
			for(int i = 0; i<dataList.size(); i++) {
				item = dataList.get(i);
				if(item.contains(relationship)) {
					pw.println(getDiagnosis(item)+relationship+","+item.substring(item.lastIndexOf(',')+1));
				}
			}
			pw.flush();
			System.out.println("Process completed.");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//method finds the CUI and matches it with the diagnosis CUI and returns the diagnosis.
	public String getDiagnosis(String id) {
		String val;
		for(int i = 0; i<diagnosis.size(); i++) {
			val = diagnosis.get(i);
			if(id.substring(0,id.indexOf(',')).equals(val.substring(0,val.indexOf(',')))){
				return val.substring(val.indexOf(',')+1)+",";
			}
		}
		return null;
	}
	
	//returns true if relationship exists in the dataList
	private boolean isRelationship(String relationship) {
		String el,segment;
		int start;
		for(int i = 0; i<dataList.size(); i++) {
			el = dataList.get(i);
			start = el.indexOf(',')+1;
			segment = el.substring(start);
			if(segment.substring(0,segment.indexOf(',')).equals(relationship)) {
				return true;
			}
		}
		return false;
	}
	
	
	//matches the data in the diagnosis list, returns the medical term from the id
	private String getMedicalTerm(String data,String term) {
	String el,mTerm;
	data = data.substring(0,data.indexOf(','));
	for(int i = 0; i< diagnosis.size(); i++) {
		el = diagnosis.get(i);
		mTerm = el.substring(el.indexOf(',')+1);
		if(el.substring(0,el.indexOf(',')).equals(data)&&mTerm.equals(term)) {
			return mTerm+",";	
		}	
	}	
		return null;
	}
	
	//extracts the relationship in the dataList.
	private String getRelationship(String data) {
		data = data.substring(data.indexOf(',')+1);
		return data.substring(0,data.indexOf(','))+",";
	}
	
	//searches the diagnosis and determines if the input exists in the list
	private boolean isMedicalTerm(String term) {
		for(int i = 0; i<diagnosis.size(); i++) {
			if(diagnosis.get(i).contains(term)) {
				return true;
			}
		}
		return false;
	}
	
	//Writes to a csv file with medical information
	public void writeMedicalTerms() {
		Scanner userInput = new Scanner(System.in);
		String medicalTerm,fileName,item;
		File file;
		System.out.println("Please enter a medical term");
		medicalTerm = userInput.nextLine();
		if(!isMedicalTerm(medicalTerm)) {
			System.out.println("Unable to find "+medicalTerm+" In the database.");
			return;
		}
		System.out.println("Please enter a file name to export the information");
		fileName = userInput.nextLine();
		file = new File(fileLocation+fileName);
		try {
			FileWriter writeFile = new FileWriter(file,true);
			BufferedWriter br = new BufferedWriter(writeFile);
			PrintWriter pw = new PrintWriter(br);
			pw.println("STR,Relationship,STR2");
			for(int i = 0; i<dataList.size(); i++) {
				item = dataList.get(i);
				if(getMedicalTerm(item,medicalTerm)!=null) {
				pw.println(getMedicalTerm(item,medicalTerm)+getRelationship(item)+item.substring(item.lastIndexOf(',')+1));
				}
			}
			pw.flush();
			System.out.println("Process completed.");
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	//Runner for the program
	public void run() {
		String token;
		Scanner input = new Scanner(System.in);
		boolean inUse = true;
		greeting();
		while(inUse) {
			System.out.println("\n\nEnter in a key to begin a process...\n");
			System.out.print("DataIntegrator> ");
			token = input.next();
			token = token.toLowerCase();
			switch (token){
			case "a":
				addData();
				break;
			case "t":
				writeMedicalTerms();
				break;
			case "r":
				writeRelationships();
				break;
			case "e":
				inUse = false;
				break;
				default:
					System.out.println("Invalid input, The list of commands goes as follows\n");
					System.out.println("*Add data: A ");
					System.out.println("*Save data for a relationship: R");
					System.out.println("*Save data for a medical terms: T");
					System.out.println("*Exit terminal: E ");
					//System.out.println(dataList.toString());
					break;
			}
		}
		System.out.println("\nGoodbye");
	}
}
