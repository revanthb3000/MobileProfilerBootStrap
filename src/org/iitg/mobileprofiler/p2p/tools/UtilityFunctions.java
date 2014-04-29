package org.iitg.mobileprofiler.p2p.tools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;

import org.iitg.mobileprofiler.db.DatabaseConnector;
import org.iitg.mobileprofiler.db.ResponseDao;

public class UtilityFunctions {
	
	/**
	 * Utility function that generates the hexdigest for a given input string.
	 * @param inputString
	 * @return
	 */
	public static String getHexDigest(String inputString){
		MessageDigest md = null;;
		try {
			md = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return "";
		}
		md.update(inputString.getBytes());
		byte[] digest = md.digest();
		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(Integer.toHexString((int) (b & 0xff)));
		}
		return sb.toString();
	}
	
	/**
	 * Basically just inserts the response classes into the table.
	 */
	public static void fillResponseClasses(){
		DatabaseConnector databaseConnector = new DatabaseConnector();

		databaseConnector.insertResponseClass("Animation");
		databaseConnector.insertResponseClass("Cricket");
		databaseConnector.insertResponseClass("Entertainment");
		databaseConnector.insertResponseClass("Football");
		databaseConnector.insertResponseClass("Movies");
		databaseConnector.insertResponseClass("News");
		databaseConnector.insertResponseClass("Politics");
		databaseConnector.insertResponseClass("Social");
		databaseConnector.insertResponseClass("Technology");
		databaseConnector.insertResponseClass("Tennis");
		databaseConnector.insertResponseClass("TV Show");


		databaseConnector.closeDBConnection();
	}
	
	/**
	 * I extract data from the csv file and then add them to the DB.
	 */
	public static void insertExperimentalResponses(){
		try{
			FileReader fileReader = new FileReader("experiment.csv");
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			
			ArrayList<String> questions = new ArrayList<String>();
			ArrayList<Integer> classIds = new ArrayList<Integer>();
			
			ArrayList<ResponseDao> responseDaos = new ArrayList<ResponseDao>();
			
			String[] splitArray = null;
			
			String line = "";
			//Questions
			line = bufferedReader.readLine();
			splitArray = line.split(",");
			for(int i=1;i<splitArray.length;i++){
				questions.add(splitArray[i]);
			}
			
			//Class Ids
			line = bufferedReader.readLine();
			splitArray = line.split(",");
			for(int i=1;i<splitArray.length;i++){
				classIds.add(Integer.parseInt(splitArray[i]));
			}
			
			
			while((line=bufferedReader.readLine())!=null){
				splitArray = line.split(",");
				String userName = splitArray[0];
				for(int i=1;i<splitArray.length;i++){
					if(splitArray[i].trim().equals("")){
						//Question Not Answered
//						System.out.println("Here for " + userName + " at question : " + questions.get(i-1));
						continue;
					}
					else{
						int answer = Integer.parseInt(splitArray[i].trim());
						responseDaos.add(new ResponseDao(UtilityFunctions.getHexDigest(userName), questions.get(i-1), answer, classIds.get(i-1)));
					}
				}
			}
			
			System.out.println(responseDaos.size());
			Collections.shuffle(responseDaos);
			for(ResponseDao responseDao : responseDaos){
				System.out.println(responseDao);
			}
			
			DatabaseConnector databaseConnector = new DatabaseConnector();
			databaseConnector.insertResponses(responseDaos);
			databaseConnector.closeDBConnection();
			
			bufferedReader.close();
			fileReader.close();	
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
}
