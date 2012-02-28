package groovyx.gaelyk.resources

import groovyx.gaelyk.spock.GroovletMockLogger;

class PluginGroovletUnderSpec {
	static final LOCATION = "src/main/resources/META-INF/gaelyk-plugins/groovy"

	def gse = new GroovyScriptEngine(LOCATION)
	def binding = new Binding()
	def log = new GroovletMockLogger(level:'info')
	def scriptName

	def forward = ''
	def redirect = ''
	def logging = ''

	PluginGroovletUnderSpec(scriptName){
		if(!scriptName){
			throw new IllegalStateException('The scriptName was not defined in setup()')
		}
		if(! new File("$LOCATION/$scriptName").exists()){
			throw new IllegalArgumentException("$scriptName not found. No such file in $LOCATION?")
		}
		this.scriptName = scriptName
		bindVariables()
	}

	def bindVariables = {
		binding.setVariable 'params', [:]
		binding.setVariable 'headers', [:]
		binding.setVariable 'request', [:]
		binding.setVariable 'forward', { forward = it }
		binding.setVariable 'redirect', { redirect = it }
		binding.setVariable 'log', log
	}

	void get(){
		run()
	}

	void post(){
		run()
	}

	void run(){
		gse.run scriptName, binding
		logging = this.log.buffer
		println logging
	}

	def propertyMissing(String name, value){
		log.config "Adding $name:$value to the $scriptName binding."
		binding.setVariable name, value
	}

	def propertyMissing(String name){
		binding.getVariable name
	}

}
