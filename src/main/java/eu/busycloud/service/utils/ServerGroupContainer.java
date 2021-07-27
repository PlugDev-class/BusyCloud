package eu.busycloud.service.utils;

import java.io.File;
import java.nio.file.Files;

import org.json.JSONObject;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.infrastructure.ProxyServer;
import eu.busycloud.service.infrastructure.ServerSoftware;
import eu.busycloud.service.infrastructure.ServerSoftware.ServerSoftwareType;

public class ServerGroupContainer {
	
	private String groupName;
	private ProxyServer proxyServer;
	private ServerSoftware serverSoftware;
	private int startPort;
	private int groupId;
	private int maxRamEachServer;
	private int startNewServerByPercentage;
	private int startServerByGroupstart;
	private boolean lobbyState;
	
	public ServerGroupContainer(String groupName) {
		setGroupName(groupName);
		loadInformationsByServerGroup();
	}
	
	public void setProxyServer(ProxyServer proxyServer) {
		this.proxyServer = proxyServer;
	}
	
	public void setServerSoftware(ServerSoftware serverSoftware) {
		this.serverSoftware = serverSoftware;
	}
	
	public void setServerSoftware(String serverSoftware) {
		this.serverSoftware = ApplicationInterface.getAPI().getInfrastructure().getVersionById(serverSoftware);
	}
	
	public void setStartNewServerByPercentage(int startNewServerByPercentage) {
		this.startNewServerByPercentage = startNewServerByPercentage;
	}
	
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}
	
	public void setLobbyState(boolean lobbyState) {
		this.lobbyState = lobbyState;
	}
	
	public void setMaxRamEachServer(int maxRamEachServer) {
		this.maxRamEachServer = maxRamEachServer;
	}
	
	public void setStartPort(int startPort) {
		this.startPort = startPort;
	}
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public ServerSoftware getServerSoftware() {
		return serverSoftware;
	}
	
	public int getStartPort() {
		return startPort;
	}
	
	public int getGroupId() {
		return groupId;
	}
	
	public int getMaxRamEachServer() {
		return maxRamEachServer;
	}
	
	public int getStartNewServerByPercentage() {
		return startNewServerByPercentage;
	}
	
	public String getGroupName() {
		return groupName;
	}
	
	public ProxyServer getProxyServer() {
		return proxyServer;
	}
	
	public boolean isLobbyState() {
		return lobbyState;
	}
	
	public void setStartServerByGroupstart(int startServerByGroupstart) {
		this.startServerByGroupstart = startServerByGroupstart;
	}
	
	public int getStartServerByGroupstart() {
		return startServerByGroupstart;
	}
	
	public void loadInformationsByServerGroup() {
		try {
			JSONObject jsonObject = new JSONObject(new String(Files.readAllBytes(new File("configurations/servergroups.json").toPath()), "UTF-8"));
			JSONObject object = jsonObject.getJSONObject(groupName);
			setServerSoftware(object.getString("serverSoftware"));
			setStartPort(object.getInt("startPort"));
			setStartServerByGroupstart(object.getInt("startServerByGroupstart"));
			setGroupId(object.getInt("groupId"));
			setLobbyState(object.getBoolean("lobbyState"));
			setMaxRamEachServer(object.getInt("maxRamEachServer"));
			setStartNewServerByPercentage(object.getInt("startNewServerByPercentage"));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}
	
	public ProxyServer startProxyCauseNotExists() {
		if (ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size() == 0) {
			boolean did = false;
			for(ServerSoftware serverSoftware2 : ApplicationInterface.getAPI().getInfrastructure().serverSoftwares)
				if(serverSoftware2.isAvailable() && serverSoftware2.getType() == ServerSoftwareType.PROXY) {
					int proxyID = ApplicationInterface.getAPI().getInfrastructure().startProxyServer(
							"Proxy-" + (ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size() + 1),
							serverSoftware2);
					setProxyServer(ApplicationInterface.getAPI().getInfrastructure().getProxyById(proxyID));
					did = true;
				}
			if(!did) {
				CloudInstance.LOGGER.info("Couldn't start servergroup " + groupName + " ~ no proxy installed.");
				CloudInstance.LOGGER.info("Install Proxy with '/install' otherwise get introduction with '/introduction'.");
				return null;
			}
		} else {
			for(ProxyServer cProxy : ApplicationInterface.getAPI().getInfrastructure().getRunningProxies()) {
				if(proxyServer == null)
					proxyServer = cProxy;
				if(cProxy.getOnlinePlayer().size() >= proxyServer.getOnlinePlayer().size())
					proxyServer = cProxy;
			}
		}
		return proxyServer;
	}
}
