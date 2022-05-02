package de.plugdev.cloud.internal.console.commands;

import java.text.SimpleDateFormat;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.console.ConsoleCommand;
import de.plugdev.cloud.internal.console.ConsoleOutput;

public class CommandList implements ConsoleCommand {


	SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy - HH_mm");
	
	@Override
	public void runCommand(String command, String[] args) {
		ApplicationInterface.getAPI().getInfrastructure().getServices().forEach((service) -> {
			ConsoleOutput.write(ConsoleOutput.GREEN, "Service: " + service.getName() + " | " + service.getUniqueId() + " | " + service.getPort());
		});
	}
	
	

	@Override
	public String getHelp() {
		return "Display every service with following format: \"Service: <SERVICE-NAME> | <SERVICE-ID> | <SERVICE-PORT>";
	}

}
