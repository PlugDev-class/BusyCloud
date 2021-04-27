package de.plugdev.cloud.console.commands;

import de.plugdev.cloud.console.ConsoleOutput;
import de.plugdev.cloud.console.ConsoleCommand;

public class CommandLicense extends ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] ======================================================================");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] ______                 _____ _                 _ ");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] | ___ \\               /  __ \\ |               | |");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] | |_/ /_   _ ___ _   _| /  \\/ | ___  _   _  __| |");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] | ___ \\ | | / __| | | | |   | |/ _ \\| | | |/ _` |");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] | |_/ / |_| \\__ \\ |_| | \\__/\\ | (_) | |_| | (_| |");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] \\____/ \\__,_|___/\\__, |\\____/_|\\___/ \\__,_|\\__,_|");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE]                   __/ |                          ");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE]                  |___/                           ");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] This project is developed by PlugDev and SumoKadaver.");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] Developer: PlugDev (Dennis B.)");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] Content: SumoKadaver (Tristan J.)");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] This project is licensed to the following License \"Apache v2.0 GPL\"");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] Version: v1.03 ~ Dev");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] ======================================================================");
	}

}
