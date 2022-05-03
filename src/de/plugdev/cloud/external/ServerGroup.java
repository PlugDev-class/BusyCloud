package de.plugdev.cloud.external;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import de.plugdev.cloud.internal.infrastructure.IService;
import de.plugdev.cloud.internal.infrastructure.Proxy;
import de.plugdev.cloud.internal.models.IVersion;
import de.terrarier.netlistening.Connection;
import lombok.Data;

@Data
public class ServerGroup implements IService {

	private final UUID uniqueId;
	private final String name;

	private final int maxRamEachServer;
	private final IVersion version;

	private final int startPort;
	private final boolean isLobby;

	private final Proxy proxy;
	private final List<IService> groupList = new ArrayList<>();

	private final int defaultActiveServers;
	private final int percentage;
	
	private int onlinePlayers;

	public void init() {
		if(!ApplicationInterface.getAPI().getInfrastructure().getGroupByName(name).isPresent())
			ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().add(this);
		for (int i = 0; i < defaultActiveServers; i++) {
			start();
		}
	}
	
	@Override
	public void start() {
		UUID serverID = ApplicationInterface.getAPI().getInfrastructure().startSpigotServer(name, version, false, maxRamEachServer, startPort+groupList.size(), isLobby);
		IService server = ApplicationInterface.getAPI().getInfrastructure().getServiceById(serverID).get();
		groupList.add(server);
	}

	@Override
	public void stop() {
		groupList.forEach((iService) -> iService.stop());
	}

	@Override
	public void rcon(String command) {
		groupList.forEach((iService) -> iService.rcon(command));
	}

	@Override
	public String getKey() {
		return null;
	}

	@Override
	public void setConnection(Connection object) {
	}

	@Override
	public Connection getConnection() {
		return null;
	}

	@Override
	public int getPort() {
		return startPort;
	}

}
