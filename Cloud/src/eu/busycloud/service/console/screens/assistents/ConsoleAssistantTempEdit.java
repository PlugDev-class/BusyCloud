package eu.busycloud.service.console.screens.assistents;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.google.gson.JsonParser;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.console.ConsoleScreen;
import eu.busycloud.service.console.screens.ConsoleCloudDefault;
import eu.busycloud.service.infrastructure.ServerGroup;
import eu.busycloud.service.utils.CloudSetupContainer;
import eu.busycloud.service.utils.FileUtils;
import eu.busycloud.service.utils.ServerGroupContainer;
import eu.busycloud.service.utils.TextUtils;
import eu.busycloud.service.utils.CloudSetupContainer.AnswerType;

public class ConsoleAssistantTempEdit implements ConsoleScreen {

	public ConsoleAssistantTempEdit() {
		ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, true);
		ApplicationInterface.getAPI().getConsole().getQueueMap()
				.put(ApplicationInterface.getAPI().getConsole().getConsoleDefault(), false);

		TextUtils.sendFatLine();
		CloudInstance.LOGGER.info("(" + position + "/" + (cloudSetupContainers.length - 1) + ") "
				+ cloudSetupContainers[0].question + " [Type: " + cloudSetupContainers[0].answerType.toString()
				+ ", Optional: " + cloudSetupContainers[0].optional + "]");

	}

	private CloudSetupContainer[] cloudSetupContainers = {

			(new CloudSetupContainer("Which group do you want to edit?", "Groupname", AnswerType.STRING, false)),
			(new CloudSetupContainer("Which serversoftware should the group have", "ServerSoftware", AnswerType.STRING,
					true)),
			(new CloudSetupContainer("Which port should the group have?", "Port", AnswerType.INTEGER,
					true)),
			(new CloudSetupContainer("How much memory should a single server take? > Answer in MB", "RAM",
					AnswerType.INTEGER, true)),
			(new CloudSetupContainer("Please type in a percentrate between 30-100, to define when a new server start?",
					"Percentrate", AnswerType.INTEGER, true)),
			(new CloudSetupContainer("How much servers should start at beginning?", "Begin", AnswerType.INTEGER,
					true)),
			(new CloudSetupContainer("Is this a lobby-servergroup?", "Lobby", AnswerType.BOOLEAN, true)) };

	int position = 0;

	public void completeInstallation() throws IOException {
		CloudInstance.LOGGER.warning("Validating answers...");
		if(ApplicationInterface.getAPI().getInfrastructure().getGroupbyName((String) cloudSetupContainers[0].getAnswer()) == null) {
			CloudInstance.LOGGER.info((String) cloudSetupContainers[0].getAnswer() + " .. not found ~ ABORT");
			ApplicationInterface.getAPI().getConsole().getQueueMap().put(this, false);
			ApplicationInterface.getAPI().getConsole().getQueueMap().put(new ConsoleCloudDefault(), true);
			return;
		}
		CloudInstance.LOGGER.info((String) cloudSetupContainers[0].getAnswer() + " .. okay");
		
		ServerGroupContainer container = new ServerGroupContainer((String) cloudSetupContainers[0].getAnswer());
		if(cloudSetupContainers[1].getAnswer() != null)
			if(ApplicationInterface.getAPI().getInfrastructure().isValidVersion((String) cloudSetupContainers[1].getAnswer()))
				container.setServerSoftware((String) cloudSetupContainers[1].getAnswer());
		if(cloudSetupContainers[2].getAnswer() != null)
			container.setStartPort((int) cloudSetupContainers[2].getAnswer());
		if(cloudSetupContainers[3].getAnswer() != null)
			container.setMaxRamEachServer((int) cloudSetupContainers[3].getAnswer());
		if(cloudSetupContainers[4].getAnswer() != null)
			container.setStartNewServerByPercentage((int) cloudSetupContainers[4].getAnswer());
		if(cloudSetupContainers[5].getAnswer() != null)
			container.setStartServerByGroupstart((int) cloudSetupContainers[5].getAnswer());
		if(cloudSetupContainers[6].getAnswer() != null)
			container.setLobbyState((boolean) cloudSetupContainers[6].getAnswer());

		CloudInstance.LOGGER.warning("Writing to servergroups.json...");
		JSONObject jsonObject = new JSONObject(
				new String(Files.readAllBytes(Paths.get("configurations", "servergroups.json")), "UTF-8"));
		jsonObject.remove(container.getGroupName());
		Map<String, Object> serverMap = new LinkedHashMap<String, Object>();
		serverMap.put("serverSoftware", container.getServerSoftware().getVersionName());
		serverMap.put("startPort", container.getStartPort());
		serverMap.put("groupId", container.getGroupId());
		serverMap.put("maxRamEachServer", container.getMaxRamEachServer());
		serverMap.put("startServerByGroupstart", container.getStartServerByGroupstart());
		serverMap.put("startNewServerByPercentage", container.getStartNewServerByPercentage());
		serverMap.put("lobbyState", container.isLobbyState());
		jsonObject.put((String) cloudSetupContainers[0].getAnswer(), serverMap);
		FileUtils.writeFile(new File("configurations/servergroups.json"),
				TextUtils.GSON.toJson(JsonParser.parseString(jsonObject.toString())));
		CloudInstance.LOGGER.warning("Writing done.");
		CloudInstance.LOGGER.warning("Closing assistant...");
		TextUtils.sendFatLine();

		ApplicationInterface.getAPI().getInfrastructure().getGroupbyName(container.getGroupName()).stopServers();
		ApplicationInterface.getAPI().getInfrastructure().getRunningGroups().remove(
				ApplicationInterface.getAPI().getInfrastructure().getGroupbyName((String) cloudSetupContainers[0].getAnswer()));
		new ServerGroup(container);
		
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
			CloudInstance.LOGGER.info("We're gonna edit a servergroup with this pre-set:");
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
