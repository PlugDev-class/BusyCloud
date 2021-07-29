package eu.busycloud.service.tasks;

import eu.busycloud.service.infrastructure.SpigotServer;

public class ServerDisableCheckTask extends Thread {

	private SpigotServer spigotServer;
	public ServerDisableCheckTask(SpigotServer spigotServer) {
		this.spigotServer = spigotServer;
		run();
	}
	
	
	/**
	 * 
	 * This method checks after 15 seconds of wating, 
	 * if a server tries to reconnect after timeout.
	 * If it does, it stays alive and if it doesn't 
	 * it stops the instance.
	 * 
	 * @since 2.0
	 */
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
