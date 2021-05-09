package de.plugdev.cloud.external;

import java.util.LinkedList;
import java.util.List;

import de.plugdev.cloud.CloudInstance;
import de.plugdev.cloud.external.permissions.PermissionsSystem;
import de.plugdev.cloud.external.plugins.Application;
import de.plugdev.cloud.internal.console.ConsoleInstance;
import de.plugdev.cloud.internal.guiinterface.GuiInterface;
import de.plugdev.cloud.internal.infrastructure.Infrastructure;
import de.plugdev.cloud.internal.networking.Networking;

public class ApplicationInterface {

	private static ApplicationInterface applicationInterface;
	private CloudInstance cloud;
	private Networking networking;
	private Infrastructure infrastructure;
	private ConsoleInstance console;
	private PermissionsSystem permissionsSystem;
	private GuiInterface guiInterface;

	private List<Application> plugins = new LinkedList<>();

	public void initializeInterface(CloudInstance cloud) {
		applicationInterface = this;
		this.cloud = cloud;
		this.infrastructure = new Infrastructure();
		this.guiInterface = new GuiInterface();
		this.guiInterface.initGUI();
		this.networking = new Networking();
		this.networking.initNetworking();
		this.permissionsSystem = new PermissionsSystem();
		this.permissionsSystem.init("local/permissions/groups.pdv");
	}

//	public void callEvent(Event event) {
//		for(Application application : getPlugins()) {
	// TODO: Calling some events..
//		}
//	}

	public static ApplicationInterface getAPI() {
		return applicationInterface;
	}

	public CloudInstance getCloud() {
		return cloud;
	}

	public PermissionsSystem getPermissionsSystem() {
		return permissionsSystem;
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

	public GuiInterface getGuiInterface() {
		return guiInterface;
	}

	public void setConsole(ConsoleInstance console) {
		this.console = console;
	}
	
}
