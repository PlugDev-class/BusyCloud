package de.plugdev.cloud.internal.console.commands;

import java.util.Optional;
import java.util.UUID;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.console.ConsoleCommand;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.infrastructure.IService;
import de.plugdev.cloud.internal.models.IVersion;

public class CommandStaticserver implements ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		if (args.length < 2) {
			ConsoleOutput.write(ConsoleOutput.RED, "Wrong syntax.");
			ConsoleOutput.write(ConsoleOutput.RED, "/staticserver <Serverid> stop");
			ConsoleOutput.write(ConsoleOutput.RED, "/staticserver <Serverid> rcon <command>");
			ConsoleOutput.write(ConsoleOutput.RED, "/staticserver <Serverid> add <Version> <Port> <MaxRamEachServer>");
			return;
		}

		UUID uuid = UUID.fromString(args[1]);
		if(uuid == null) {
			ConsoleOutput.write(ConsoleOutput.CYAN, "Service not found! UniqueId invalid.");
			return;
		}
		Optional<IService> iService = ApplicationInterface.getAPI().getInfrastructure().getServiceById(uuid);
		if(!iService.isPresent()) {
			ConsoleOutput.write(ConsoleOutput.CYAN, "Service not found!");
			return;
		}
		
		if(args.length == 3) {
			if(args[2].equalsIgnoreCase("stop")) {
				iService.get().stop();
			}
		} else if(args.length > 3) {
			if(args[2].equalsIgnoreCase("rcon")) {
				StringBuilder builder = new StringBuilder();
				for(int i = 3; i < args.length; i++) {
					builder.append(args[i] + " ");
				}
				iService.get().rcon(builder.toString());
			} else if(args[2].equalsIgnoreCase("add")) {
				Optional<IVersion> version = ApplicationInterface.getAPI().getInfrastructure().getVersionById(args[3]);
				int startport = Integer.parseInt(args[4]);
				int maxRam = Integer.parseInt(args[5]);

				ConsoleOutput.write(ConsoleOutput.CYAN, "==========================================================================");
				ConsoleOutput.write(ConsoleOutput.CYAN, "Starting staticservercreation.");
				if (!version.isPresent()) {
					ConsoleOutput.write(ConsoleOutput.RED, "Version not downloaded or invalid. Maybe renamed?");
					ConsoleOutput.write(ConsoleOutput.RED, "Stopping task...");
					return;
				} else {
					ConsoleOutput.write(ConsoleOutput.GREEN, "Version by ID " + version.get().getVersion() + " found.");
				}
				ConsoleOutput.write(ConsoleOutput.GREEN, "RAM in MegaByte " + maxRam + " set.");
				ConsoleOutput.write(ConsoleOutput.GREEN, "Startingport " + startport + " set.");
				
				ApplicationInterface.getAPI().getInfrastructure().startSpigotServer("Static", version.get(), true, maxRam, startport, true);

				ConsoleOutput.write(ConsoleOutput.CYAN, "Starting ServerGroup!");
				ConsoleOutput.write(ConsoleOutput.CYAN, "==========================================================================");
			}
		}
	}

	@Override
	public String getHelp() {
		return "Manages Static-Server";
	}

}
