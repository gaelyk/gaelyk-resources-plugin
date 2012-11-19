package groovyx.gaelyk.resources

import groovyx.gaelyk.plugins.PluginsHandler

class ServerGroovletSpec extends PluginGroovletSpec {

	String getGroovletName() { 'groovyx/gaelyk/resources/server.groovy' }
	
	def setup(){
		groovletInstance.request.getDateHeader = { 0 }
	}
	
	def "Serve file"(){
		when:
		groovletInstance.params = params
		
		groovletInstance.get()
		
		String result = sout.toString()
		
		then:
		result == output
		
		where:
		output						| params
		"Hello world"				| [path: "test.txt"]
		"File not found!"			| [path: "text.txt"]
		"Path not defined!"			| []
	}
	
	def "Serve file with right mime type"(){
		when:
		groovletInstance.params = [path: "test.txt"]
		groovletInstance.get()
		
		String result = sout.toString()
		
		then:
		result == "Hello world"
		 1 * response.setContentType("text/plain")
		 1 * response.setContentLength("Hello world".bytes.size())
		 
	}
	

}
