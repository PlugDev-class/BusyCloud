package de.plugdev.cloud.console.commands;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.console.ConsoleColors;
import de.plugdev.cloud.console.ConsoleCommand;

public class CommandInput extends ConsoleCommand {
	
	@Override
	public void runCommand(String command, String[] args) {
		if(args.length == 2) {
			if(args[1].equalsIgnoreCase("quit")) {
				ApplicationInterface.getAPI().getConsole().setCurrentStream(ApplicationInterface.getAPI().getConsole().getBackendStream());
				ConsoleColors.write(ConsoleColors.RED, "[PLUGIN] Quit Session.");
			}
		} else if(args.length == 3) {
			if(args[1].equalsIgnoreCase("join")) {
				int parse = Integer.parseInt(args[2]);
				if(ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(parse) == null) {
					ConsoleColors.write(ConsoleColors.RED, "[PLUGIN] Server with specific id not found.");
					return;
				}
				ApplicationInterface.getAPI().getConsole().setInputStream(
						ApplicationInterface.getAPI().getInfrastructure().getSpigotServerById(parse).getInstance().getInputStream());
				ConsoleColors.write(ConsoleColors.RED, "[PLUGIN] Joined Session -> " + parse);
			}
		} else {
			ConsoleColors.write(ConsoleColors.RED, "Wrong syntax.");
			ConsoleColors.write(ConsoleColors.RED, "/input join <ServerID>");
			ConsoleColors.write(ConsoleColors.RED, "/input quit");
		}
	}
	
	
}
