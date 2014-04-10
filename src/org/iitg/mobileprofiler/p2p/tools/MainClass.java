package org.iitg.mobileprofiler.p2p.tools;

import java.util.ArrayList;

import it.unipr.ce.dsg.s2p.org.json.JSONException;

import org.iitg.mobileprofiler.db.DatabaseConnector;
import org.iitg.mobileprofiler.db.ResponseDao;
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
		ArrayList<ResponseDao> responseDaos = new ArrayList<ResponseDao>();
		responseDaos.add(new ResponseDao("rb3000", "This is a question", 0, "N/A"));
		responseDaos.add(new ResponseDao("rb3000", "This is a question", 0, "N/A"));
		responseDaos.add(new ResponseDao("rb30030", "This is a question", 0, "N/A"));
		DatabaseConnector databaseConnector = new DatabaseConnector();
		databaseConnector.insertResponses(responseDaos);
		databaseConnector.closeDBConnection();
		//startBootstrapNode();
	}

	public static void startBootstrapNode() {
		BootstrapPeer peer = new BootstrapPeer(
				UtilityFunctions.getHexDigest("bootstrap"), "bootstrap",
				boostrapPort);
		System.out.println("BootStrap Node has started - " + peer.toString());
	}

}
