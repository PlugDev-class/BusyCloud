package de.plugdev.cloud.external;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;

import de.plugdev.cloud.CloudInstance;
import de.plugdev.cloud.external.plugins.Application;
import de.plugdev.cloud.internal.console.ConsoleInstance;
import de.plugdev.cloud.internal.infrastructure.Infrastructure;
import de.plugdev.cloud.internal.networking.Networking;
import de.plugdev.cloud.lang.LanguageManager;

public class ApplicationInterface {

	private static ApplicationInterface applicationInterface;
	private final LanguageManager languageManager = new LanguageManager();
	private CloudInstance cloud;
	private Networking networking;
	private Infrastructure infrastructure;
	private ConsoleInstance console;

	private List<Application> plugins = new LinkedList<>();

	public void initializeInterface(CloudInstance cloud, boolean gui) throws IOException {
		applicationInterface = this;
		this.cloud = cloud;
		languageManager.loadVar(new URL("https://raw.githubusercontent.com/BusyCloud-Service/BusyCloud_Info/master/default.lang"));
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
