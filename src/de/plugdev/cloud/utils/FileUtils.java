package de.plugdev.cloud.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class FileUtils {
	
	public static void deleteFile(String path) {
		new File(path).delete();
	}
	
	public static void mkdir(String path) {
		new File(path).mkdir();
	}
	
	public static void mkdirs(String path) {
		new File(path).mkdirs();
	}
	
	public static void createFile(String path) {
		try {
			new File(path).createNewFile();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public static void deleteFolderRecursivly(String path) {
		
	}
	
	public static void writeFile(File file, String string) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(string);
			writer.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	public static void download(String url, String path) {
		System.out.print("[SETUP] Download \"" + url + "\" started");
		try {
			Files.copy(new URL(url).openStream(), Paths.get(path));
			System.out.print("   .. | ..   done");
		} catch (IOException e) {
			System.out.print("   .. | ..   failed");
		}
		System.out.println(" ");
	}
	
	public static void checkFolder(String path) {
		if(!new File(path).exists()) {
			mkdir(path);
		}
	}
	
	public static void checkFolder(File file) {
		if(!file.exists()) {
			mkdir(file.getPath());
		}
	}
	
	public static void copyFolder(Path src, Path dest) throws IOException {
		try (Stream<Path> stream = Files.walk(src)) {
			stream.forEach(source -> copyFile(source, dest.resolve(src.relativize(source))));
		}
	}

	public static void copyFile(Path source, Path dest) {
		try {
			Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}
	
}
