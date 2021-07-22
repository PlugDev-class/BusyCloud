package eu.busycloud.service.console.screens;

import java.util.HashMap;
import java.util.Map;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleCommand;
import eu.busycloud.service.console.ConsoleScreen;
import eu.busycloud.service.console.commands.CommandBackup;
import eu.busycloud.service.console.commands.CommandClearConsole;
import eu.busycloud.service.console.commands.CommandHelp;
import eu.busycloud.service.console.commands.CommandDeleteCloud;
import eu.busycloud.service.console.commands.CommandGroup;
import eu.busycloud.service.console.commands.CommandInstallSoftware;
import eu.busycloud.service.console.commands.CommandIntroduction;
import eu.busycloud.service.console.commands.CommandProxy;
import eu.busycloud.service.console.commands.CommandShutdown;
import eu.busycloud.service.console.commands.CommandStaticServer;
import eu.busycloud.service.console.commands.CommandTempServer;

public class ConsoleCloudDefault implements ConsoleScreen {

	private Map<String, ConsoleCommand> commandMap = new HashMap<>();

	public ConsoleCloudDefault() {
		ApplicationInterface.getAPI().getConsole().setConsoleDefault(this);
		commandMap.put("/introduction", new CommandIntroduction("This command provides informations for beginners."));
		commandMap.put("/backup", new CommandBackup("Make a backup of the whole filesystem."));
		commandMap.put("/help", new CommandHelp("Shows the relevant BusyCloud-Commands"));
		commandMap.put("/shutdown", new CommandShutdown("Shutdowns the network"));
		commandMap.put("/clear", new CommandClearConsole("Clears the console-log"));
		commandMap.put("/install", new CommandInstallSoftware("Install serversoftware like Spigotdistributions, proxies etc."));
		commandMap.put("/proxy", new CommandProxy("Complex proxy-command for setups/pings/commands/rcons etc."));
		commandMap.put("/staticserver", new CommandStaticServer("Complex staticserver-command for setups/pings/commands/rcons etc."));
		commandMap.put("/tempserver", new CommandTempServer("Complex tempserver-command for setups/pings/commands/rcons etc."));
		commandMap.put("/servergroup", new CommandGroup("Complex servergroup-command for setups/pings/commands/rcons etc."));
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
