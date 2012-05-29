package groovyx.gaelyk.resources

import spock.lang.Specification
import spock.lang.Unroll;

class MimeTypesTest extends Specification {
	
	@Unroll
	def  "Ext #ext belongs to #mime"(){
		expect:
		mime == MimeTypes.getTypeByExt(ext)
		where:
		ext		| mime
		'png'	| 'image/png'
		'exe'	| 'application/octet-stream'
	}
	
	@Unroll
	def "File #file is #mime"(){
		expect:
		mime == MimeTypes.getTypeByFile(file)
		where:
		file					| mime
		'/some.root/file.png'	| 'image/png'
		'\\what\\the\\hell.exe'	| 'application/octet-stream'
	}

}
