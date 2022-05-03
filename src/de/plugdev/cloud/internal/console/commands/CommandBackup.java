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
import de.plugdev.cloud.lang.LanguageManager;

public class CommandBackup implements ConsoleCommand {


	SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy - HH_mm");
	Date date = null;
	
	@Override
	public void runCommand(String command, String[] args) {
		if(args.length == 2) {
			if(args[1].equalsIgnoreCase("create")) {
				ConsoleOutput.write(ConsoleOutput.RED, LanguageManager.getVar("plugin.default.command.backup.createFor", format.format(date = new Date())));
				try {
					copyFolder(new File("server").toPath(), new File("backend/backups/" + format.format(date)).toPath());
				} catch (DirectoryNotEmptyException e) {
					delete(new File("backend/backups/" + format.format(date)));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void delete(File root) {
		ConsoleOutput.write(ConsoleOutput.RED, LanguageManager.getVar("plugin.default.command.backup.deleteFolder", root.getPath()));
		for(File file : root.listFiles()) {
			if(file.isDirectory()) {
				ConsoleOutput.write(ConsoleOutput.RED, LanguageManager.getVar("plugin.default.command.backup.deleteFile", file.getName()));
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
			ConsoleOutput.write(ConsoleOutput.RED, LanguageManager.getVar("plugin.default.command.backup.createForCF", source.toFile().getName()));
		} catch (DirectoryNotEmptyException e) {
			delete(new File("backend/backups/" + format.format(date)));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

	@Override
	public String getHelp() {
		return "Backup current servers";
	}

}
