package groovyx.gaelyk.resources

import java.text.SimpleDateFormat;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

class CacheHelper {
	
	static long resouceExpiration = 24 * 60 * 60
	static long lastDeployment = System.currentTimeMillis() + 365L * resouceExpiration * 1000
	
	static {
		File libDir = new File("WEB-INF/lib")
		if(libDir.exists()){
			lastDeployment = libDir.lastModified()
		}
	}
	
	static boolean isModified(URL url, long ifModifiedHeader){
		if(ifModifiedHeader > lastDeployment){
			return false
		}
		return getLastModified(url) > ifModifiedHeader
	}
	
	static long getLastModified(URL url){
		try {
			JarFile jarFile = new JarFile(new File(getJarFilePath(url)))
			JarEntry entry = jarFile.getJarEntry(getJarEntryName(url))
			if(!entry){
				return lastDeployment
			}
			return entry.time
		} catch(ZipException e){
			return lastDeployment
		}
	}
	
	private static getJarFilePath(URL url){
		def urlString = url.toExternalForm()
		return urlString[9..(urlString.indexOf("!")-1)]
	}
	
	private static getJarEntryName(URL url){
		def urlString = url.toExternalForm()
		return urlString[((urlString.indexOf("!")+2)..-1)]
	}
	
}
