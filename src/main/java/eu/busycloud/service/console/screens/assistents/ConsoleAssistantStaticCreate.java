package eu.busycloud.service.console.screens.assistents;

import java.io.IOException;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleScreen;
import eu.busycloud.service.console.screens.ConsoleCloudDefault;
import eu.busycloud.service.utils.CloudSetupContainer;
import eu.busycloud.service.utils.CloudSetupContainer.AnswerType;
import eu.busycloud.service.utils.TextUtils;

public class ConsoleAssistantStaticCreate implements ConsoleScreen {

	public ConsoleAssistantStaticCreate() {
		ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, true);
		ApplicationInterface.getAPI().getConsole().getQueueMap()
				.put(ApplicationInterface.getAPI().getConsole().getConsoleDefault(), false);

		TextUtils.sendFatLine();
		CloudInstance.LOGGER.info("(" + position + "/" + (cloudSetupContainers.length - 1) + ") "
				+ cloudSetupContainers[0].question + " [Type: " + cloudSetupContainers[0].answerType.toString()
				+ ", Optional: " + cloudSetupContainers[0].optional + "]");

	}

	private CloudSetupContainer[] cloudSetupContainers = {
			(new CloudSetupContainer("Which name should your server have?", "StaticServer-Name", AnswerType.STRING, false)),
			(new CloudSetupContainer("Which serversoftware should your server run?", "StaticServer-ServerSoftware", AnswerType.STRING, false)),
			(new CloudSetupContainer("At which port should your server start? 0-65535", "Port", AnswerType.INTEGER, false)),
			(new CloudSetupContainer("How much memory should your server server get? > Answer in MB", "RAM", AnswerType.INTEGER, false))  };

	int position = 0;

	@SuppressWarnings("null")
	public void completeInstallation() throws IOException {
		CloudInstance.LOGGER.warning("Validating answers...");
		CloudInstance.LOGGER.info(cloudSetupContainers[0].getAnswer() + " .. okay");
		if (ApplicationInterface.getAPI().getInfrastructure()
				.isValidVersion(((String) cloudSetupContainers[1].getAnswer()))) {
			if (!ApplicationInterface.getAPI().getInfrastructure()
					.getVersionById(((String) cloudSetupContainers[1].getAnswer())).isAvailable())
				ApplicationInterface.getAPI().getInfrastructure()
						.getVersionById((String) cloudSetupContainers[1].getAnswer()).download();
			CloudInstance.LOGGER.info(cloudSetupContainers[1].getAnswer() + " .. okay");
		} else {
			CloudInstance.LOGGER.info(cloudSetupContainers[1].getAnswer() + " .. doesn't exists! ~ ABORT");
			ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, false);
			ApplicationInterface.getAPI().getConsole().getQueueMap().put(new ConsoleCloudDefault(), true);
			return;
		}
		int port = (int) cloudSetupContainers[2].getAnswer();
		if (!(port < 0 && port > 65535)) {
			CloudInstance.LOGGER.info(port + " .. okay");
		} else {
			CloudInstance.LOGGER.info(port + " .. out of range! ~ ABORT");
			CloudInstance.LOGGER.info("Be sure to set a port between 0 and 65535.");
			ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, false);
			ApplicationInterface.getAPI().getConsole().getQueueMap().put(new ConsoleCloudDefault(), true);
			return;
		}
		int ram = (int) cloudSetupContainers[3].getAnswer();
		if (ram < Runtime.getRuntime().maxMemory()) {
			CloudInstance.LOGGER.info(ram + "M .. okay");
		} else {
			CloudInstance.LOGGER.info(ram + "M .. you don't have that much ram! ~ ABORT");
			CloudInstance.LOGGER.info("Be sure you started your cloud with max-ram parameter, like:");
			CloudInstance.LOGGER.info("\tusage: java -jar -Xmx\"MAX-RAM\"M BusyCloud.jar");
			CloudInstance.LOGGER.info("\tex: java -jar -Xmx4096M BusyCloud.jar");
			CloudInstance.LOGGER.info("We need to know that, to calculate your total memory of your system.");
			ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, false);
			return;
		}
		CloudInstance.LOGGER.warning("Starting server...");
		ApplicationInterface.getAPI().getInfrastructure().startStaticSpigotServer((String) cloudSetupContainers[0].getAnswer(), 
				ApplicationInterface.getAPI().getInfrastructure().getVersionById((String) cloudSetupContainers[1].getAnswer()), (Integer) null, ram, true, port);
		CloudInstance.LOGGER.warning("Closing assistant...");
		TextUtils.sendFatLine();

		ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, false);
		ApplicationInterface.getAPI().getConsole().getQueueMap()
				.put(ApplicationInterface.getAPI().getConsole().getConsoleDefault(), true);

	}

	@Override
	public void scanLine(String input) {
		CloudSetupContainer question = cloudSetupContainers[position];
		if (!question.validateAnswer(input)) {
			CloudInstance.LOGGER.info("We couldn't recognize your input, please check the answertype.");
			return;
		}
		if (cloudSetupContainers.length == position + 1) {

			TextUtils.sendFatLine();
			CloudInstance.LOGGER.info("We're gonna create a staticserver with this pre-set:");
			for (CloudSetupContainer container : cloudSetupContainers)
				CloudInstance.LOGGER.info(container.shortQuestion + ": " + container.answer);
			TextUtils.sendFatLine();

			try {
				completeInstallation();
				return;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		CloudSetupContainer container = cloudSetupContainers[position += 1];
		CloudInstance.LOGGER.info("(" + position + "/" + (cloudSetupContainers.length - 1) + ") " + container.question
				+ " [Type: " + container.answerType.toString() + ", Optional: " + container.optional + "]");
	}

}
