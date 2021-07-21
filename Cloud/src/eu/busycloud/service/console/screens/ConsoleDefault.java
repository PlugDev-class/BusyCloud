package eu.busycloud.service.console.screens;

import java.util.HashMap;
import java.util.Map;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleCommand;
import eu.busycloud.service.console.ConsoleScreen;
import eu.busycloud.service.console.commands.CommandBackup;
import eu.busycloud.service.console.commands.CommandClearConsole;
import eu.busycloud.service.console.commands.CommandCloud;
import eu.busycloud.service.console.commands.CommandDeleteCloud;
import eu.busycloud.service.console.commands.CommandGroup;
import eu.busycloud.service.console.commands.CommandInstallSoftware;
import eu.busycloud.service.console.commands.CommandPingserver;
import eu.busycloud.service.console.commands.CommandProxy;
import eu.busycloud.service.console.commands.CommandRconServer;
import eu.busycloud.service.console.commands.CommandServerInfo;
import eu.busycloud.service.console.commands.CommandShutdown;
import eu.busycloud.service.console.commands.CommandStaticserver;
import eu.busycloud.service.console.commands.CommandStopServer;

public class ConsoleDefault implements ConsoleScreen {

	private Map<String, ConsoleCommand> commandMap = new HashMap<>();

	public ConsoleDefault() {

		ApplicationInterface.getAPI().getConsole().setConsoleDefault(this);

		commandMap.put("/backup", new CommandBackup("Make a backup of the whole filesystem."));
		commandMap.put("/help", new CommandCloud("Shows the relevant BusyCloud-Commands"));
		commandMap.put("/shutdown", new CommandShutdown("Shutdowns the network"));
		commandMap.put("/rcon", new CommandRconServer("Send a console-command to a specific server/proxy"));
		commandMap.put("/ping", new CommandPingserver("Ask for a pluginwise-feedback from a specific server/proxy"));
		commandMap.put("/serverinfo", new CommandServerInfo("Shows the relevant BusyCloud-Serverinfos"));
		commandMap.put("/stopserver", new CommandStopServer("Stops a specific server"));
		commandMap.put("/group", new CommandGroup("Complex group-command for setups/pings/commands/rcons etc."));
		commandMap.put("/clearconsole", new CommandClearConsole("Clears the console-log"));
		commandMap.put("/install", new CommandInstallSoftware("Install serversoftware like Spigotdistributions, proxies etc."));
		commandMap.put("/proxy", new CommandProxy("Complex proxy-command for setups/pings/commands/rcons etc."));
		commandMap.put("/staticserver", new CommandStaticserver("Complex staticserver-command for setups/pings/commands/rcons etc."));
		commandMap.put("/deletecloud", new CommandDeleteCloud("The last command you touch! It will delete the whole cloud!"));
	}

	@Override
	public void scanLine(String input) {
		try {
			if (commandMap.containsKey(input.split(" ")[0])) {
				ConsoleCommand command = commandMap.get(input.split(" ")[0]);
				command.runCommand(input, input.split(" "));
			} else {
				CloudInstance.LOGGER.warning("Command not found! Type '/help' for further informations.");
			}
		} catch (ArrayIndexOutOfBoundsException exception) {
			exception.printStackTrace();
			CloudInstance.LOGGER.warning("Command not found! Type '/help' for further informations.");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	public Map<String, ConsoleCommand> getCommandMap() {
		return commandMap;
	}
}
