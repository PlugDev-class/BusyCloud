package eu.busycloud.service.networking.decoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;
import de.terrarier.netlistening.internal.AssumeNotNull;
import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.api.PlayerInfo;
import eu.busycloud.service.infrastructure.ProxyServer;

public class DecodeProxyServer implements DecodeListener {

	@Override
	public void trigger(@AssumeNotNull DecodeEvent event) {
		final String receiver = event.getData().read();
		if (receiver.equalsIgnoreCase("Proxy")) {
			final String title = event.getData().read();
			switch (title.toLowerCase()) {
			case "onenable()":
				final String keyEnable = event.getData().read();
				final ProxyServer preferredProxyServer = ApplicationInterface.getAPI().getInfrastructure()
						.getProxyByKey(keyEnable);
				preferredProxyServer.setConnection(event.getConnection());
				CloudInstance.LOGGER.info("ProxyChannel \"" + preferredProxyServer.getProxyName() + "\" connected");
				preferredProxyServer.getConnection().sendData("pushInformations", "maxPlayers", getInfo("maxPlayers"));

				final JSONObject jsonObject = (JSONObject) getInfo("motdSettings");
				final String motdHeader = jsonObject.getString("motdLine1");
				final String motdFooter = jsonObject.getString("motdLine2");
				final String motdProtocol = jsonObject.getString("motdProtocol");
				final JSONArray playerInfoArray = jsonObject.getJSONArray("motdPlayerInfo");

				preferredProxyServer.getConnection().sendData("pushInformations", "motdHeader", motdHeader);
				preferredProxyServer.getConnection().sendData("pushInformations", "motdFooter", motdFooter);
				preferredProxyServer.getConnection().sendData("pushInformations", "motdProtocol", motdProtocol);
				playerInfoArray.forEach((object) -> {
					preferredProxyServer.getConnection().sendData("pushInformations", "motdPlayerInfo", (String) object);
				});
				break;
			case "ondisable()":
				final ProxyServer proxyServer = ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(event.getData().read());
				proxyServer.getOnlinePlayers().forEach((playerInfo5) -> {
					CloudInstance.LOGGER.info("'" + playerInfo5.getPlayername() + "' disconnected from '"
							+ playerInfo5.getConnectedServer() + "' (" + playerInfo5.getAddress() + ") due shutdown.");
				});
				break;
			case "playerconnect":
				final String keyConnect = event.getData().read();
				final ProxyServer proxyServer5 = ApplicationInterface.getAPI().getInfrastructure()
						.getProxyByKey(keyConnect);
				final String playerName = event.getData().read();
				final UUID playerUUID = UUID.fromString((String) event.getData().read());
				final String targetServer = event.getData().read();
				final String address = event.getData().read();
				final PlayerInfo playerInfo = new PlayerInfo(playerName, playerUUID, targetServer, address);
				proxyServer5.getOnlinePlayers().add(playerInfo);
				CloudInstance.LOGGER.info(
						"'" + playerInfo.getPlayername() + "' connected to '" + targetServer + "' (" + address + ")");
				break;
			case "playerdisconnect":
				final String keyDisconnect = event.getData().read();
				final ProxyServer proxyServer2 = ApplicationInterface.getAPI().getInfrastructure()
						.getProxyByKey(keyDisconnect);
				final UUID uuid = UUID.fromString((String) event.getData().read());
				final PlayerInfo playerInfo2 = ApplicationInterface.getAPI().getInfrastructure().getPlayer(uuid);
				if (playerInfo2 != null) {
					CloudInstance.LOGGER.info("'" + playerInfo2.getPlayername() + "' disconnected from '"
							+ playerInfo2.getConnectedServer() + "' (" + playerInfo2.getAddress() + ")");
				} else {
					CloudInstance.LOGGER
							.info("'Unknown' disconnected from '" + proxyServer2.getProxyName() + "' (0.0.0.0:0)");
				}
				break;
			case "playerswitchserver":
				final String keySwitch = event.getData().read();
				final ProxyServer proxyServer3 = ApplicationInterface.getAPI().getInfrastructure()
						.getProxyByKey(keySwitch);
				final UUID playerUUID2 = UUID.fromString((String) event.getData().read());
				final String connectedTo = event.getData().read();
				final PlayerInfo playerInfo3 = ApplicationInterface.getAPI().getInfrastructure().getPlayer(playerUUID2);
				if (playerInfo3 != null) {
					CloudInstance.LOGGER.info(
							"'" + playerInfo3.getPlayername() + "' switched from '" + playerInfo3.getConnectedServer()
									+ "' to '" + connectedTo + "' on '" + proxyServer3.getProxyName() + "'");
				}

				break;
			default:
				break;
			}

		}
	}

	JSONObject jsonObject;

	private Object getInfo(String key) {
		try {
			if (jsonObject == null)
				jsonObject = new JSONObject(
						new String(Files.readAllBytes(new File("configurations/cloudconfig.json").toPath()), "UTF-8"));
			JSONObject bungeeObject = jsonObject.getJSONObject("bungeeCord");
			return bungeeObject.get(key);
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

		return null;
	}

}
