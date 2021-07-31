package eu.busycloud.service.utils;

import java.util.UUID;

import de.terrarier.netlistening.Connection;
import eu.busycloud.service.infrastructure.ProxyServer;
import eu.busycloud.service.infrastructure.ServerSoftware;

public abstract class SingleServerInstance {
	
	protected Process instance;
	protected UUID id;
	protected String serverName;
	protected String serverGroup;
	
	protected int port;
	protected int proxyId;
	protected int maxRam;
	protected ServerSoftware serverSoftware;
	protected boolean eula;
	protected boolean isStatic;

	protected Connection connection;
	protected String registerKey;

	protected ProxyServer preferredProxy = null;
	protected boolean isLobbyServer = false;
	
	public abstract void startServer();
	public abstract void stopServer();
	public abstract void rconServer(String command);
	
	public UUID getId() {
		return id;
	}
	
	public Process getInstance() {
		return instance;
	}
	
	public String getServerName() {
		return serverName;
	}
	
	public String getPartServerName() {
		return serverName.split("#")[0];
	}
	
	public String getServerGroup() {
		return serverGroup;
	}
	
	public int getPort() {
		return port;
	}
	
	public int getProxyId() {
		return proxyId;
	}
	
	public int getMaxRam() {
		return maxRam;
	}
	
	public ServerSoftware getServerSoftware() {
		return serverSoftware;
	}
	
	public boolean isEula() {
		return eula;
	}
	
	public boolean isStatic() {
		return isStatic;
	}
	
	public Connection getConnection() {
		return connection;
	}
	
	public String getRegisterKey() {
		return registerKey;
	}
	
	public ProxyServer getPreferredProxy() {
		return preferredProxy;
	}
	
	public boolean isLobbyServer() {
		return isLobbyServer;
	}
	
	public void setConnection(Connection connection) {
		this.connection = connection;
	}
	/**
	 * @param instance the instance to set
	 */
	public final void setInstance(Process instance) {
		this.instance = instance;
	}
	/**
	 * @param id the id to set
	 */
	public final void setId(UUID id) {
		this.id = id;
	}
	/**
	 * @param serverName the serverName to set
	 */
	public final void setServerName(String serverName) {
		this.serverName = serverName;
	}
	/**
	 * @param serverGroup the serverGroup to set
	 */
	public final void setServerGroup(String serverGroup) {
		this.serverGroup = serverGroup;
	}
	/**
	 * @param port the port to set
	 */
	public final void setPort(int port) {
		this.port = port;
	}
	/**
	 * @param proxyId the proxyId to set
	 */
	public final void setProxyId(int proxyId) {
		this.proxyId = proxyId;
	}
	/**
	 * @param maxRam the maxRam to set
	 */
	public final void setMaxRam(int maxRam) {
		this.maxRam = maxRam;
	}
	/**
	 * @param serverSoftware the serverSoftware to set
	 */
	public final void setServerSoftware(ServerSoftware serverSoftware) {
		this.serverSoftware = serverSoftware;
	}
	/**
	 * @param eula the eula to set
	 */
	public final void setEula(boolean eula) {
		this.eula = eula;
	}
	/**
	 * @param isStatic the isStatic to set
	 */
	public final void setStatic(boolean isStatic) {
		this.isStatic = isStatic;
	}
	/**
	 * @param registerKey the registerKey to set
	 */
	public final void setRegisterKey(String registerKey) {
		this.registerKey = registerKey;
	}
	/**
	 * @param preferredProxy the preferredProxy to set
	 */
	public final void setPreferredProxy(ProxyServer preferredProxy) {
		this.preferredProxy = preferredProxy;
	}
	/**
	 * @param isMain the isMain to set
	 */
	public void setLobbyServer(boolean isLobbyServer) {
		this.isLobbyServer = isLobbyServer;
	}
	public abstract void ping();
	public abstract void printInfo();
	
}
