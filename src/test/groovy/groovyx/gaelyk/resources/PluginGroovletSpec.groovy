package groovyx.gaelyk.resources

import groovyx.gaelyk.spock.GaelykUnitSpec

abstract class PluginGroovletSpec extends GaelykUnitSpec {

	StringWriter sw = []
	
	def setup(){
		out = new PrintWriter(sw)
		sout = new ByteArrayOutputStream()
		groovlet getGroovletName()
	}
	
	def groovlet = {
		groovletInstance = new PluginGroovletUnderSpec("$it")

		[ 'sout', 'out', 'response', 'datastore', 'memcache', 'mail', 'urlFetch', 'images', 'users', 'user', 'defaultQueue', 'queues', 'xmpp',
		  'blobstore', 'files', 'oauth', 'channel', 'capabilities', 'namespace', 'localMode', 'app', 'backends', 'lifecycle'
		].each { groovletInstance."$it" = this."$it" }

		this.metaClass."${it.tokenize('.').first()}" = groovletInstance
	}
	
	abstract String getGroovletName()
	

}
