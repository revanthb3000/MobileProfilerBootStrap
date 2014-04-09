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
			query = "CREATE TABLE IF NOT EXISTS `classcontents` "
					+ "(`classId` int(11) NOT NULL,`numberOfDocs` "
					+ "int(11) NOT NULL,PRIMARY KEY (`classId`));";
			statement.executeUpdate(query);

		} catch (SQLException e) {
			System.out.println("Exception Caught for query " + query + " \n"
					+ e);
			e.printStackTrace();
		}
	}

}
