package eu.busycloud.plugin.utils;

import java.util.ArrayList;
import java.util.List;

import net.md_5.bungee.api.config.ServerInfo;

public class ProxyCloudInformations {
	
	private List<ServerInfo> serverInfo = new ArrayList<ServerInfo>();
	
	private List<String> motdPlayerinfo = new ArrayList<String>();
	private int motdMaxplayers = 40;
	private String motdProtocolString = "";
	private String motdDescription = "";
	private String cloudKey;
	
	public List<String> getMotdPlayerinfo() {
		return motdPlayerinfo;
	}
	
	public String getCloudKey() {
		return cloudKey;
	}
	
	public void setCloudKey(String cloudKey) {
		this.cloudKey = cloudKey;
	}
	
	public void setMotdDescription(String motdDescription) {
		this.motdDescription = motdDescription;
	}
	
	public String getMotdDescription() {
		return motdDescription;
	}
	
	public int getMotdMaxplayers() {
		return motdMaxplayers;
	}
	
	public void setMotdMaxplayers(int motdMaxplayers) {
		this.motdMaxplayers = motdMaxplayers;
	}
	
	public String getMotdProtocolString() {
		return motdProtocolString;
	}
	
	public void setMotdProtocolString(String motdProtocolString) {
		this.motdProtocolString = motdProtocolString;
	}
	
	public List<ServerInfo> getLobbyServers() {
		return serverInfo;
	}
	
}
