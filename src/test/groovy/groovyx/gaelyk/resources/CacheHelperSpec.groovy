package groovyx.gaelyk.resources

import groovyx.gaelyk.plugins.PluginResourceSupport;
import groovyx.gaelyk.plugins.PluginsHandler;
import groovyx.gaelyk.spock.GaelykUnitSpec;
import spock.lang.Specification

class CacheHelperSpec extends GaelykUnitSpec {

	def setup(){
		PluginsHandler.instance.scriptContent = { String path ->
			switch (path) {
			case "WEB-INF/plugins.groovy":
				return "install test"
			case "META-INF/gaelyk-plugins/test.groovy":
				return "//some text"
			default:
				return ''
			}
		}
		PluginsHandler.instance.initPlugins()
	}
	
	def "Resource modified time"(){
		URL url = PluginResourceSupport.getPluginFileURL("resources", "gaelyk-plugins/test/test.txt")
		
		expect:
		url
		CacheHelper.getJarFilePath(url).endsWith "WEB-INF/lib/test-gaelyk-resources-plugin.jar"
		!CacheHelper.getJarFilePath(url).startsWith("jar:")
		!CacheHelper.getJarFilePath(url).startsWith("file:")
		CacheHelper.getJarEntryName(url) == "META-INF/gaelyk-plugins/resources/test.txt"
		CacheHelper.getLastModified(url) < System.currentTimeMillis()
	}
	
}
