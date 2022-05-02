package de.plugdev.cloud.internal.console.commands;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.internal.console.ConsoleCommand;
import de.plugdev.cloud.internal.console.ConsoleOutput;

public class CommandInstallSoftware implements ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		if (args.length == 2) {
			if (args[1].equalsIgnoreCase("list")) {
				ConsoleOutput.write(ConsoleOutput.CYAN, "Which version do you want to install?");
				ConsoleOutput.write(ConsoleOutput.CYAN, "Possible answerchoices:");
				
				ConsoleOutput.write(ConsoleOutput.CYAN, "<'1.7.10'>");
				ConsoleOutput.write(ConsoleOutput.CYAN, "<'1.8', '1.8.3', '1.8.4', '1.8.5', '1.8.6', '1.8.7', '1.8.8'*$>");
				ConsoleOutput.write(ConsoleOutput.CYAN, "<'1.9', '1.9.2'$, '1.9.4'*$>");
				ConsoleOutput.write(ConsoleOutput.CYAN, "<'1.10', '1.10.2'*>");
				ConsoleOutput.write(ConsoleOutput.CYAN, "<'1.11', '1.11.1', '1.11.2'*>");
				ConsoleOutput.write(ConsoleOutput.CYAN, "<'1.12', '1.12.1', '1.12.2'*>");
				ConsoleOutput.write(ConsoleOutput.CYAN, "<'1.13', '1.13.1', '1.13.2'*>");
				ConsoleOutput.write(ConsoleOutput.CYAN, "<'1.14', '1.14.1', '1.14.2', '1.14.3', '1.14.4'*>");
				ConsoleOutput.write(ConsoleOutput.CYAN, "<'1.15', '1.15.1', '1.15.2'*>");
				ConsoleOutput.write(ConsoleOutput.CYAN, "<'1.16.1', '1.16.2', '1.16.3', '1.16.4', '1.16.5'*>");
				
				ConsoleOutput.write(ConsoleOutput.GREEN, "Example: /install spigot 1.8.8");
				
				ConsoleOutput.write(ConsoleOutput.CYAN, "* means that paper is available too.");
				ConsoleOutput.write(ConsoleOutput.CYAN, "$ means that taco is available too.");
			}
		} else if (args.length == 3) {
			StringBuilder builder = new StringBuilder();
			builder.append(args[1]);
			builder.append("-");
			builder.append(args[2]);

			if (!ApplicationInterface.getAPI().getInfrastructure().isValidVersion(builder.toString())) {
				ConsoleOutput.write(ConsoleOutput.RED, "The specified version isn't valid.");
				return;
			}
			
			ApplicationInterface.getAPI().getInfrastructure().getVersionById(builder.toString()).get().download();
		}
	}

	@Override
	public String getHelp() {
		return "Install and show your versions. (VersionManager)";
	}

}
