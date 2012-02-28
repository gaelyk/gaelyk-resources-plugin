package groovyx.gaelyk.resources

import spock.lang.Specification
import spock.lang.Unroll;

class MimeTypesTest extends Specification {
	
	@Unroll({ "Ext $ext belongs to $mime"})
	def "Random mimes test"(){
		expect:
		mime == MimeTypes.getTypeByExt(ext)
		where:
		ext		| mime
		'png'	| 'image/png'
		'exe'	| 'application/octet-stream'
	}
	
	@Unroll({ "File $file is $mime"})
	def "Random files mimes test"(){
		expect:
		mime == MimeTypes.getTypeByFile(file)
		where:
		file					| mime
		'/some.root/file.png'	| 'image/png'
		'\\what\\the\\hell.exe'	| 'application/octet-stream'
	}

}
