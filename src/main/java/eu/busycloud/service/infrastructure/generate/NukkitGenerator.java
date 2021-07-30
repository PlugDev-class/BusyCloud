package eu.busycloud.service.infrastructure.generate;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import eu.busycloud.service.infrastructure.NukkitServer;
import eu.busycloud.service.utils.FileUtils;

public class NukkitGenerator extends ServerGenerator {

	private transient NukkitServer nukkitServer;
	private transient String nukkitServerPath;

	public NukkitGenerator(NukkitServer nukkitServer) {
		this.nukkitServer = nukkitServer;
		generateServer();
	}

	@Override
	public void generateServer() {
		nukkitServerPath = "server/" + (nukkitServer.isStatic() ? "static" : "temp") + "/"
				+ nukkitServer.getServerName();
		FileUtils.mkdirs(nukkitServerPath);
		if (!nukkitServer.isStatic())
			FileUtils.copyFolder(Paths.get("saves/templates/", nukkitServer.getServerGroup()),
					Paths.get(nukkitServerPath, "."), false);
		FileUtils.checkFolder(nukkitServerPath + "/plugins");
		FileUtils.copyFile(
				new File("saves/environments/" + nukkitServer.getServerSoftware().getVersionName() + ".jar").toPath(),
				new File(nukkitServerPath + "/" + nukkitServer.getServerSoftware().getVersionName() + ".jar").toPath());
		FileUtils.writeFile(new File(nukkitServerPath + "/" + nukkitServer.getRegisterKey()),
				nukkitServer.getRegisterKey());
		FileUtils.mkdirs(nukkitServerPath + "/plugins");
		FileUtils.copyFile(Paths.get("saves/environment-plugins", "CloudAPI.jar"),
							Paths.get(nukkitServerPath + "/plugins", "CloudAPI.jar"));
		generateSpigotProperties();
	}

	@Override
	public void generateSpigotProperties() {
		try {
			StringBuilder builder = new StringBuilder();
			builder.append("motd=Nukkit Server hosted by BusyCloud\n");
			builder.append("server-port=" + nukkitServer.getPort() + "\n");
			builder.append("server-ip=localhost\n");
			Files.write(Paths.get(nukkitServerPath, "server.properties"), builder.toString().getBytes(),
					StandardOpenOption.CREATE_NEW);
			builder = new StringBuilder();
			builder.append("# Advanced configuration file for Nukkit\r\n");
			builder.append("# Some of these settings are safe, others can break your server if modified incorrectly\r\n");
			builder.append("# New settings/defaults won't appear automatically on this file when upgrading\r\n");
			builder.append("\r\n");
			builder.append("settings:\r\n");
			builder.append(" # Multi-language setting\r\n");
			builder.append(" # Available: %1\r\n");
			builder.append(" language: eng\r\n");
			builder.append(" # Whether to send all strings translated to server locale or let the device handle them\r\n");
			builder.append(" force-language: true\r\n");
			builder.append(" shutdown-message: \"BusyCloud: Nukkit-Server closed\"\r\n");
			builder.append(" # Allow listing plugins via Query\r\n");
			builder.append(" query-plugins: true\r\n");
			builder.append(" # Show a console message when a plugin uses deprecated API methods\r\n");
			builder.append(" deprecated-verbose: true\r\n");
			builder.append(" # Number of AsyncTask workers\r\n");
			builder.append(" # If set to auto, it'll try to detect the number of cores (and at least 4)\r\n");
			builder.append(" async-workers: auto\r\n");
			builder.append(" safe-spawn: true\r\n");
			builder.append("\r\n");
			builder.append("network:\r\n");
			builder.append(" batch-threshold: 256\r\n");
			builder.append(" compression-level: 7\r\n");
			builder.append(" async-compression: true\r\n");
			builder.append("\r\n");
			builder.append("debug:\r\n");
			builder.append(" level: 1\r\n");
			builder.append(" commands: false\r\n");
			builder.append(" ignored-packets:\r\n");
			builder.append("  - LevelChunkPacket\r\n");
			builder.append("\r\n");
			builder.append("timings:\r\n");
			builder.append(" enabled: false\r\n");
			builder.append(" verbose: false\r\n");
			builder.append(" history-interval: 6000\r\n");
			builder.append(" history-length: 72000\r\n");
			builder.append(" bypass-max: false\r\n");
			builder.append(" privacy: false\r\n");
			builder.append(" ignore: []\r\n");
			builder.append("\r\n");
			builder.append("level-settings:\r\n");
			builder.append(" default-format: anvil\r\n");
			builder.append(" auto-tick-rate: true\r\n");
			builder.append(" auto-tick-rate-limit: 20\r\n");
			builder.append(" base-tick-rate: 1\r\n");
			builder.append(" always-tick-players: false\r\n");
			builder.append(" tick-redstone: true\r\n");
			builder.append("\r\n");
			builder.append("chunk-sending:\r\n");
			builder.append(" per-tick: 4\r\n");
			builder.append(" max-chunks: 192\r\n");
			builder.append(" spawn-threshold: 56\r\n");
			builder.append(" cache-chunks: false\r\n");
			builder.append("\r\n");
			builder.append("chunk-ticking:\r\n");
			builder.append(" per-tick: 40\r\n");
			builder.append(" tick-radius: 3\r\n");
			builder.append(" light-updates: false\r\n");
			builder.append(" clear-tick-list: false\r\n");
			builder.append("\r\n");
			builder.append("chunk-generation:\r\n");
			builder.append(" queue-size: 8\r\n");
			builder.append(" population-queue-size: 8\r\n");
			builder.append("\r\n");
			builder.append("# Max tick rate for these entities\r\n");
			builder.append("ticks-per:\r\n");
			builder.append(" animal-spawns: 400\r\n");
			builder.append(" monster-spawns: 1\r\n");
			builder.append(" autosave: 6000\r\n");
			builder.append(" cache-cleanup: 900\r\n");
			builder.append("\r\n");
			builder.append("# Max amount of these entities\r\n");
			builder.append("spawn-limits:\r\n");
			builder.append(" monsters: 70\r\n");
			builder.append(" animals: 15\r\n");
			builder.append(" water-animals: 5\r\n");
			builder.append(" ambient: 15\r\n");
			builder.append("\r\n");
			builder.append("player:\r\n");
			builder.append(" save-player-data: true\r\n");
			builder.append(" skin-change-cooldown: 30\r\n");
			builder.append(" force-skin-trusted: false\r\n");
			builder.append(" check-movement: true\r\n");
			builder.append("\r\n");
			builder.append("aliases:\r\n");
			builder.append(" # Aliases for commands\r\n");
			builder.append(" # Examples:\r\n");
			builder.append(" # showtheversion: version\r\n");
			builder.append(" # savestop: [save-all, stop]\r\n");
			builder.append("\r\n");
			builder.append("worlds:\r\n");
			builder.append(" # These settings will override the generator set in server.properties and allows loading multiple levels\r\n");
			builder.append(" # Examples:\r\n");
			builder.append(" #world:\r\n");
			builder.append(" # seed: 404\r\n");
			builder.append(" # generator: FLAT:2;7,59x1,3x3,2;1;decoration(treecount=80 grasscount=45)\r\n");
			Files.write(Paths.get(nukkitServerPath, "nukkit.yml"), builder.toString().getBytes(),
					StandardOpenOption.CREATE_NEW);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

}
