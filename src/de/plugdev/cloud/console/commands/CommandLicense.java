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
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] This project is licensed to the following \"Apache v2.0 GPL\" License");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] IMPORTANT! This project will be licensed to the following ");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] \"The Open Software License 3.0 (OSL-3.0)\" License at 30.04.2021, 6pm CET | Changing License.");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] More informations? Questions? What will be changed? | /licenseupdate");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] Free to use, but by Forking -> Add some credits..");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] Version: v1.02 ~ Dev by PlugDev | Contributor: PlugDev");
		ConsoleOutput.write(ConsoleOutput.GREEN, "[CORE] ======================================================================");
	}

}
