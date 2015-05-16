package lemon.engine.fileIO;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarUtil{
	public static URL getURL(String filename){
		try {
			return Paths.get(filename).toUri().toURL();
		} catch (MalformedURLException e) {
			return null;
		}
	}
	public static URL getURL(Path path){
		try {
			return path.toUri().toURL();
		} catch (MalformedURLException e) {
			return null;
		}
	}
	public static JarFile getJarFile(Path path){
		try {
			return new JarFile(path.toFile());
		} catch (IOException e) {
			return null;
		}
	}
	public static InputStream getInputStream(JarFile jar, JarEntry entry){
		try {
			return jar.getInputStream(entry);
		} catch (IOException e) {
			return null;
		}
	}
	public static URLClassLoader getClassLoader(URL url, ClassLoader loader){
		return new URLClassLoader(new URL[]{url}, loader);
	}
	public static URLClassLoader getClassLoader(String filename){
		return getClassLoader(getURL(filename), JarUtil.class.getClassLoader());
	}
	public static URLClassLoader getClassLoader(Path path){
		return getClassLoader(getURL(path), JarUtil.class.getClassLoader());
	}
}
