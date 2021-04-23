package de.plugdev.cloud.api;

import java.util.LinkedList;
import java.util.List;

import de.plugdev.cloud.Cloud;
import de.plugdev.cloud.api.plugins.Application;
import de.plugdev.cloud.console.ConsoleInstance;
import de.plugdev.cloud.infrastructure.Infrastructure;
import de.plugdev.cloud.networking.Networking;

public class ApplicationInterface {
	
	private static ApplicationInterface applicationInterface;
	private Cloud cloud;
	private Networking networking;
	private ConsoleInstance console;
	
	private List<Application> plugins = new LinkedList<>();
	
	public void initializeInterface(Cloud cloud) {
		applicationInterface = this;
		this.cloud = cloud;
		this.networking = new Networking();
		this.networking.initNetworking();
 	}
	
//	public void callEvent(Event event) {
//		for(Application application : getPlugins()) {
			//TODO: Calling some events..
//		}
//	}
	
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
	
	public List<Application> getPlugins() {
		return plugins;
	}
	
	public ConsoleInstance getConsole() {
		return console;
	}
	
	public void setConsole(ConsoleInstance console) {
		this.console = console;
	}
	
}
