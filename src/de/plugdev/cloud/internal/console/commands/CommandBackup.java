package de.plugdev.cloud.internal.console.commands;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.stream.Stream;

import de.plugdev.cloud.internal.console.ConsoleCommand;
import de.plugdev.cloud.internal.console.ConsoleOutput;

public class CommandBackup implements ConsoleCommand {


	SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy - HH_mm");
	
	@Override
	public void runCommand(String command, String[] args) {
		if(args.length == 2) {
			if(args[1].equalsIgnoreCase("create")) {
				ConsoleOutput.write(ConsoleOutput.RED,
						"[BACKUP] Backupping current serverfiles to backend/backups/" + format.format(new Date()));
				try {
					copyFolder(new File("server").toPath(), new File("backend/backups/" + format.format(new Date())).toPath());
				} catch (DirectoryNotEmptyException e) {
					delete(new File("backend/backups/" + format.format(new Date())));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void delete(File root) {
		ConsoleOutput.write(ConsoleOutput.RED, "[BACKUP] Deleting old backup: " + root.getPath());
		for(File file : root.listFiles()) {
			if(file.isDirectory()) {
				ConsoleOutput.write(ConsoleOutput.RED, "[BACKUP] Deleting file: " + file.getName());
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
			ConsoleOutput.write(ConsoleOutput.RED, "[BACKUP] Backupping current file: " + source.toFile().getName());
		} catch (DirectoryNotEmptyException e) {
			delete(new File("backend/backups/" + format.format(new Date())));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public String getHelp() {
		return "Backup current servers";
	}

}
