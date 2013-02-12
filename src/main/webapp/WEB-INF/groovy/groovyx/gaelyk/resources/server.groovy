/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package groovyx.gaelyk.resources
 
import groovyx.gaelyk.resources.CacheHelper
import groovyx.gaelyk.resources.MimeTypes

import javax.servlet.http.HttpServletResponse

def path = params.path

if(!path){
    response.status = HttpServletResponse.SC_BAD_REQUEST
    sout << "Path not defined!"
    return
}

def fileURL = getClass().getResource('/resources/' + path)

// templorary disbaled thx to http://code.google.com/p/googleappengine/issues/detail?id=8415
//long ifModifiedSince = request.getDateHeader("If-Modified-Since")
//if (ifModifiedSince) {
//    if (!CacheHelper.isModified(fileURL, ifModifiedSince)) {
//        response.sendError HttpServletResponse.SC_NOT_MODIFIED
//        response.setDateHeader("Last-Modified", ifModifiedSince)
//        return
//    }
//}



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




