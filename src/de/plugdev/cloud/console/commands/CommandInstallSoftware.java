package de.plugdev.cloud.console.commands;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.console.ConsoleOutput;
import de.plugdev.cloud.console.ConsoleCommand;

public class CommandInstallSoftware extends ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		if (args.length == 2) {
			if (args[1].equalsIgnoreCase("list")) {
				ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] What do you want to install?");
				ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] Possible answerchoices:");
				ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] <'1.7.10'>");
				ConsoleOutput.write(ConsoleOutput.CYAN,
						"[SETUP] <'1.8', '1.8.3', '1.8.4', '1.8.5', '1.8.6', '1.8.7', '1.8.8'*$>");
				ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] <'1.9', '1.9.2'$, '1.9.4'*$>");
				ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] <'1.10', '1.10.2'*>");
				ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] <'1.11', '1.11.1', '1.11.2'*>");
				ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] <'1.12', '1.12.1', '1.12.2'*>");
				ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] <'1.13', '1.13.1', '1.13.2'*>");
				ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] <'1.14', '1.14.1', '1.14.2', '1.14.3', '1.14.4'*>");
				ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] <'1.15', '1.15.1', '1.15.2'*>");
				ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] <'1.16.1', '1.16.2', '1.16.3', '1.16.4', '1.16.5'*>");
				ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] * means that paper is available too.");
				ConsoleOutput.write(ConsoleOutput.CYAN, "[SETUP] $ means that taco is available too.");
			}
		} else if (args.length == 3) {
			StringBuilder builder = new StringBuilder();
			builder.append(args[1]);
			builder.append("-");
			builder.append(args[2]);

			if (!ApplicationInterface.getAPI().getInfrastructure().isValidVersion(builder.toString())) {
				ConsoleOutput.write(ConsoleOutput.RED, "[PLUGIN] The specific Version isn't valid.");
				return;
			}
			
			ApplicationInterface.getAPI().getInfrastructure().getVersionById(builder.toString()).install();
		}
	}

}
