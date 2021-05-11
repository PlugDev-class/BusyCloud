package de.plugdev.cloud.internal.networking.receive;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.external.PlayerInfo;
import de.plugdev.cloud.external.permissions.utils.PermissionsGroup;
import de.plugdev.cloud.external.permissions.utils.PermissionsUser;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.infrastructure.Proxy;
import de.plugdev.cloud.internal.infrastructure.SpigotServer;
import de.terrarier.netlistening.api.event.DecodeEvent;
import de.terrarier.netlistening.api.event.DecodeListener;

public class DecodeProxy implements DecodeListener {

	/*
	 * @since 0.1
	 * 
	 * @author PlugDev
	 */
	@Override
	public void trigger(DecodeEvent event) {
		final String receiver = event.getData().read();
		if (receiver.equalsIgnoreCase("Proxy")) {
			final String title = event.getData().read();
			switch (title.toLowerCase()) {
			case "linkproxy": {
				final String key = event.getData().read();
				Proxy proxy;
				(proxy = ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(key))
						.setConnection(event.getConnection());
				ConsoleOutput.write(ConsoleOutput.GREEN,
						"[NETWORKING] ProxyChannel \"" + proxy.getProxyid() + "\" connected!");

				event.getConnection().sendData("changebungeeinfo", "change#motd",
						"§9BusyCloud §c| §dCloud is loading...");
			}
				break;
			case "playerconnect": {
				String proxyKey = event.getData().read();
				String playername = event.getData().read();
				UUID playeruuid = event.getData().read();
				String conectedServer = event.getData().read();
				String address = event.getData().read();

				ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getOnlinePlayer()
						.add(new PlayerInfo(playername, playeruuid, conectedServer, address));

				ConsoleOutput.write(ConsoleOutput.YELLOW, "[CORE] Player " + playername + " connected to "
						+ ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getProxyName()
						+ " - " + conectedServer);

				try {
					PermissionsUser permissionsUser = null;
					List<String> lines = Files.readAllLines(new File("local/permissions/users.pdv").toPath());
					for (String line : lines) {
						if (!line.startsWith("#")) {
							String[] array = line.split(" \\| ");
							if (playeruuid.toString().equalsIgnoreCase(array[0])) {
								permissionsUser = new PermissionsUser();
								permissionsUser.setPlayerName(playername);
								permissionsUser.setUuid(playeruuid);
								permissionsUser.setUserHeigt(0);
								permissionsUser.setCurrentGroup(ApplicationInterface.getAPI().getPermissionsSystem()
										.getGroupByID(Integer.parseInt(array[1])));
								ApplicationInterface.getAPI().getPermissionsSystem().getUsers().add(permissionsUser);
								break;
							}
						}
					}

					if (permissionsUser == null) {
						BufferedWriter writer = new BufferedWriter(new FileWriter("local/permissions/users.pdv", true));
						writer.append(playeruuid.toString() + "\n");
						writer.close();

						PermissionsGroup prefferedGroup = null;
						for (PermissionsGroup group : ApplicationInterface.getAPI().getPermissionsSystem()
								.getGroups()) {
							if (prefferedGroup == null) {
								prefferedGroup = group;
							}
							if (prefferedGroup.getGroupHeight() > group.getGroupHeight()) {
								prefferedGroup = group;
							}
						}

						permissionsUser = new PermissionsUser();
						permissionsUser.setPlayerName(playername);
						permissionsUser.setUuid(playeruuid);
						permissionsUser.setUserHeigt(0);
						permissionsUser.setCurrentGroup(prefferedGroup);
						ApplicationInterface.getAPI().getPermissionsSystem().getUsers().add(permissionsUser);
					}

					for (SpigotServer server : ApplicationInterface.getAPI().getInfrastructure().getRunningServers()) {
						server.getConnection().sendData("adduser", 
								permissionsUser.getCurrentGroup().getGroupId(),
								permissionsUser.getUuid(),
								permissionsUser.getPlayerName(),
								permissionsUser.getUserHeigt());
					}
					for (Proxy proxy : ApplicationInterface.getAPI().getInfrastructure().getRunningProxies()) {
						proxy.getConnection().sendData("adduser", 
								permissionsUser.getCurrentGroup().getGroupId(),
								permissionsUser.getUuid(),
								permissionsUser.getPlayerName(),
								permissionsUser.getUserHeigt());
					}
				} catch (IOException exception) {
					exception.printStackTrace();
				}

				if (ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).isMaintenance()) {
					ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey)
							.sendRCON("kick " + playername + " §cThe server is currently in maintenance-mode.");
				}
			}
				break;
			case "playerdisconnect": {
				String proxyKey = event.getData().read();
				UUID playerUUID = event.getData().read();

				PlayerInfo info = null;

				for (PlayerInfo infos : ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey)
						.getOnlinePlayer()) {
					if (infos.getUniqueID().equals(playerUUID)) {
						info = infos;
						break;
					}
				}

				if (info != null) {
					ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getOnlinePlayer().remove(info);
					ConsoleOutput.write(ConsoleOutput.YELLOW, "[CORE] Player " + info.getPlayername()
							+ " disconnected from "
							+ ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getProxyName());
					for (SpigotServer server : ApplicationInterface.getAPI().getInfrastructure().getRunningServers()) {
						server.getConnection().sendData("remuser", ApplicationInterface.getAPI().getPermissionsSystem().getUserByUUID(info.getUniqueID()));
					}
					for (Proxy proxy : ApplicationInterface.getAPI().getInfrastructure().getRunningProxies()) {
						proxy.getConnection().sendData("remuser", ApplicationInterface.getAPI().getPermissionsSystem().getUserByUUID(info.getUniqueID()));
					}
				}
				break;
			}
			case "playerswitchserver": {
				String proxyKey = event.getData().read();
				UUID playerUUID = event.getData().read();
				String from = event.getData().read();
				String to = event.getData().read();

				PlayerInfo info = null;

				for (PlayerInfo infos : ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey)
						.getOnlinePlayer()) {
					if (infos.getUniqueID().equals(playerUUID)) {
						info = infos;
						break;
					}
				}

				if (info != null) {
					ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getOnlinePlayer()
							.remove(info);
					info.setConnectedServer(to);
					ConsoleOutput.write(ConsoleOutput.YELLOW, "[CORE] Player " + info.getPlayername()
							+ " switched from " + from + " to " + to + " on "
							+ ApplicationInterface.getAPI().getInfrastructure().getProxyByKey(proxyKey).getProxyName());
				}
				break;
			}
			default:
				break;
			}
		}
	}

}
