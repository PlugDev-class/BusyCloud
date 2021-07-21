package eu.busycloud.service.tasks;

import eu.busycloud.service.infrastructure.SpigotServer;

public class ServerDisableCheckTask extends Thread {

	private SpigotServer spigotServer;
	public ServerDisableCheckTask(SpigotServer spigotServer) {
		this.spigotServer = spigotServer;
		run();
	}
	
	@Override
	public void run() {
		try {
			Thread.sleep(15000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if(spigotServer.getConnection() == null)
			spigotServer.stopServer();
		super.run();
	}
	
	
	
}
