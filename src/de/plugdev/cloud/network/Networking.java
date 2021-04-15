package de.plugdev.cloud.network;

import de.plugdev.cloud.network.receive.DecodeGeneral;
import de.plugdev.cloud.network.receive.DecodeProxy;
import de.plugdev.cloud.network.receive.DecodeSpigotServer;
import de.terrarier.netlistening.Server;

public class Networking {

	public void initNetworking() {
		final Server server = new Server.Builder(1130).timeout(15000).encryption().build().compression()
				.nibbleCompression(true).varIntCompression(true).build().build();

		server.registerListener(new DecodeProxy());
		server.registerListener(new DecodeSpigotServer());
		server.registerListener(new DecodeGeneral());
	}

}
