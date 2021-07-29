package eu.busycloud.service.console.commands;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.console.ConsoleCommand;
import eu.busycloud.service.utils.TextUtils;

public class CommandIntroduction extends ConsoleCommand {

	public CommandIntroduction(String help) {
		super(help);
	}

	@Override
	public void runCommand(String command, String[] args) {
		TextUtils.sendPlainHeader();
		CloudInstance.LOGGER.info("  ___     _               _         _   _          ");
		CloudInstance.LOGGER.info(" |_ _|_ _| |_ _ _ ___  __| |_  _ __| |_(_)___ _ _  ");
		CloudInstance.LOGGER.info("  | || ' \\  _| '_/ _ \\/ _` | || / _|  _| / _ \\ ' \\ ");
		CloudInstance.LOGGER.info(" |___|_||_\\__|_| \\___/\\__,_|\\_,_\\__|\\__|_\\___/_||_|");
		CloudInstance.LOGGER.info("                                                   ");
		TextUtils.sendLine();
		CloudInstance.LOGGER.info("Tip: Install an easier Introduction-Page as website with: /install <Web-Introduction-'version'>");
		TextUtils.sendLine();
		CloudInstance.LOGGER.info("First steps:");
		CloudInstance.LOGGER.info("1. Download some softwares to use in further steps.");
		CloudInstance.LOGGER.info(" -> /install storelist");
		CloudInstance.LOGGER.info(" \tChoose a software in the *PROXY*-Category.");
		CloudInstance.LOGGER.info(" \tWe prefer \"Travertine-1.16\" or \"BungeeCord-1.17\"");
		CloudInstance.LOGGER.info(" -> /install <Software>");
		CloudInstance.LOGGER.info(" \tNow you can choose a spigot software in the *JAVA*-Category");
		CloudInstance.LOGGER.info(" \tWe prefer \"Paper-1.8.8\" for PvP-Servers, Lobbies or minigames.");
		CloudInstance.LOGGER.info(" \tFor buildingservers like CityBuild, FreeBuild or similiar we prefer \"Paper-1.16.5\".");
		CloudInstance.LOGGER.info(" \tThe installation is the same, just type:");
		CloudInstance.LOGGER.info(" -> /install <Software>");
		TextUtils.sendLine();
		CloudInstance.LOGGER.info("Setup proxies and servergroups:");
		CloudInstance.LOGGER.info("1. Explanation");
		CloudInstance.LOGGER.info(" \t\"Proxies and servergroups\" sounds like very difficult things, but it's suprisingly simple with BusyCloud!");
		CloudInstance.LOGGER.info(" \t\"Proxies\" are functional softwares to spread players across your network ~ without much work.");
		CloudInstance.LOGGER.info(" \t\"Servergroups\" are collections of dynamics servers. If a server is needed, the group will start");
		CloudInstance.LOGGER.info(" \ta new server, otherwise it'll shutdowns a server to save valuable resources. ");
		CloudInstance.LOGGER.info(" \tFor example, many players are online and your previous lobbyserver can't handle it, the");
		CloudInstance.LOGGER.info(" \tservergroup will decide to start a new one to prevent lags in any way.");
		CloudInstance.LOGGER.info("2. Execution");
		CloudInstance.LOGGER.info(" \tIn general the cloud should've a proxy preinstalled.");
		CloudInstance.LOGGER.info(" \tEven though a \"Lobby\"-servergroup should be there as well.");
		CloudInstance.LOGGER.info(" \tBut you're not finished yet, if you want a minigames-server, you've to setup a new group.");
		CloudInstance.LOGGER.info(" \tThis is very easy with the proper command:");
		CloudInstance.LOGGER.info(" -> /servergroup setup create");
		CloudInstance.LOGGER.info(" \tAfter executing this command, you should've a simple assistant to setup your group.");
		CloudInstance.LOGGER.info(" \tIf you want to edit a specific group like Lobby, you can simply type:");
		CloudInstance.LOGGER.info(" -> /servergroup setup edit");
		CloudInstance.LOGGER.info(" \tSometimes you don't need a group anymore for any reason. If this happens, do a backup with:");
		CloudInstance.LOGGER.info(" -> /backup create");
		CloudInstance.LOGGER.info(" \tand delete the group with:");
		CloudInstance.LOGGER.info(" -> /servergroup setup delete");
		TextUtils.sendFatLine();
	}

}
