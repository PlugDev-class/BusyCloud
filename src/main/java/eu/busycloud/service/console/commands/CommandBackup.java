package eu.busycloud.service.console.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

import eu.busycloud.service.CloudInstance;
import eu.busycloud.service.console.ConsoleCommand;

public class CommandBackup extends ConsoleCommand {

	public CommandBackup(String help) {
		super(help);
	}

	SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy - HH;mm");
	
	@Override
	public void runCommand(String command, String[] args) {
		if(args.length == 2) {
			if(args[1].equalsIgnoreCase("create")) {
				CloudInstance.LOGGER.info("Backupping current serverfiles to backend/backups/" + format.format(new Date()));
				try {
					copyFolder(new File("server").toPath(), new File("saves/backups/" + format.format(new Date())).toPath());
				} catch (DirectoryNotEmptyException e) {
					delete(new File("saves/backups/" + format.format(new Date())));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			return;
		}
		CloudInstance.LOGGER.info("/backup create ~ Make a backup");
	}
	
	public void delete(File root) {
		CloudInstance.LOGGER.info("Deleting old backup: " + root.getPath());
		for(File file : root.listFiles()) {
			if(file.isDirectory()) {
				CloudInstance.LOGGER.info("Deleting file: " + file.getName());
				delete(file);
			}
			file.delete();
		}
	}

	private void copyFolder(Path src, Path dest) throws IOException {
		if (!dest.toFile().exists()) {
			dest.toFile().mkdir();
		}
		try (Stream<Path> stream = Files.walk(src)) {
			stream.forEach(source -> copy(source, dest.resolve(src.relativize(source))));
		}
	}

	private void copy(Path source, Path dest) {
		try {
			Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
			CloudInstance.LOGGER.info("Backupping current file: " + source.toFile().getName());
		} catch (DirectoryNotEmptyException e) {
			delete(new File("saves/backups/" + format.format(new Date())));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
