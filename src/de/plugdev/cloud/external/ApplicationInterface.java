package de.plugdev.cloud.external;

import java.util.LinkedList;
import java.util.List;

import de.plugdev.cloud.CloudInstance;
import de.plugdev.cloud.external.plugins.Application;
import de.plugdev.cloud.internal.console.ConsoleInstance;
import de.plugdev.cloud.internal.infrastructure.Infrastructure;
import de.plugdev.cloud.internal.networking.Networking;

public class ApplicationInterface {

	private static ApplicationInterface applicationInterface;
	private CloudInstance cloud;
	private Networking networking;
	private Infrastructure infrastructure;
	private ConsoleInstance console;

	private List<Application> plugins = new LinkedList<>();

	public void initializeInterface(CloudInstance cloud, boolean gui) {
		applicationInterface = this;
		this.cloud = cloud;
		this.infrastructure = new Infrastructure();
		this.networking = new Networking();
		this.networking.initNetworking();
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

	public void setConsole(ConsoleInstance console) {
		this.console = console;
	}

}
