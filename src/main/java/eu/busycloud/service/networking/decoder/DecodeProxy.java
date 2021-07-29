package eu.busycloud.service.networking.decoder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;
import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.api.PlayerInfo;
import eu.busycloud.service.infrastructure.ProxyServer;

public class DecodeProxy implements DecodeListener {

	/**
	 * @since 0.1
	 * @author PlugDev
	 */
	@Override
	public void trigger(DecodeEvent event) {
		final String receiver = event.getData().read();
		if (receiver.equalsIgnoreCase("Proxy")) {
			final String title = event.getData().read();
			switch (title.toLowerCase()) {
			case "onenable()": {
				final String key = event.getData().read();
				ProxyServer proxy;
				(proxy = ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(key))
						.setConnection(event.getConnection());
				CloudInstance.LOGGER.info("ProxyChannel \"" + proxy.getProxyid() + "\" connected!");

				event.getConnection().sendData("pushInformations", "maxPlayers", getInfo("bungeeCord.maxPlayers"));
				event.getConnection().sendData("pushInformations", "motdPlayerInfo", getInfo("bungeeCord.motdPlayerInfo"));
				event.getConnection().sendData("pushInformations", "motdDescription", getInfo("bungeeCord.motdLine1") + "\\n" + getInfo("bungeeCord.motdLine2"));
				event.getConnection().sendData("pushInformations", "motdProtocol", getInfo("bungeeCord.motdProtocol"));
			}
				break;
			case "playerconnect": {
				String proxyKey = event.getData().read();
				String playername = event.getData().read();
				UUID playeruuid = UUID.fromString(event.getData().read());
				String conectedServer = event.getData().read();
				String address = event.getData().read();

				ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getOnlinePlayer()
						.add(new PlayerInfo(playername, playeruuid, conectedServer, address));

				CloudInstance.LOGGER.info("Player " + playername + " connected to "
						+ ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getProxyName()
						+ " - " + conectedServer);

				if (ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).isMaintenance()) {
					ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey)
							.sendRCON("kick " + playername + " §cThe server is currently in maintenance-mode.");
				}
			}
				break;
			case "playerdisconnect": {
				String proxyKey = event.getData().read();
				UUID playerUUID = UUID.fromString(event.getData().read());

				PlayerInfo info = null;

				for (PlayerInfo infos : ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey)
						.getOnlinePlayer())
					if (infos.getUniqueID().equals(playerUUID)) {
						info = infos;
						break;
					}

				if (info != null) {
					ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getOnlinePlayer()
							.remove(info);
					CloudInstance.LOGGER.info("Player " + info.getPlayername() + " disconnected from "
							+ ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getProxyName());
				}
				break;
			}
			case "playerswitchserver": {
				String proxyKey = event.getData().read();
				UUID playerUUID = UUID.fromString(event.getData().read());
				String from = event.getData().read();
				String to = event.getData().read();

				PlayerInfo info = null;

				for (PlayerInfo infos : ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey)
						.getOnlinePlayer())
					if (infos.getUniqueID().equals(playerUUID)) {
						info = infos;
						break;
					}

				if (info != null) {
					ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getOnlinePlayer()
							.remove(info);
					info.setConnectedServer(to);
					CloudInstance.LOGGER.info("Player " + info.getPlayername() + " switched from " + from + " to " + to
							+ " on "
							+ ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getProxyName());
				}
				break;
			}
			default:
				break;
			}
		}
	}

	JSONObject jsonObject;

	private String getInfo(String key) {
		try {
			if (jsonObject == null)
				jsonObject = new JSONObject(
						new String(Files.readAllBytes(new File("configurations/cloudconfig.json").toPath()), "UTF-8"));
		} catch (JSONException | IOException e) {
			e.printStackTrace();
		}

		return jsonObject.getString(key);
	}

}
