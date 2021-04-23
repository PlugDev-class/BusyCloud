package de.plugdev.cloud.console;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.api.ServerGroup;
import de.plugdev.cloud.console.commands.CommandBackup;
import de.plugdev.cloud.console.commands.CommandClearConsole;
import de.plugdev.cloud.console.commands.CommandCloud;
import de.plugdev.cloud.console.commands.CommandGroup;
import de.plugdev.cloud.console.commands.CommandInstallSoftware;
import de.plugdev.cloud.console.commands.CommandLicense;
import de.plugdev.cloud.console.commands.CommandLicenseupdate;
import de.plugdev.cloud.console.commands.CommandPingserver;
import de.plugdev.cloud.console.commands.CommandProxy;
import de.plugdev.cloud.console.commands.CommandRconServer;
import de.plugdev.cloud.console.commands.CommandServerInfo;
import de.plugdev.cloud.console.commands.CommandShutdown;
import de.plugdev.cloud.console.commands.CommandStopServer;
import de.plugdev.cloud.infrastructure.MinecraftVersion;

public class ConsoleInstance {

	private Map<String, ConsoleCommand> commandMap = new HashMap<>();

	private OutputStream backendStream = null;
	private OutputStream currentStream = null;

	private InputStream inputStream = null;

	public ConsoleInstance() {

		backendStream = System.out;
		currentStream = backendStream;

		ApplicationInterface.getAPI().setConsole(this);

		commandMap.put("/backup", new CommandBackup());
		commandMap.put("/cloud", new CommandCloud());
		commandMap.put("/shutdown", new CommandShutdown());
		commandMap.put("/rcon", new CommandRconServer());
		commandMap.put("/ping", new CommandPingserver());
		commandMap.put("/license", new CommandLicense());
		commandMap.put("/version", new CommandLicense());
		commandMap.put("/serverinfo", new CommandServerInfo());
		commandMap.put("/stopserver", new CommandStopServer());
		commandMap.put("/group", new CommandGroup());
		commandMap.put("/cls", new CommandClearConsole());
		commandMap.put("/clearconsole", new CommandClearConsole());
		commandMap.put("/clear", new CommandClearConsole());
		commandMap.put("/install", new CommandInstallSoftware());
		commandMap.put("/proxy", new CommandProxy());
		commandMap.put("/licenseupdate", new CommandLicenseupdate());
		new CommandLicense().runCommand(null, null);

		ConsoleOutput.write(ConsoleOutput.PURPLE, "[CONSOLE] *==========~~~~~~~~~~~~~~~~==========*");
		ConsoleOutput.write(ConsoleOutput.PURPLE, "[CONSOLE] *==========~ Loadup ended ~==========*");
		ConsoleOutput.write(ConsoleOutput.PURPLE, "[CONSOLE] *==========~~~~~~~~~~~~~~~~==========*");

		ConsoleOutput.write(ConsoleOutput.YELLOW, "[CONSOLE] Now you have access to the console!");
		ConsoleOutput.write(ConsoleOutput.YELLOW, "[CONSOLE] Do you need help? Type '/cloud'.");

		List<String> lines;
		try {
			lines = Files.readAllLines(new File("local/groups.pdv").toPath());
			for (String string : lines) {
				if (string.startsWith("#")) {
					continue;
				}
				String[] array = string.split(" \\| ");

				String groupName = array[0];
				int groupId = Integer.parseInt(array[1]);
				int maxRamEachServer = Integer.parseInt(array[2]);
				int startPort = Integer.parseInt(array[3]);
				int activeRunningServers = Integer.parseInt(array[4]);
				int percentage = Integer.parseInt(array[5]);
				MinecraftVersion version = ApplicationInterface.getAPI().getInfrastructure().getVersionById(array[6]);
				boolean isMain = array[7].equalsIgnoreCase("yes");

				ServerGroup group = new ServerGroup(version, startPort, groupName, groupId, null, maxRamEachServer,
						activeRunningServers, percentage, isMain);
				if (!ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().contains(group)) {
					ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().add(group);
				}
			}
		} catch (Exception exception) {
			ConsoleOutput.write(ConsoleOutput.RED_BOLD, "[PLUGIN] AN EXCEPTION WAS THROWN! " + exception.getMessage());
			exception.printStackTrace();
		}

		Scanner scanner = new Scanner(System.in);
		String input = "";
		while ((input = scanner.nextLine()) != null) {
			if (commandMap.containsKey(input.split(" ")[0])) {
				try {
					ConsoleCommand command = commandMap.get(input.split(" ")[0]);
					command.runCommand(input, input.split(" "));
				} catch (Exception exception) {
					exception.printStackTrace();
				}
			} else {
				ConsoleOutput.write(ConsoleOutput.RED, "[CONSOLE] Command not found! Type '/cloud' for help.");
			}
		}

		scanner.close();
	}

	public InputStream getInputStream() {
		return inputStream;
	}

	public OutputStream getCurrentStream() {
		return currentStream;
	}

	public OutputStream getBackendStream() {
		return backendStream;
	}

	public Map<String, ConsoleCommand> getCommandMap() {
		return commandMap;
	}

}
