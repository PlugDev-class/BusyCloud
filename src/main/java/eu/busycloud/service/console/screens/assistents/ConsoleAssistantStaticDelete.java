package eu.busycloud.service.console.screens.assistents;

import java.io.IOException;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleScreen;
import eu.busycloud.service.console.screens.ConsoleCloudDefault;
import eu.busycloud.service.utils.CloudSetupContainer;
import eu.busycloud.service.utils.CloudSetupContainer.AnswerType;
import eu.busycloud.service.utils.FileUtils;
import eu.busycloud.service.utils.TextUtils;

public class ConsoleAssistantStaticDelete implements ConsoleScreen {

	public ConsoleAssistantStaticDelete() {
		ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, true);
		ApplicationInterface.getAPI().getConsole().getQueueMap()
				.put(ApplicationInterface.getAPI().getConsole().getConsoleDefault(), false);

		TextUtils.sendFatLine();
		CloudInstance.LOGGER.info("(" + position + "/" + (cloudSetupContainers.length - 1) + ") "
				+ cloudSetupContainers[0].question + " [Type: " + cloudSetupContainers[0].answerType.toString()
				+ ", Optional: " + cloudSetupContainers[0].optional + "]");

	}

	private CloudSetupContainer[] cloudSetupContainers = {
			(new CloudSetupContainer("What's the name of the staticerver?", "StaticServer-Name", AnswerType.STRING,
					false)) };

	int position = 0;

	public void completeInstallation() throws IOException {
		CloudInstance.LOGGER.warning("Validating answers...");
		if (ApplicationInterface.getAPI().getInfrastructure()
				.getServerByName((String) cloudSetupContainers[0].getAnswer()) == null) {
			CloudInstance.LOGGER.info((String) cloudSetupContainers[0].getAnswer() + " .. not found ~ ABORT");
			ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, false);
			ApplicationInterface.getAPI().getConsole().getQueueMap().put(new ConsoleCloudDefault(), true);
			return;
		}
		if (!ApplicationInterface.getAPI().getInfrastructure()
				.getServerByName((String) cloudSetupContainers[0].getAnswer()).isStatic()) {
			CloudInstance.LOGGER.info((String) cloudSetupContainers[0].getAnswer() + " .. not static ~ ABORT");
			ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, false);
			ApplicationInterface.getAPI().getConsole().getQueueMap().put(new ConsoleCloudDefault(), true);
			return;
		}
		CloudInstance.LOGGER.info((String) cloudSetupContainers[0].getAnswer() + " .. okay");
		CloudInstance.LOGGER.warning("Stopping instance...");
		ApplicationInterface.getAPI().getInfrastructure()
				.getServerByName((String) cloudSetupContainers[0].getAnswer()).stopServer();
		ApplicationInterface.getAPI().getInfrastructure().getRunningServers().remove(ApplicationInterface.getAPI()
				.getInfrastructure().getServerByName((String) cloudSetupContainers[0].getAnswer()));
		CloudInstance.LOGGER.warning("Deleting files...");
		FileUtils.deleteFolderRecursivly("server/static/" + (String) cloudSetupContainers[0].getAnswer());
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
			CloudInstance.LOGGER.info("We're gonna delete a staticserver with this pre-set:");
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
