package lemon.engine.fileIO;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.imageio.ImageIO;

public class DataUtil{
	private static final int BUFFER = 2048;
	private static String jarPath;
	public static void writeFile(String directory, String filename, String[] data){
		File file = new File(directory, filename);
		File directoryFile = new File(directory);
		directoryFile.mkdirs();
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException ex) {
				return;
			}
		}
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getAbsoluteFile()))){
			for(String line: data){
				writer.write(line);
			}
		} catch (IOException ex) {
			return;
		}
	}
	public static int findNextAvailableId(String directory, String prefix, String suffix){
		int id = 1;
		while(new File(directory+"/"+prefix+id+suffix).exists()){
			id++;
		}
		return id;
	}
	public static String[] getFiles(String folder, String... extensions){
		File file = new File(folder, ".");
		if(!file.exists()){
			return new String[]{};
		}
		String[] files = file.list();
		if(extensions==null){
			return files;
		}
		List<String> filtered = new ArrayList<String>();
		for(String filename: files){
			for(String extension: extensions){
				if(filename.endsWith(extension)){
					filtered.add(filename);
					break;
				}
			}
		}
		return filtered.toArray(new String[]{});
	}
	public static void compress(String destinationFolder, String filename, String folder, String[] files){
		new File(destinationFolder).mkdirs();
		try {
			FileOutputStream destination = new FileOutputStream(destinationFolder+"/"+filename);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(destination));
			byte[] data = new byte[BUFFER];
			out.setMethod(ZipOutputStream.DEFLATED);
			for(String file: files){
				FileInputStream in = new FileInputStream(folder+"/"+file);
				BufferedInputStream origin = new BufferedInputStream(in, BUFFER);
				ZipEntry entry = new ZipEntry(folder+"/"+file);
				out.putNextEntry(entry);
				int count;
				while((count=origin.read(data, 0, BUFFER)) != -1){
					out.write(data, 0, count);
				}
				origin.close();
			}
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void deleteFiles(String folder, String[] files){
		for(String file: files){
			new File(folder, file).delete();
		}
	}
	public static BufferedImage loadImage(String path){
		try{
			return ImageIO.read(new File(path));
		}catch(IOException ex){
			return null;
		}
	}
	public static Path downloadFile(byte[] bytes, String path){
		Path file = Paths.get(path);
		Path parent = file.getParent();
		if(!Files.exists(parent)){
			try {
				Files.createDirectories(parent);
			} catch (IOException e) {
				return null;
			}
		}
		try {
			return Files.write(file, bytes);
		} catch (IOException e) {
			return null;
		}
	}
	public static String getJarPath(){
		if(jarPath==null){
			jarPath = new File(System.getProperty("java.class.path")).getAbsoluteFile().getParentFile().toString();
		}
		return jarPath;
	}
}
