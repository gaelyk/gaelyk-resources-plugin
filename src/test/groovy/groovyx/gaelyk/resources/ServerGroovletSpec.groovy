package groovyx.gaelyk.resources


class ServerGroovletSpec extends PluginGroovletSpec {

	String getGroovletName() { 'groovyx/gaelyk/resources/server.groovy' }
	
	def setup(){
		groovletInstance.request.getHeader = { CacheHelper.getHeaderDateFromMillis(0) }
	}
	
	def "Serve file"(){
        StringWriter sw = new StringWriter()
        PrintWriter pw = new PrintWriter(sw)
        
		when:
		groovletInstance.params = params
		groovletInstance.out    = pw
		groovletInstance.get()
		
		String result = sout.toString() ?: sw.toString()
		
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
