package org.iitg.mobileprofiler.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
	

}
