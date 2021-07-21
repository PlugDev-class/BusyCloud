package eu.busycloud.service.console.screens;

import java.io.File;
import java.io.IOException;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleScreen;
import eu.busycloud.service.infrastructure.Boot;
import eu.busycloud.service.utils.CloudSetupContainer;
import eu.busycloud.service.utils.CloudSetupContainer.AnswerType;

public class ConsoleSetup implements ConsoleScreen {

	private CloudSetupContainer[] cloudSetupContainers = {
		
			(new CloudSetupContainer("Are you accepting our current guidelines?", "Guidlines-Accept", AnswerType.BOOLEAN, false)),
			(new CloudSetupContainer("Are you accepting any other by BusyCloud used guidelines?", "TP-Accept", AnswerType.BOOLEAN, false)),
			(new CloudSetupContainer("Please type in your License-Key:", "License-Key", AnswerType.STRING, true)),
			(new CloudSetupContainer("If you are permitted to, do you want to use the latest ViaVersion?", "ViaVersion", AnswerType.BOOLEAN, false)),
			(new CloudSetupContainer("How do you want to name your network?", "Network-Name", AnswerType.STRING, false)),
			(new CloudSetupContainer("Expert: How much percent of cpu you want to use for cloudplugins?", "CPU/Plugin", AnswerType.INTEGER, true)),
			(new CloudSetupContainer("Expert: How much percent of ram you want to use for cloudplugins?", "RAM/Plugin", AnswerType.INTEGER, true)),
			(new CloudSetupContainer("Expert: How much percent of cpu you want to use for cloudplayers?", "CPU/Player", AnswerType.INTEGER, true)),
			(new CloudSetupContainer("Expert: How much percent of ram you want to use for cloudplayers?", "RAM/Player", AnswerType.INTEGER, true)),
			(new CloudSetupContainer("Expert: Do you want to use nibble-compression-networking?", "Nibble-Networking", AnswerType.BOOLEAN, true)),
			(new CloudSetupContainer("Soon: Do you want to do the servers cross-compatible? (Java <-> Bedrock)", "Java <=> Bedrock", AnswerType.BOOLEAN, false)),
			(new CloudSetupContainer("Last question: did you answer this questions truthfully and you're sure 'bout it?", "Everythings true", AnswerType.BOOLEAN, false))
			
	};
	
	int position = 0;

	public ConsoleSetup() throws IOException {
		CloudInstance.LOGGER.info("CloudSetup «~» Skip optional questions with: 'skip'");
		CloudSetupContainer container = cloudSetupContainers[0];
		CloudInstance.LOGGER
				.info("CloudSetup «~» (" + position + "/" + (cloudSetupContainers.length-1) + ") " + container.question
						+ " [Type: " + container.answerType.toString() + ", Optional: " + container.optional + "]");
	}

	public void completeInstallation() throws IOException {
		CloudInstance.LOGGER.warning("CloudSetup «~» (Re/Force)installing Cloud, please wait a moment...");
		CloudInstance.LOGGER.warning("CloudSetup «~» (Re/Force)installing Cloud: iterating through some files...");
		for (File file : new File[] { 
				new File("configurations"), 
				new File("developer"),
				new File("saves"),
				new File("configurations/cloudconfig.json"),
				new File("configurations/expertconfig.json"), 
				new File("configurations/language.json"),
				new File("configurations/servergroups.json"), 
				new File("developer/logs"),
				new File("developer/-= ..only for experts.. =-"), 
				new File("saves/backups"),
				new File("saves/environment-plugins"), 
				new File("saves/environments"), 
				new File("saves/templates") }) {
			if (file.getName().endsWith(".json") || file.getName().endsWith(".check")) {
				file.createNewFile();
			} else {
				file.mkdir();
			}
		}
		CloudInstance.LOGGER.warning("CloudSetup «~» (Re/Force)installing Cloud, finished");
		CloudInstance.LOGGER.info("Install software with: /install");

		new Boot((String) cloudSetupContainers[4].getAnswer(), null, null, (boolean) cloudSetupContainers[3].getAnswer());
		
		ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, false);
		ApplicationInterface.getAPI().getConsole().getQueueMap().put(new ConsoleDefault(), true);
	}

	@Override
	public void scanLine(String input) {
		CloudSetupContainer question = cloudSetupContainers[position];
		if (!question.validateAnswer(input)) {
			CloudInstance.LOGGER.info("CloudSetup «~» We couldn't recognize your input, please check the answertype.");
			return;
		}
		if (cloudSetupContainers.length == position+1) {

			CloudInstance.LOGGER.info("==========================================");
			CloudInstance.LOGGER.info("We're going to install BusyCloud with this pre-set:");
			for(CloudSetupContainer container : cloudSetupContainers)
				CloudInstance.LOGGER.info(container.shortQuestion + ": " + container.answer);
			CloudInstance.LOGGER.info("==========================================");
			
			try {
				completeInstallation();
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		CloudSetupContainer container = cloudSetupContainers[position+=1];
		CloudInstance.LOGGER
				.info("CloudSetup «~» (" + position + "/" + (cloudSetupContainers.length-1) + ") " + container.question
						+ " [Type: " + container.answerType.toString() + ", Optional: " + container.optional + "]");
	}
	
}
