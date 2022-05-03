package de.plugdev.cloud.internal.infrastructure;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import de.plugdev.cloud.external.ApplicationInterface;
import de.plugdev.cloud.external.PlayerInfo;
import de.plugdev.cloud.internal.console.ConsoleOutput;
import de.plugdev.cloud.internal.infrastructure.generate.ServerGenerator;
import de.plugdev.cloud.internal.models.IVersion;
import de.plugdev.cloud.internal.utils.FileUtils;
import de.plugdev.cloud.packets.PacketProxyRegisterServer;
import de.plugdev.cloud.packets.PacketProxyUnRegisterServer;
import de.plugdev.cloud.packets.PacketSharedRconServer;
import de.terrarier.netlistening.Connection;
import lombok.Data;

@Data
public class Proxy implements IService {

	private Process instance;

	private final String proxyName;
	private final UUID proxyid = UUID.randomUUID();
	private final IVersion version;
	private int port;
	
	private boolean maintenance = false;
	private final List<UUID> whitelistedPlayers = new ArrayList<>();

	private Connection connection;
	private String registerKey;

	private final List<IService> registeredServer = new ArrayList<>();
	private final List<PlayerInfo> onlinePlayer = new ArrayList<>();
	
	@Override
	public void start() {
		this.registerKey = "KEY_" + new Random().nextInt(Integer.MAX_VALUE);
		this.port = (int) (25574 + (ApplicationInterface.getAPI().getInfrastructure().getServices().stream().filter((predicate) -> predicate.getName().startsWith("Proxy")).count()));
		new ServerGenerator(version, this);
		String startCommand = "java -XX:+UseG1GC -XX:MaxGCPauseMillis=50 -XX:MaxPermSize=256M -XX:-UseAdaptiveSizePolicy -XX:CompileThreshold=100 "
				+ "-Dcom.mojang.eula.agree=true -Dio.netty.recycler.maxCapacity=0 "
				+ "-Dio.netty.recycler.maxCapacity.default=0 "
				+ "-Djline.terminal=jline.UnsupportedTerminal -Xmx" + 512 + "M -jar " + version.getVersion()
				+ ".jar";
		new Thread(() -> {
			try {
				this.instance = Runtime.getRuntime().exec(startCommand.split(" "), null, new File("server/temp/" + this.getProxyName()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
	}

	@Override
	public void stop() {
		if(getConnection() != null)
			if(getConnection().isConnected())
				getConnection().disconnect();
		new Thread(() -> {
			FileUtils.deleteFolderRecursivly(new File("server/" + ("temp") + "/" + this.getProxyName()).getPath());
			FileUtils.deleteFile("server/" + ("temp") + "/" + this.getProxyName());
		}).start();
		ConsoleOutput.write(ConsoleOutput.GREEN_BOLD, "Stopping Proxy(\"Proxy-" + getProxyid() + " - localhost:" + port + "\")");
	}
	
	public void addSpigotServer(IService service, boolean isMain) {
		registeredServer.add(service);
		connection.sendData(new PacketProxyRegisterServer(service.getName(), service.getPort(), ((SpigotServer) service).isLobby()));
	}

	public void removeSpigotServer(IService service) {
		connection.sendData(new PacketProxyUnRegisterServer(service.getName()));
	}
	
	public void doTemplate() {
		File serverFolder = new File("server/temp/" + getProxyName());
		File backendTemplates = new File("backend/templates/proxy");
		if(!backendTemplates.exists()) {
			backendTemplates.mkdirs();
		} else if(backendTemplates.listFiles().length != 0) {
			FileUtils.deleteFolderRecursivly(backendTemplates.getPath());
		}
		try {
			FileUtils.copyFolder(serverFolder.toPath(), backendTemplates.toPath());
			ConsoleOutput.write(ConsoleOutput.GREEN, "Template created.");
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public void rcon(String command) {
		connection.sendData(new PacketSharedRconServer(command));
	}

	@Override
	public String getKey() {
		return registerKey;
	}

	@Override
	public UUID getUniqueId() {
		return proxyid;
	}

	@Override
	public String getName() {
		return proxyName;
	}

	@Override
	public void setConnection(Connection object) {
		this.connection = object;
	}
	
}
