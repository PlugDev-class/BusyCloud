package de.plugdev.cloud.console.commands;

import de.plugdev.cloud.console.ConsoleCommand;
import de.plugdev.cloud.console.ConsoleOutput;

public class CommandLicenseupdate extends ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		if(args.length == 1) {
			ConsoleOutput.write(ConsoleOutput.GREEN, "-> License Change <-");
			ConsoleOutput.write(ConsoleOutput.GREEN, "1. First of all, thank you for reading and attentioning it.");
			ConsoleOutput.write(ConsoleOutput.GREEN, "1.1. This license changing is not needed because of some forks, that");
			ConsoleOutput.write(ConsoleOutput.GREEN, "I don't want to exists. It's a little safety to not abuse my project");
			ConsoleOutput.write(ConsoleOutput.GREEN, "for some ideas, that I heard by some peoples.");
			ConsoleOutput.write(ConsoleOutput.GREEN, "1.2. You are obliged to accept the licensechange. If you don't please");
			ConsoleOutput.write(ConsoleOutput.GREEN, "contact me at admin@peaxemc.net and we'll search for a solution.");
			ConsoleOutput.write(ConsoleOutput.GREEN, "1.3. The licenserights are at > https://opensource.org/licenses/OSL-3.0");
			ConsoleOutput.write(ConsoleOutput.GREEN, "1.4. If you're uploading some stuff from my project, please don't forget");
			ConsoleOutput.write(ConsoleOutput.GREEN, "to accepting and crediting my work.");
			return;
		}
	}
	
}
