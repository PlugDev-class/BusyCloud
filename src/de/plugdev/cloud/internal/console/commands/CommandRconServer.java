package de.plugdev.cloud.internal.console.commands;

import java.util.Optional;
import java.util.UUID;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.console.ConsoleCommand;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.infrastructure.IService;

public class CommandRconServer implements ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		
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
		
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = 2; i < args.length; i++) {
			stringBuilder.append(args[i] + " ");
		}
		iService.get().rcon(stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1));
		ConsoleOutput.write(ConsoleOutput.CYAN, "Sent rcon \"" + stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1) + "\" to service " + iService.get().getName());
	}

	@Override
	public String getHelp() {
		return "/rcon <serviceId> <command>";
	}

}
