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
	
	/*
	 * Deletes a file in specific path
	 * @param path A path to a specific file
	 * @since 1.02
	 */
	
	public static void deleteFile(String path) {
		new File(path).delete();
	}
	
	/*
	 * Generate a folder in specific path
	 * @param path A path to a specific file
	 * @since 1.02
	 */
	
	public static void mkdir(String path) {
		new File(path).mkdir();
	}
	
	/*
	 * Generate a folder in specific path with parentfolders behind it.
	 * @param path A path to a specific file
	 * @since 1.02
	 */
	
	public static void mkdirs(String path) {
		new File(path).mkdirs();
	}
	
	/*
	 * Generate file with in specific path.
	 * @param path A path to a specific file
	 * @throws IOException
	 * @since 1.02
	 */
	
	public static void createFile(String path) {
		try {
			new File(path).createNewFile();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	/*
	 * Delete a folder recursivly at specific path
	 * @param path A path to a specific file
	 * @since 1.02
	 */
	
	public static void deleteFolderRecursivly(String path) {
		for(File files : new File(path).listFiles()) {
			if(files.isDirectory()) {
				deleteFolderRecursivly(path);
			}
			files.delete();
		}
	}
	
	/*
	 * Delete a folder recursivly at specific path
	 * @param file A path to a specific file
	 * @param string The content in file.
	 * @throws IOException
	 * @since 1.02
	 */
	
	public static void writeFile(File file, String string) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(string);
			writer.close();
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
	/*
	 * Downloads content in specific url.
	 * @param url An url to specific online-file.
	 * @param path The path whereto download the file.
	 * @throws IOException
	 * @since 1.02
	 */
	
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
	
	/*
	 * Check if folder exists, if no, create a new one.
	 * @param path A path to a specific file
	 * @since 1.02
	 */
	
	public static void checkFolder(String path) {
		if(!new File(path).exists()) {
			mkdir(path);
		}
	}
	
	/*
	 * Check if folder exists, if no, create a new one.
	 * @param path A path to a specific file
	 * @since 1.02
	 */
	public static void checkFolder(File file) {
		if(!file.exists()) {
			mkdir(file.getPath());
		}
	}
	
	/*
	 * Copy whole folder in specific path to specific path.
	 * @param src The sourcepath of copying.
	 * @param dest The destinationpath of copying.
	 * @throws IOException
	 * @since 1.02
	 */
	
	public static void copyFolder(Path src, Path dest) throws IOException {
		try (Stream<Path> stream = Files.walk(src)) {
			stream.forEach(source -> copyFile(source, dest.resolve(src.relativize(source))));
		}
	}
	
	/*
	 * Copy a single file in specific path to specific path.
	 * @param src The sourcepath of copying.
	 * @param dest The destinationpath of copying.
	 * @throws IOException
	 * @since 1.02
	 */
	public static void copyFile(Path source, Path dest) {
		try {
			Files.copy(source, dest, StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException exception) {
			exception.printStackTrace();
		}
	}
	
}
