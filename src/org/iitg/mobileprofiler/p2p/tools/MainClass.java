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

}
