package groovyx.gaelyk.resources

import groovyx.gaelyk.GaelykCategory;
import groovyx.gaelyk.plugins.PluginsHandler;

class ServerGroovletSpec extends PluginGroovletSpec {

	String getGroovletName() { 'server.groovy' }
	
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
		groovletInstance.request.getDateHeader = { 0 }
	}
	
	def "Serve file"(){
		when:
		groovletInstance.params = params
		
		use(GaelykCategory){
			groovletInstance.get()
		}
		
		String result = sout.toString()
		
		then:
		result == output
		
		where:
		output						| params
		"Hello world"				| [plugin: "test", path: "test.txt"]
		"File not found!"			| [plugin: "test", path: "text.txt"]
		"Missing plugin definition!"| [path: "text.txt"]
		"Path not defined!"			| [plugin: "test"]
	}
	
	def "Serve file with right mime type"(){
		when:
		groovletInstance.params = [plugin: "test", path: "test.txt"]
		
		use(GaelykCategory){
			groovletInstance.get()
		}
		
		String result = sout.toString()
		
		then:
		result == "Hello world"
		 1 * response.setContentType("text/plain")
		 1 * response.setContentLength("Hello world".bytes.size())
		 
	}
	

}
