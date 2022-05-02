package de.plugdev.cloud.internal.console.commands;

import de.plugdev.cloud.internal.console.ConsoleCommand;
import de.plugdev.cloud.internal.console.ConsoleOutput;

public class CommandVersion implements ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		ConsoleOutput.write(ConsoleOutput.GREEN, "======================================================================");
		ConsoleOutput.write(ConsoleOutput.GREEN, "______                 _____ _                 _ ");
		ConsoleOutput.write(ConsoleOutput.GREEN, "| ___ \\               /  __ \\ |               | |");
		ConsoleOutput.write(ConsoleOutput.GREEN, "| |_/ /_   _ ___ _   _| /  \\/ | ___  _   _  __| |");
		ConsoleOutput.write(ConsoleOutput.GREEN, "| ___ \\ | | / __| | | | |   | |/ _ \\| | | |/ _` |");
		ConsoleOutput.write(ConsoleOutput.GREEN, "| |_/ / |_| \\__ \\ |_| | \\__/\\ | (_) | |_| | (_| |");
		ConsoleOutput.write(ConsoleOutput.GREEN, "\\____/ \\__,_|___/\\__, |\\____/_|\\___/ \\__,_|\\__,_|");
		ConsoleOutput.write(ConsoleOutput.GREEN, "                  __/ |                          ");
		ConsoleOutput.write(ConsoleOutput.GREEN, "                 |___/                           ");
		ConsoleOutput.write(ConsoleOutput.GREEN, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		ConsoleOutput.write(ConsoleOutput.GREEN, "This project is developed by PlugDev and SumoKadaver.");
		ConsoleOutput.write(ConsoleOutput.GREEN, "Developer: PlugDev (Dennis B.)");
		ConsoleOutput.write(ConsoleOutput.GREEN, "Content: SumoKadaver (Tristan J.)");
		ConsoleOutput.write(ConsoleOutput.GREEN, "This project is licensed to the following License \"Apache v2.0 GPL\"");
		ConsoleOutput.write(ConsoleOutput.GREEN, "~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		ConsoleOutput.write(ConsoleOutput.GREEN, "Version: (FORK v1.03) v1.0 ~ Dev");
		ConsoleOutput.write(ConsoleOutput.GREEN, "======================================================================");
	}

	@Override
	public String getHelp() {
		return "Shows the version of BusyCloud.";
	}

}
