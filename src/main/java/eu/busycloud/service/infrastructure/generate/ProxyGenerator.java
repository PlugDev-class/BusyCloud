package eu.busycloud.service.infrastructure.generate;

import java.io.File;
import java.nio.file.Paths;

import eu.busycloud.service.api.ApplicationInterface;
import eu.busycloud.service.infrastructure.ProxyServer;
import eu.busycloud.service.utils.FileUtils;

public class ProxyGenerator extends ServerGenerator {

	private transient ProxyServer proxyServer;
	private transient String proxyServerPath;

	public ProxyGenerator(ProxyServer proxyServer) {
		this.proxyServer = proxyServer;
		generateServer();
	}

	@Override
	public void generateServer() {
		proxyServerPath = "server/temp/" + proxyServer.getProxyName();
		FileUtils.mkdirs(proxyServerPath);
		FileUtils.createFile(proxyServerPath + "/" + proxyServer.getKey());
		if (proxyServer.getSoftware().getVersionName().contains("Waterdog")) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("groups:\r\n");
			stringBuilder.append("  PlugDev:\r\n");
			stringBuilder.append("  - admin\r\n");
			stringBuilder.append("bedrock_encryption: true\r\n");
			stringBuilder.append("disabled_commands:\r\n");
			stringBuilder.append("- disabledcommandhere\r\n");
			stringBuilder.append("use_xuid_for_uuid: true\r\n");
			stringBuilder.append("timeout: 30000\r\n");
			stringBuilder.append("online_mode: true\r\n");
			stringBuilder.append("servers:\r\n");
			stringBuilder.append("  fallbackS:\r\n");
			stringBuilder.append("    raknet: true\r\n");
			stringBuilder.append("    motd: '&1Just another Waterfall - Forced Host'\r\n");
			stringBuilder.append("    transfer_group: default\r\n");
			stringBuilder.append("    address: localhost:" + ApplicationInterface.getAPI().getInfrastructure()
					.getRunningGroups().get(0).getServerGroupContainer().getStartPort() + "\r\n");
			stringBuilder.append("    restricted: false\r\n");
			stringBuilder.append("server_connect_timeout: 5000\r\n");
			stringBuilder.append("listeners:\r\n");
			stringBuilder.append("- query_port: 25577\r\n");
			stringBuilder.append("  motd: '&1Another Bungee server'\r\n");
			stringBuilder.append("  tab_list: GLOBAL_PING\r\n");
			stringBuilder.append("  query_enabled: false\r\n");
			stringBuilder.append("  proxy_protocol: false\r\n");
			stringBuilder.append("  forced_hosts: &id036\r\n");
			stringBuilder.append("    pvp.md-5.net: pvp\r\n");
			stringBuilder.append("  ping_passthrough: false\r\n");
			stringBuilder.append("  raknet: true\r\n");
			stringBuilder.append("  priorities:\r\n");
			stringBuilder.append("  - fallbackS\r\n");
			stringBuilder.append("  bind_local_address: true\r\n");
			stringBuilder.append("  host: 0.0.0.0:" + proxyServer.getPort() + "\r\n");
			stringBuilder.append("  max_players: 1\r\n");
			stringBuilder.append("  tab_size: 60\r\n");
			stringBuilder.append("  force_default_server: false\r\n");
			stringBuilder.append("- query_port: 19132\r\n");
			stringBuilder.append("  motd: '&1Another Bungee server'\r\n");
			stringBuilder.append("  tab_list: GLOBAL_PING\r\n");
			stringBuilder.append("  query_enabled: true\r\n");
			stringBuilder.append("  proxy_protocol: false\r\n");
			stringBuilder.append("  forced_hosts: *id036\r\n");
			stringBuilder.append("  ping_passthrough: false\r\n");
			stringBuilder.append("  raknet: true\r\n");
			stringBuilder.append("  priorities:\r\n");
			stringBuilder.append("  - fallbackS\r\n");
			stringBuilder.append("  bind_local_address: true\r\n");
			stringBuilder.append("  host: 0.0.0.0:" + proxyServer.getPort() + "\r\n");
			stringBuilder.append("  max_players: 1\r\n");
			stringBuilder.append("  tab_size: 60\r\n");
			stringBuilder.append("  force_default_server: false\r\n");
			stringBuilder.append("stats: 71699f67-cf6b-4eaf-adc2-b7c749456ed4\r\n");
			stringBuilder.append("network_compression_threshold: 256\r\n");
			stringBuilder.append("log_pings: true\r\n");
			stringBuilder.append("permissions:\r\n");
			stringBuilder.append("  default:\r\n");
			stringBuilder.append("  - bungeecord.command.server\r\n");
			stringBuilder.append("  - bungeecord.command.list\r\n");
			stringBuilder.append("  admin:\r\n");
			stringBuilder.append("  - bungeecord.command.alert\r\n");
			stringBuilder.append("  - bungeecord.command.end\r\n");
			stringBuilder.append("  - bungeecord.command.ip\r\n");
			stringBuilder.append("  - bungeecord.command.reload\r\n");
			stringBuilder.append("max_mtu: -1\r\n");
			stringBuilder.append("ip_forward: false\r\n");
			stringBuilder.append("player_limit: -1\r\n");
			stringBuilder.append("log_commands: false\r\n");
			stringBuilder.append("connection_throttle_limit: 3\r\n");
			stringBuilder.append("connection_throttle: 4000\r\n");
			stringBuilder.append("replace_username_spaces: true\r\n");
			stringBuilder.append("prevent_proxy_connections: false\r\n");
			stringBuilder.append("remote_ping_timeout: 5000\r\n");
			stringBuilder.append("forge_support: true\r\n");
			stringBuilder.append("remote_ping_cache: -1\r\n");
			FileUtils.writeFile(new File(proxyServerPath + "/config.yml"), stringBuilder.toString());
		}
		FileUtils.copyFolder(Paths.get("saves/templates", "proxy"), Paths.get(proxyServerPath, "."), false);
		FileUtils.copyFile(
				new File("saves/environments/" + proxyServer.getSoftware().getVersionName() + ".jar").toPath(),
				new File(proxyServerPath + "/" + proxyServer.getSoftware().getVersionName() + ".jar").toPath());
		FileUtils.mkdirs(proxyServerPath + "/plugins");
		FileUtils.copyFile(Paths.get("saves/environment-plugins", "CloudAPI.jar"),
				Paths.get(proxyServerPath + "/plugins", "CloudAPI.jar"));
	}

}
