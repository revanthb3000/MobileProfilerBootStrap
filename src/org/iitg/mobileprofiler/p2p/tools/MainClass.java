package org.iitg.mobileprofiler.p2p.tools;

import it.unipr.ce.dsg.s2p.org.json.JSONException;

import org.iitg.mobileprofiler.db.DatabaseConnector;
import org.iitg.mobileprofiler.p2p.peer.BootstrapPeer;

/**
 * This is used to test the P2P classes
 * 
 * @author RB
 * 
 */
public class MainClass {

	private static int boostrapPort = 5080;

	public static void main(String[] args) throws JSONException {
		startBootstrapNode();
	}

	public static void startBootstrapNode() {
		BootstrapPeer peer = new BootstrapPeer(
				UtilityFunctions.getHexDigest("bootstrap"), "bootstrap",
				boostrapPort);
		System.out.println("BootStrap Node has started - " + peer.toString());
	}
	
	public static void fillResponseClasses(){
		DatabaseConnector databaseConnector = new DatabaseConnector();

		databaseConnector.insertResponseClass("Politics");
		databaseConnector.insertResponseClass("Tennis");
		databaseConnector.insertResponseClass("TV Show");
		databaseConnector.insertResponseClass("Technology");
		databaseConnector.insertResponseClass("Football");
		databaseConnector.insertResponseClass("Social");
		databaseConnector.insertResponseClass("News");
		databaseConnector.insertResponseClass("Entertainment");
		databaseConnector.insertResponseClass("Cricket");
		databaseConnector.insertResponseClass("Movies");

		databaseConnector.closeDBConnection();
	}

}
