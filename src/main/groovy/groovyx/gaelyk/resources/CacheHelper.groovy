/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

 package groovyx.gaelyk.resources

import java.text.SimpleDateFormat;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipException;

/**
 * Utility class for better caching of the resources.
 * 
 * @author Vladimir Orany
 */
class CacheHelper {
	
	static long resouceExpiration = 24 * 60 * 60
	static long lastDeployment = System.currentTimeMillis() + 365L * resouceExpiration * 1000
	
	static {
		File libDir = new File("WEB-INF/lib")
		if(libDir.exists()){
			lastDeployment = libDir.lastModified()
		}
	}
	
	/**
	 * Checks if the specified resource is modified since the time given in the header.
	 * @param url					url of the resource
	 * @param ifModifiedHeader		last modified date as long
	 * @return						true if the file has been modified after given time
	 */
	static boolean isModified(URL url, long ifModifiedHeader){
		if(ifModifiedHeader > lastDeployment){
			return false
		}
		return getLastModified(url) > ifModifiedHeader
	}
	
	/**
	 * Returns the time stamp of given resources
	 * @param url the resource which time stamp we demand
	 * @return the time when the file in the jar was modified (e.g. time when the jar was build)
	 */
	static long getLastModified(URL url){
        if(!url){
            return lastDeployment;
        }
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
