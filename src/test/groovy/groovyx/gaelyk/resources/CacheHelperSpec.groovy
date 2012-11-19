package groovyx.gaelyk.resources

import groovyx.gaelyk.spock.GaelykUnitSpec
import spock.lang.Specification

class CacheHelperSpec extends GaelykUnitSpec {

	
	def "Resource modified time"(){
		URL url = getClass().getClassLoader().getResource("resources/test.txt")
		
		expect:
		url
		CacheHelper.getJarFilePath(url).endsWith "WEB-INF/lib/test-gaelyk-resources-plugin.jar"
		!CacheHelper.getJarFilePath(url).startsWith("jar:")
		!CacheHelper.getJarFilePath(url).startsWith("file:")
		CacheHelper.getJarEntryName(url) == "resources/test.txt"
		//CacheHelper.getLastModified(url) < System.currentTimeMillis()
	}
	
}
