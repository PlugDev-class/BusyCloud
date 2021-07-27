package eu.busycloud.service.console.commands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleCommand;
import eu.busycloud.service.utils.TextUtils;

public class CommandHelp extends ConsoleCommand {

	public CommandHelp(String help) {
		super(help);
	}

	@Override
	public void runCommand(String command, String[] args) {

		TextUtils.sendPainHeader();
		CloudInstance.LOGGER.info("  _  _     _              _    _    _   ");
		CloudInstance.LOGGER.info(" | || |___| |_ __   ___  | |  (_)__| |_ ");
		CloudInstance.LOGGER.info(" | __ / -_) | '_ \\ |___| | |__| (_-<  _|");
		CloudInstance.LOGGER.info(" |_||_\\___|_| .__/       |____|_/__/\\__|");
		CloudInstance.LOGGER.info("            |_|                         ");
		TextUtils.sendLine();

		List<String> list = null;
		Collections.sort(list = new ArrayList<>(
				ApplicationInterface.getAPI().getConsole().getConsoleDefault().getCommandMap().keySet()));
		for (String commands : list) {
			CloudInstance.LOGGER.info(commands + "\t" + ApplicationInterface.getAPI().getConsole().getConsoleDefault()
					.getCommandMap().get(commands).getHelp());
			TextUtils.sendLine();
		}
	}

}
