package de.plugdev.cloud.console.commands;

import de.plugdev.cloud.console.ConsoleColors;
import de.plugdev.cloud.console.ConsoleCommand;

public class CommandLicense extends ConsoleCommand {

	@Override
	public void runCommand(String command, String[] args) {
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] ======================================================================");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] ______                 _____ _                 _ ");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] | ___ \\               /  __ \\ |               | |");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] | |_/ /_   _ ___ _   _| /  \\/ | ___  _   _  __| |");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] | ___ \\ | | / __| | | | |   | |/ _ \\| | | |/ _` |");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] | |_/ / |_| \\__ \\ |_| | \\__/\\ | (_) | |_| | (_| |");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] \\____/ \\__,_|___/\\__, |\\____/_|\\___/ \\__,_|\\__,_|");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE]                   __/ |                          ");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE]                  |___/                           ");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] This project is developed by PlugDev and SumoKadaver.");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] This project is registered under \"Apache v2.0 GPL\" License");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] Free to use, but by Forking -> Add some credits..");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] Version: v1.01 ~ UnstableDev by PlugDev | Contributor: PlugDev");
		ConsoleColors.write(ConsoleColors.GREEN, "[CORE] ======================================================================");
	}

}
