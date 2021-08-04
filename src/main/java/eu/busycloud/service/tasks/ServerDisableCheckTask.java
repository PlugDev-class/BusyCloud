package eu.busycloud.service.tasks;

import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.infrastructure.ProxyServer;
import eu.busycloud.service.networking.easypackets.EasyPacket;
import eu.busycloud.service.networking.easypackets.EasyPacketHeader;
import eu.busycloud.service.utils.SingleServerInstance;

public class ServerDisableCheckTask extends Thread {

	private SingleServerInstance serverInstance;

	public ServerDisableCheckTask(SingleServerInstance serverInstance) {
		this.serverInstance = serverInstance;
		run();
	}

	/**
	 * 
	 * This method checks after 15 seconds of waiting, if a server tries to
	 * reconnect after timeout. If it does, it stays alive and if it doesn't it
	 * stops the instance.
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
		if (serverInstance.getConnection() == null) {
			final ProxyServer proxyServer = ApplicationInterface.getAPI().getInfrastructure().getProxyById(0);
			serverInstance.stopServer();
			final EasyPacket newEasyPacket = new EasyPacket(EasyPacketHeader.CLOUD, "PUSH_DISSERVER",
					serverInstance.getId().toString());
			ApplicationInterface.getAPI().getNetworking().getEasyPacketHandler().sendPacket(proxyServer, newEasyPacket);

		}
	}

}
