package eu.busycloud.service.console.commands;

import java.util.ArrayList;
import java.util.List;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleCommand;
import eu.busycloud.service.infrastructure.ServerSoftware;
import eu.busycloud.service.infrastructure.ServerSoftware.ServerSoftwareType;

public class CommandInstallSoftware extends ConsoleCommand {

	public CommandInstallSoftware(String help) {
		super(help);
	}
	
	@Override
	public void runCommand(String command, String[] args) {
		if (args.length == 2) {
			switch (args[1].toLowerCase()) {
			case "storelist":
				List<StringBuilder> listBuilder = new ArrayList<StringBuilder>();
				ServerSoftwareType currentType = null;
				int objectsInList = 0;
				for(ServerSoftware serverSoftware : ApplicationInterface.getAPI().getInfrastructure().serverSoftwares) {
					if(currentType == null || currentType != serverSoftware.getType()) {
						currentType = serverSoftware.getType();
						objectsInList = 0;
						listBuilder.add(new StringBuilder());
						listBuilder.get(listBuilder.size()-1).append("<------------------------------------------> " + serverSoftware.getType().toString().toUpperCase() + " <------------------------------------------>");
						listBuilder.add(new StringBuilder());
					}
					if(objectsInList > 7) {
						objectsInList = 0;
						listBuilder.add(new StringBuilder());
					}
					listBuilder.get(listBuilder.size()-1).append("\"" + serverSoftware.getVersionName() + "\" | ");
					++objectsInList;
				}
				for(StringBuilder stringBuilder : listBuilder) {
					CloudInstance.LOGGER.info(stringBuilder.toString());
				}
				listBuilder.clear();
				break;
			default:
				if (!ApplicationInterface.getAPI().getInfrastructure().isValidSoftware(args[1])) {
					CloudInstance.LOGGER.warning("The specific version isn't valid.");
					return;
				}
				if(ApplicationInterface.getAPI().getInfrastructure().getSoftwareById(args[1]).isAvailable()) {
					CloudInstance.LOGGER.warning("Software already installed. Lookup: /install info " + args[1]);
					return;
				}

				System.out.print("Start download of " + args[1] + "");
				ApplicationInterface.getAPI().getInfrastructure().getSoftwareById(args[1]).download();
				System.out.print(" .. done\n");
				break;
			}
		} else if (args.length == 3) {
			if(args[1].equalsIgnoreCase("info")) {
				if (!ApplicationInterface.getAPI().getInfrastructure().isValidSoftware(args[2])) {
					CloudInstance.LOGGER.info("The specific version isn't valid.");
					return;
				}
				ServerSoftware serverSoftware = ApplicationInterface.getAPI().getInfrastructure().getSoftwareById(args[2]);
				CloudInstance.LOGGER.info("Softwarename: " + serverSoftware.getVersionName());
				CloudInstance.LOGGER.info("Softwareauthor: " + serverSoftware.getAuthor());
				CloudInstance.LOGGER.info("Software installed: " + serverSoftware.isAvailable());
				CloudInstance.LOGGER.info("Softwaretype: " + serverSoftware.getType().toString().toUpperCase());
			}
		} else {
			CloudInstance.LOGGER.info("/install storelist");
			CloudInstance.LOGGER.info("/install update");
			CloudInstance.LOGGER.info("/install info <Version>");
			CloudInstance.LOGGER.info("/install <Version>");
		}
	}

}
