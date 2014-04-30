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
					+ "`answer` int(11) NOT NULL,`classId` int(11) NOT NULL)";
			statement.executeUpdate(query);

			// THIS WON'T SUFFICE. OPEN THE SQLITE FILE AND SET classId to
			// INTEGER PRIMARY KEY. This is for the AUTO_INCREMENT thing.
			query = "CREATE TABLE IF NOT EXISTS `responseclasses` (`classId` "
					+ "int(11) NOT NULL,`className` varchar(255) NOT NULL)";
			statement.executeUpdate(query);

		} catch (SQLException e) {
			System.out.println("Exception Caught for query " + query + " \n"
					+ e);
			e.printStackTrace();
		}
	}

	/*************************************************************************
	 * Queries that run on the response Class table follow.
	 **************************************************************************/

	/**
	 * This is used to insert a className into the response class table.
	 * Standard query.
	 * @param className
	 */
	public void insertResponseClass(String className) {
		String query = "INSERT INTO responseclasses (`className`) VALUES ('"
				+ className + "');";
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
	 * Given a classId, the className is returned.
	 * @param classId
	 * @return
	 */
	public String getResponseClassName(int classId) {
		String query = "SELECT className FROM `responseclasses` WHERE `classId` = "
						+ classId + "";
		String className = "";
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet;
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				className = resultSet.getString("className");
			}
		} catch (SQLException e) {
			System.out.println("Exception Caught for query " + query + " \n"
					+ e);
			e.printStackTrace();
		}
		return className;
	}
	
	/**
	 * Given a className, the classId is returned.
	 * @param classId
	 * @return
	 */
	public int getResponseClassId(String className) {
		String query = "SELECT classId FROM `responseclasses` WHERE `className` = \""
						+ className + "\";";
		int classId = 0;
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet;
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				classId = resultSet.getInt("classId");
			}
		} catch (SQLException e) {
			System.out.println("Exception Caught for query " + query + " \n"
					+ e);
			e.printStackTrace();
		}
		return classId;
	}

	/*************************************************************************
	 * Queries that run on the response table follow.
	 **************************************************************************/

	/**
	 * Given response data, this query inserts it into the table.
	 * 
	 * @param userId
	 * @param question
	 * @param answer
	 * @param classId
	 */
	public void insertResponse(String userId, String question, int answer,
			int classId) {
		String query = "INSERT INTO `responses` (`userId` ,`question` ,`answer` ,`classId`)"
				+ "VALUES ('"
				+ userId
				+ "', '"
				+ question
				+ "', '"
				+ answer
				+ "', '" + classId + "');";
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
	 * Given an arraylist of response daos, this function will insert them into
	 * the DB.
	 * 
	 * @param responseDaos
	 */
	public void insertResponses(ArrayList<ResponseDao> responseDaos) {
		String query = "";
		try {
			Statement statement = connection.createStatement();
			for (int i = 0; i < responseDaos.size(); i++) {
				if (i % 450 == 0) {
					statement.executeUpdate(query);
					query = "INSERT INTO `responses` (`userId` ,`question` ,`answer` ,`classId`) Select '"
							+ responseDaos.get(i).getUserId()
							+ "' AS `userId`, '"
							+ responseDaos.get(i).getQuestion()
							+ "' AS `question`, '"
							+ responseDaos.get(i).getAnswer()
							+ "' AS `answer`, '"
							+ responseDaos.get(i).getClassId()
							+ "' AS `classId`";
				} else {
					query += "UNION SELECT '" + responseDaos.get(i).getUserId()
							+ "','" + responseDaos.get(i).getQuestion() + "','"
							+ responseDaos.get(i).getAnswer() + "','"
							+ responseDaos.get(i).getClassId() + "'  ";
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
	 * Gets the max responseId present in the table (we remove the user's Id
	 * from consideration)
	 * 
	 * @return
	 */
	public int getMaxResponseId(String blacklistUserId) {
		String query = "SELECT MAX(responseId) from responses WHERE userId!=\""
				+ blacklistUserId + "\";";
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
	 * Given a response Dao, this function returns the maximum responseId.
	 * 
	 * @param responseDao
	 * @return
	 */
	public int getResponseIdGivenDao(ResponseDao responseDao) {
		String query = "SELECT MAX(responseId) from responses WHERE userId=\""
				+ responseDao.getUserId() + "\" AND question=\""
				+ responseDao.getQuestion() + "\"" + " AND answer="
				+ responseDao.getAnswer() + " AND classId="
				+ responseDao.getClassId() + ";";
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
	 * Given a startingId and an endingId, this query will return an ArrayList
	 * of Responses.
	 * 
	 * @param startingId
	 * @param endingId
	 * @return
	 */
	public ArrayList<ResponseDao> getResponses(int startingId, int endingId) {
		ArrayList<ResponseDao> responseDaos = new ArrayList<ResponseDao>();
		String query = "Select * from `responses` where responseId>="
				+ startingId + " AND responseId<=" + endingId + "";
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet;
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				String userId = resultSet.getString("userId");
				String question = resultSet.getString("question");
				int answer = resultSet.getInt("answer");
				int classId = resultSet.getInt("classId");
				responseDaos.add(new ResponseDao(userId, question, answer,
						classId));
			}
		} catch (SQLException e) {
			System.out.println("Exception Caught for query " + query + " \n"
					+ e);
			e.printStackTrace();
		}
		return responseDaos;
	}

	/**
	 * Given a question, this function returns all responses.
	 * 
	 * @param question
	 * @return
	 */
	public ArrayList<ResponseDao> getAnswersOfQuestion(String question) {
		String query = "Select * from `responses` Where question='" + question
				+ "';";
		ArrayList<ResponseDao> responseDaos = new ArrayList<ResponseDao>();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet;
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				String userId = resultSet.getString("userId");
				int answer = resultSet.getInt("answer");
				int classId = resultSet.getInt("classId");
				responseDaos.add(new ResponseDao(userId, question, answer,
						classId));
			}
		} catch (SQLException e) {
			System.out.println("Exception Caught for query " + query + " \n"
					+ e);
			e.printStackTrace();
		}
		return responseDaos;
	}

	/**
	 * Returns a list of unique questions in repo.
	 * 
	 * @return
	 */
	public ArrayList<String> getQuestionsList() {
		String query = "Select distinct(question) from `responses`;";
		ArrayList<String> questions = new ArrayList<String>();
		try {
			Statement statement = connection.createStatement();
			ResultSet resultSet;
			resultSet = statement.executeQuery(query);
			while (resultSet.next()) {
				String question = resultSet.getString("question");
				questions.add(question);
			}
		} catch (SQLException e) {
			System.out.println("Exception Caught for query " + query + " \n"
					+ e);
			e.printStackTrace();
		}
		return questions;
	}

}