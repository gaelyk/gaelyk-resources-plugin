import groovyx.gaelyk.plugins.PluginResourceSupport;
import groovyx.gaelyk.resources.CacheHelper;
import groovyx.gaelyk.resources.MimeTypes;

import javax.servlet.http.HttpServletResponse;


def plugin = params.plugin

if(!plugin){
	response.status = HttpServletResponse.SC_BAD_REQUEST
			sout << "Missing plugin definition!"
			return
}


def path = params.path

if(!path){
	response.status = HttpServletResponse.SC_BAD_REQUEST
			sout << "Path not defined!"
			return
}

def fileURL = PluginResourceSupport.getPluginFileURL("resources", PluginResourceSupport.PLUGIN_RESOURCES_PREFIX + plugin + '/' + path)
long ifModifiedSince = request.getDateHeader("If-Modified-Since")
if (ifModifiedSince) {
	if (!CacheHelper.isModified(fileURL, ifModifiedSince)) {
		response.sendError HttpServletResponse.SC_NOT_MODIFIED
		response.setDateHeader("Last-Modified", ifModifiedSince)
		return
	}
}



try {
	response.contentType = MimeTypes.getTypeByFile(path)
	def bytes = fileURL.bytes
	response.contentLength = bytes.size()
	response.setDateHeader('Last-Modified', CacheHelper.lastDeployment)
	response.setHeader "Cache-Control", "max-age=${CacheHelper.resouceExpiration}"
	response.setDateHeader "Expires", System.currentTimeMillis() + CacheHelper.resouceExpiration * 1000
	sout << bytes
} catch (Exception e){
	log.info(e.message)
	response.status = HttpServletResponse.SC_NOT_FOUND
	sout << "File not found!"
}




