package org.iitg.mobileprofiler.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * This is the main class used to interact with the database. A class with
 * several methods in it. Contains methods for all database queries that will be
 * used.
 * 
 * @author RB
 * 
 */
public class DatabaseConnector {

	private static final String DATABASE_FILE_NAME = "repository.db";

	private Connection connection;

	/**
	 * Basic constructor. Opens a database connection.
	 */
	public DatabaseConnector() {
		openDBConnection();
	}

	public static String getDatabaseFileName() {
		return DATABASE_FILE_NAME;
	}

	public Connection getConnection() {
		return connection;
	}

	/**
	 * Tries to open a JDBC connection with the sqlite database.
	 */
	public void openDBConnection() {
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:"
					+ DATABASE_FILE_NAME);
		} catch (ClassNotFoundException | SQLException e) {
			System.out.println("Exception Caught." + e);
			e.printStackTrace();
		}
	}

	/**
	 * Does what it says. Calling this function is crucial. Don't want any open
	 * connections lingering around !
	 */
	public void closeDBConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			System.out.println("Exception Caught." + e);
			e.printStackTrace();
		}
	}

	/**
	 * A one time only function. Creates the tables with the required DB schema.
	 */
	public void createTables() {
		String query = "";
		try {
			Statement statement = connection.createStatement();

			// THIS WON'T SUFFICE. OPEN THE SQLITE FILE AND SET responseId to
			// INTEGER PRIMARY KEY. This is for the AUTO_INCREMENT thing.
			query = "CREATE TABLE IF NOT EXISTS `responses` (`responseId` int(11) NOT NULL,"
					+ "`userId` varchar(255) NOT NULL,`question` varchar(255) NOT NULL,"
					+ "`answer` int(11) NOT NULL,`className` varchar(255) NOT NULL)";
			statement.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("Exception Caught for query " + query + " \n"
					+ e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Given response data, this query inserts it into the table.
	 * @param userId
	 * @param question
	 * @param answer
	 * @param className
	 */
	public void insertResponse(String userId, String question, int answer, String className){
		String query = "INSERT INTO `responses` (`userId` ,`question` ,`answer` ,`className`)"
				+ "VALUES ('"+userId+"', '"+question+"', '"+answer+"', '"+className+"');";
		try {
			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("Exception Caught for query " + query + " \n"
					+ e);
			e.printStackTrace();
		}
	}

	/**
	 * Given an arraylist of response daos, this function will insert them into the DB.
	 * @param responseDaos
	 */
	public void insertResponses(ArrayList<ResponseDao> responseDaos){
		String query = "";
		try {
			Statement statement = connection.createStatement();
			for (int i = 0; i < responseDaos.size(); i++) {
				if (i % 450 == 0) {
					statement.executeUpdate(query);
					query = "INSERT INTO `responses` (`userId` ,`question` ,`answer` ,`className`) Select '"
							+ responseDaos.get(i).getUserId()
							+ "' AS `userId`, '"
							+ responseDaos.get(i).getQuestion()
							+ "' AS `question`, '"
							+ responseDaos.get(i).getAnswer()
							+ "' AS `answer`, '"
							+ responseDaos.get(i).getClassName()
							+ "' AS `className`";
				} else {
					query += "UNION SELECT '"
							+ responseDaos.get(i).getUserId() + "','"
							+ responseDaos.get(i).getQuestion() + "','"
							+ responseDaos.get(i).getAnswer() + "','"
							+ responseDaos.get(i).getClassName() + "'  ";
				}
			}
			statement.executeUpdate(query);
		} catch (SQLException e) {
			System.out.println("Exception Caught for query " + query + " \n"
					+ e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Gets the max responseId present in the table.
	 * @return
	 */
	public int getMaxResponseId(){
		String query = "SELECT MAX(responseId) from responses;";
		int responseId = 0;
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet;
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				responseId = resultSet.getInt("MAX(responseId)");
			}
		} catch (SQLException e) {
			System.out.println("Exception Caught for query " + query + " \n"
					+ e);
			e.printStackTrace();
		}
		return responseId;
	}
	
	/**
	 * Given a startingId and an endingId, this query will return an ArrayList of Responses.
	 * @param startingId
	 * @param endingId
	 * @return
	 */
	public ArrayList<ResponseDao> getResponses(int startingId, int endingId){
		ArrayList<ResponseDao> responseDaos = new ArrayList<ResponseDao>();
		String query = "Select * from `responses` where responseId>="+startingId+" AND responseId<="+endingId+"";
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet;
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				String userId = resultSet.getString("userId");
				String question = resultSet.getString("question");
				int answer = resultSet.getInt("answer");
				String className = resultSet.getString("className");
				responseDaos.add(new ResponseDao(userId, question, answer, className));
			}
		} catch (SQLException e) {
			System.out.println("Exception Caught for query " + query + " \n"
					+ e);
			e.printStackTrace();
		}
		return responseDaos;
	}

}
