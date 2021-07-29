package eu.busycloud.service.utils;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;

import org.json.JSONObject;

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

	/**
	 * 
	 * This method sets the proxyServer by input "proxyServer"
	 * 
	 * @param serverSoftware
	 * @since 2.0
	 */
	public void setProxyServer(ProxyServer proxyServer) {
		this.proxyServer = proxyServer;
	}

	/**
	 * 
	 * This method sets the serverSoftware by given ServerSoftware input "serverSoftware"
	 * 
	 * @param serverSoftware
	 * @since 2.0
	 */
	
	public void setServerSoftware(ServerSoftware serverSoftware) {
		this.serverSoftware = serverSoftware;
	}

	/**
	 * 
	 * This method sets the serverSoftware by given String input "serverSoftware"
	 * 
	 * @param serverSoftware
	 * @since 2.0
	 */
	public void setServerSoftware(String serverSoftware) {
		this.serverSoftware = ApplicationInterface.getAPI().getInfrastructure().getVersionById(serverSoftware);
	}

	/**
	 * 
	 * This method sets the startNewServerByPercentage by given input "startNewServerByPercentage"
	 * 
	 * @param startNewServerByPercentage
	 * @since 2.0
	 */
	public void setStartNewServerByPercentage(int startNewServerByPercentage) {
		this.startNewServerByPercentage = startNewServerByPercentage;
	}

	/**
	 * 
	 * This method sets the groupId by given input "groupId"
	 * 
	 * @param groupId
	 * @since 2.0
	 */
	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	/**
	 * 
	 * This method sets the lobbyState by given input "lobbyState"
	 * 
	 * @param lobbyState
	 * @since 2.0
	 */
	public void setLobbyState(boolean lobbyState) {
		this.lobbyState = lobbyState;
	}

	/**
	 * 
	 * This method sets the maxRamEachServer by given input "maxRamEachServer"
	 * 
	 * @param maxRamEachServer
	 * @since 2.0
	 */
	public void setMaxRamEachServer(int maxRamEachServer) {
		this.maxRamEachServer = maxRamEachServer;
	}

	/**
	 * 
	 * This method sets the startPort by given input "startPort"
	 * 
	 * @param startPort
	 * @since 2.0
	 */
	public void setStartPort(int startPort) {
		this.startPort = startPort;
	}

	/**
	 * 
	 * This method sets the groupName by given input "groupName"
	 * 
	 * @param groupName
	 * @since 2.0
	 */
	
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * 
	 * This method returns the ServerSoftware "serverSoftware"
	 * 
	 * @return serverSoftware
	 * @since 2.0
	 */

	public ServerSoftware getServerSoftware() {
		return serverSoftware;
	}

	/**
	 * 
	 * This method returns the Integer "startPort"
	 * 
	 * @return startPort
	 * @since 2.0
	 */

	public int getStartPort() {
		return startPort;
	}

	/**
	 * 
	 * This method returns the Integer "groupId"
	 * 
	 * @return groupId
	 * @since 2.0
	 */

	public int getGroupId() {
		return groupId;
	}

	/**
	 * 
	 * This method returns the Integer "maxRamEachServer"
	 * 
	 * @return maxRamEachServer
	 * @since 2.0
	 */

	public int getMaxRamEachServer() {
		return maxRamEachServer;
	}

	/**
	 * 
	 * This method returns the Integer "startNewServerByPercentage"
	 * 
	 * @return startNewServerByPercentage
	 * @since 2.0
	 */

	public int getStartNewServerByPercentage() {
		return startNewServerByPercentage;
	}

	/**
	 * 
	 * This method returns the String "groupname"
	 * 
	 * @return groupName
	 * @since 2.0
	 */

	public String getGroupName() {
		return groupName;
	}

	/**
	 * 
	 * This method returns the ProxyServer "proxyServer"
	 * 
	 * @return proxyServer
	 * @since 2.0
	 */

	public ProxyServer getProxyServer() {
		return proxyServer;
	}

	/**
	 * 
	 * This method returns the boolean "lobbyState"
	 * 
	 * @return lobbyState
	 * @since 2.0
	 */

	public boolean isLobbyState() {
		return lobbyState;
	}

	/**
	 * 
	 * This method sets the value "startServerByGroupstart"
	 * 
	 * @param startServerByGroupstart
	 * @since 2.0
	 */

	public void setStartServerByGroupstart(int startServerByGroupstart) {
		this.startServerByGroupstart = startServerByGroupstart;
	}

	/**
	 * 
	 * This method returns the Integer "startServerByGroupstart"
	 * 
	 * @return int
	 * @since 2.0
	 */

	public int getStartServerByGroupstart() {
		return startServerByGroupstart;
	}

	/**
	 * 
	 * This method runs, if the container needs more informations about a
	 * servergroup with reading through the servergroups.json
	 * 
	 * @since 2.0
	 * 
	 */
	public void loadInformationsByServerGroup() {
		try {
			JSONObject jsonObject = new JSONObject(
					new String(Files.readAllBytes(new File("configurations/servergroups.json").toPath()), "UTF-8"));
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

	/**
	 * 
	 * This method runs, if the container sees, that no proxy is running and the
	 * groups needs nonetheless a proxy.
	 * 
	 * @return proxyServer
	 * @since 2.0
	 * 
	 */

	public ProxyServer startProxyCauseNotExists() {
		if (ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size() != 0) {
			ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().forEach((proxy) -> {
				if (proxyServer == null)
					proxyServer = proxy;
				if (proxy.getOnlinePlayer().size() >= proxyServer.getOnlinePlayer().size())
					proxyServer = proxy;
			});
			return proxyServer;
		}
		Arrays.asList(ApplicationInterface.getAPI().getInfrastructure().serverSoftwares).forEach((software) -> {
			if (software.isAvailable() && software.getType() == ServerSoftwareType.PROXY) {
				int proxyID = ApplicationInterface.getAPI().getInfrastructure().startProxyServer(
						"Proxy-" + (ApplicationInterface.getAPI().getInfrastructure().getRunningProxies().size() + 1),
						software);
				setProxyServer(ApplicationInterface.getAPI().getInfrastructure().getProxyById(proxyID));
			}
		});
		return proxyServer;
	}
}
