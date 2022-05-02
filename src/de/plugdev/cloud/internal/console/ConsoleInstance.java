package de.plugdev.cloud.internal.console;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.UUID;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.external.ServerGroup;
import de.plugdev.cloud.internal.console.commands.CommandBackup;
import de.plugdev.cloud.internal.console.commands.CommandClearConsole;
import de.plugdev.cloud.internal.console.commands.CommandCloud;
import de.plugdev.cloud.internal.console.commands.CommandGroup;
import de.plugdev.cloud.internal.console.commands.CommandInstallSoftware;
import de.plugdev.cloud.internal.console.commands.CommandList;
import de.plugdev.cloud.internal.console.commands.CommandPingserver;
import de.plugdev.cloud.internal.console.commands.CommandProxy;
import de.plugdev.cloud.internal.console.commands.CommandRconServer;
import de.plugdev.cloud.internal.console.commands.CommandServerInfo;
import de.plugdev.cloud.internal.console.commands.CommandShutdown;
import de.plugdev.cloud.internal.console.commands.CommandStaticserver;
import de.plugdev.cloud.internal.console.commands.CommandStopServer;
import de.plugdev.cloud.internal.console.commands.CommandVersion;
import de.plugdev.cloud.internal.models.IVersion;
import de.plugdev.cloud.lang.ApiStatus.Internal;
import lombok.Data;

@Internal
@Data
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
		commandMap.put("/version", new CommandVersion());
		commandMap.put("/serverinfo", new CommandServerInfo());
		commandMap.put("/stopserver", new CommandStopServer());
		commandMap.put("/group", new CommandGroup());
		commandMap.put("/cls", new CommandClearConsole());
		commandMap.put("/clearconsole", new CommandClearConsole());
		commandMap.put("/clear", new CommandClearConsole());
		commandMap.put("/install", new CommandInstallSoftware());
		commandMap.put("/proxy", new CommandProxy());
		commandMap.put("/staticserver", new CommandStaticserver());
		commandMap.put("/list", new CommandList());
		new CommandVersion().runCommand(null, null);

		ConsoleOutput.write(ConsoleOutput.PURPLE, "*===========~~~~~~~~~~~~~~~~===========*");
		ConsoleOutput.write(ConsoleOutput.PURPLE, "*===========~ Startup ended ~==========*");
		ConsoleOutput.write(ConsoleOutput.PURPLE, "*===========~~~~~~~~~~~~~~~~===========*");

		ConsoleOutput.write(ConsoleOutput.YELLOW, "Now you have access to the console!");
		ConsoleOutput.write(ConsoleOutput.YELLOW, "Do you need help? Type '/cloud'.");

		
		try {
			List<String> lines = Files.readAllLines(new File("local/groups.pdv").toPath());
			for (String string : lines) {
				if (string.startsWith("#")) {
					continue;
				}
				String[] array = string.split(" \\| ");

				String groupName = array[0];
				UUID groupId = UUID.fromString(array[1]);
				int maxRamEachServer = Integer.parseInt(array[2]);
				int startPort = Integer.parseInt(array[3]);
				int activeRunningServers = Integer.parseInt(array[4]);
				int percentage = Integer.parseInt(array[5]);
				IVersion version = ApplicationInterface.getAPI().getInfrastructure().getVersionById(array[6]).get();
				boolean isMain = array[7].equalsIgnoreCase("yes");

				ServerGroup group = new ServerGroup(groupId, groupName, maxRamEachServer, version, startPort, isMain, null, activeRunningServers, percentage);
				group.start();
				if (!ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().contains(group))
					ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().add(group);
			}
		} catch (Exception exception) {
			ConsoleOutput.write(ConsoleOutput.RED_BOLD, "[PLUGIN] AN EXCEPTION WAS THROWN! " + exception.getMessage());
			exception.printStackTrace();
		}
		
		if(new File("server/static").listFiles().length != 0) {
			for(File root : new File("server/static").listFiles()) {
				String version = "";
				for(File subroot : new File(root.getAbsolutePath()).listFiles()) {
					if(subroot.getName().endsWith(".jar")) {
						version = subroot.getName().replaceAll(".jar", "");
					}
				}
				ApplicationInterface.getAPI().getInfrastructure().startSpigotServer(null, ApplicationInterface.getAPI().getInfrastructure().getVersionById(version).get(), true, 512, 0, false);
			}
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
				ConsoleOutput.write(ConsoleOutput.RED, "Command not found! Type '/cloud' for help.");
			}
		}

		scanner.close();
	}

}
