package de.plugdev.cloud.infrastructure.generate;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import de.plugdev.cloud.api.ApplicationInterface;
import de.plugdev.cloud.infrastructure.MinecraftVersion;
import de.plugdev.cloud.infrastructure.Proxy;
import de.plugdev.cloud.infrastructure.SpigotServer;

public class ServerGenerator {

	private String path = null;

	public ServerGenerator(MinecraftVersion minecraftVersion, SpigotServer spigotServer) {
		path = "server/" + (spigotServer.isStatic() ? "static" : "temp") + "/" + spigotServer.getServerName();
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdir();
		}
		if (!spigotServer.isStatic()) {
			File templateFolder = new File("backend/templates/" + spigotServer.getServerGroup());
			if (templateFolder.exists()) {
				try {
					copyFolder(templateFolder.toPath(), folder.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			copy(new File("backend/downloads/" + minecraftVersion.getVersion() + ".jar").toPath(),
					new File(path + "/" + minecraftVersion.getVersion() + ".jar").toPath());
		}
		writeServerproperties(minecraftVersion, spigotServer.getServerName(), spigotServer.getPort());
		

		File pluginFolder = new File(path + "/plugins");
		if (!pluginFolder.exists()) {
			pluginFolder.mkdir();
		}

		if (ApplicationInterface.getAPI().getCloud().getInfrastructure().useViaVersion) {
			copy(new File("backend/downloads/ViaVersion-3.2.1.jar").toPath(),
					new File(path + "/plugins/ViaVersion-3.2.1.jar").toPath());

		}

		copy(new File("backend/downloads/SpigotCloudBridge.jar").toPath(),
				new File(path + "/plugins/SpigotCloudBridge.jar").toPath());

		writeFile(new File(path + "/" + spigotServer.getRegisterKey()), spigotServer.getRegisterKey());
	}

	public ServerGenerator(MinecraftVersion minecraftVersion, Proxy proxy) {

		path = "server/" + ("temp") + "/" + proxy.getProxyName();
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdir();
		}
		writeServerproperties(minecraftVersion, proxy.getProxyName(), proxy.getPort());
		File templateFolder = new File("backend/templates/proxy");
		if (templateFolder.exists()) {
			if (templateFolder.listFiles().length != 0) {
				try {
					copyFolder(templateFolder.toPath(), folder.toPath());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		copy(new File("backend/downloads/" + minecraftVersion.getVersion() + ".jar").toPath(),
				new File(path + "/" + minecraftVersion.getVersion() + ".jar").toPath());

		File pluginFolder = new File(path + "/plugins");
		if (!pluginFolder.exists()) {
			pluginFolder.mkdir();
		}

		copy(new File("backend/downloads/BungeeCloudBridge.jar").toPath(),
				new File(path + "/plugins/BungeeCloudBridge.jar").toPath());

		writeFile(new File(path + "/" + proxy.getKey()), proxy.getKey());

	}

	public void writeServerproperties(MinecraftVersion version, String servername, int port) {
		StringBuilder builder = new StringBuilder();
		if (!version.isProxy()) {
			builder.append("server-ip=localhost\n");
			builder.append("server-port=" + port + "\n");
			builder.append("network-compression-threshold=256\n");
			builder.append("online-mode=false\n\n\n");
			writeFile(new File(path + "/server.properties"), builder.toString());
			writeFile(new File(path + "/bukkit.yml"), "connection-throttle: -1");
		} else {
//			builder.append("server_connect_timeout: 5000\n");
//			builder.append("remote_ping_cache: -1\n");
//			builder.append("forge_support: false\n");
//			builder.append("player_limit: -1\n");
//			builder.append("permissions:\n");
//			builder.append("  default:\n");
//			builder.append("  admin:\n");
//			builder.append("  - bungeecord.command.alert\n");
//			builder.append("  - bungeecord.command.ip\n");
//			builder.append("timeout: 20000\n");
//			builder.append("log_commands: false\n");
//			builder.append("network_compression_threshold: 256\n");
//			builder.append("online_mode: true\n");
//			builder.append("disabled_commands:\n");
//			builder.append("servers:\n");
//			if(!ApplicationInterface.getAPI().getInfrastructure().getRunningServers().isEmpty()) {
//				for(SpigotServer server : ApplicationInterface.getAPI().getInfrastructure().getRunningServers()) {
//					builder.append("  " + server.getServerName() + ":\n");
//					builder.append("    motd: 'BusyCloud-Server | " + server.getServerName() + "'\n");
//					builder.append("    address: localhost:" + server.getPort() + "\n");
//					builder.append("    restricted: false\n");
//				}
//			} else {
//				builder.append("  lobby:\n");
//				builder.append("    motd: '&1Just another BungeeCord - Forced Host'\n");
//				builder.append("    address: localhost:25565\n");
//				builder.append("    restricted: false\n");
//			}
//			builder.append("listeners:\n");
//			builder.append("- query_port: 25577\n");
//			builder.append("  motd: '&bBusyCloud - Proxy'\n");
//			builder.append("  tab_list: GLOBAL_PING\n");
//			builder.append("  query_enabled: false\n");
//			builder.append("  proxy_protocol: false\n");
//			builder.append("  forced_hosts:\n");
//			builder.append("    pvp.md-5.net: pvp\n");
//			builder.append("  ping_passthrough: false\n");
//			builder.append("  priorities:\n");
//			if(!ApplicationInterface.getAPI().getInfrastructure().getRunningServers().isEmpty()) {
//				for(SpigotServer server : ApplicationInterface.getAPI().getInfrastructure().getRunningServers()) {
//					builder.append("  - " + server.getServerName() + "\n");
//				}
//			} else {
//				builder.append("  - lobby\n");
//			}
//			builder.append("  bind_local_address: true\n");
//			builder.append("  host: 0.0.0.0:25577\n");
//			builder.append("  max_players: 80\n");
//			builder.append("  tab_size: 60\n");
//			builder.append("  force_default_server: false\n");
//			builder.append("ip_forward: false\n");
//			builder.append("remote_ping_timeout: 5000\n");
//			builder.append("prevent_proxy_connections: false\n");
//			builder.append("groups:\n");
//			builder.append("  PlugDev:\n");
//			builder.append("  - admin\n");
//			builder.append("connection_throttle: 4000\n");
//			builder.append("stats: c4b9cabb-93e9-4bce-93ab-4b18642e6f3e\n");
//			builder.append("connection_throttle_limit: -1\n");
//			builder.append("log_pings: true\n");
//			writeFile(new File(path + "/config.yml"), builder.toString());
		}
	}

	public void writeFile(File fileObject, String fileContent) {
		try {
			if (!fileObject.exists()) {
				fileObject.createNewFile();
			}
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileObject));
			writer.write(fileContent);
			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void copyFolder(Path src, Path dest) throws IOException {
		Files.walk(src).forEach(sourcePath -> {
			try {
				Files.copy(sourcePath, dest.resolve(src.relativize(sourcePath)), StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException ex) {
			}
		});
	}

	private void copy(Path source, Path dest) {
		try {
			Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
