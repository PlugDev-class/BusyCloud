package de.plugdev.cloud.api;

import de.plugdev.cloud.Cloud;
import de.plugdev.cloud.infrastructure.Infrastructure;
import de.plugdev.cloud.network.Networking;

public class ApplicationInterface {
	
	private static ApplicationInterface applicationInterface;
	private Cloud cloud;
	private Networking networking;
	
	public void initializeInterface(Cloud cloud) {
		applicationInterface = this;
		this.cloud = cloud;
		this.networking = new Networking();
		this.networking.initNetworking();
 	}
	
	public static ApplicationInterface getAPI() {
		return applicationInterface;
	}
	
	public Cloud getCloud() {
		return cloud;
	}
	
	public Infrastructure getInfrastructure() {
		return cloud.getInfrastructure();
	}
	
	public Networking getNetworking() {
		return networking;
	}
	
}
