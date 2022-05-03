package de.plugdev.cloud.internal.console.commands;

import java.util.Optional;
import java.util.UUID;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.console.ConsoleCommand;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.infrastructure.IService;
import de.plugdev.cloud.packets.PacketSharedPing;

public class CommandPingserver implements ConsoleCommand {
	
	@Override
	public void runCommand(String command, String[] args) {
		if(args.length != 2) {
			ConsoleOutput.write(ConsoleOutput.CYAN, "Wrong syntax. /ping <Name>");
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
		if(iService.get().getConnection() == null) {
			ConsoleOutput.write(ConsoleOutput.CYAN, "Service not linked!");
			return;
		}
		iService.get().getConnection().sendData(new PacketSharedPing(System.currentTimeMillis()));
		ConsoleOutput.write(ConsoleOutput.CYAN, "Sent pingrequest");
	}

	@Override
	public String getHelp() {
		return "Sends a simple ping to a service.";
	}

}
