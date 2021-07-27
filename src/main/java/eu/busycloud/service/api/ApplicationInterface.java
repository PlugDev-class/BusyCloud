package eu.busycloud.service.api;

import java.util.LinkedList;
import java.util.List;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.plugins.Application;
import eu.busycloud.service.console.ConsoleInstance;
import eu.busycloud.service.infrastructure.Infrastructure;
import eu.busycloud.service.networking.Networking;

public class ApplicationInterface {

	private static ApplicationInterface applicationInterface;
	private CloudInstance cloud;
	private Networking networking;
	private Infrastructure infrastructure;
	private ConsoleInstance console;

	private List<Application> plugins = new LinkedList<>();
	
	public ApplicationInterface(CloudInstance cloud) {
		applicationInterface = this;
		this.cloud = cloud;
		this.infrastructure = new Infrastructure();
		this.networking = new Networking();
		this.networking.initNetworking();
	}

	/*
	 * Method initializeInterface deprecated since BusyCloud-v2.0~dev
	 */
	
	@Deprecated
	public void initializeInterface(CloudInstance cloud) {
	}

	public static ApplicationInterface getAPI() {
		return applicationInterface;
	}

	public CloudInstance getCloud() {
		return cloud;
	}

	public Infrastructure getInfrastructure() {
		return infrastructure;
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

	/*
	 * Method setConsole never used since BusyCloud-v2.0~dev
	 * 
	 */
	public void setConsole(ConsoleInstance console) {
		this.console = console;
	}

}
