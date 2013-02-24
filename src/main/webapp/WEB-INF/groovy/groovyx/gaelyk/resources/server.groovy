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

import java.text.SimpleDateFormat

import groovyx.gaelyk.resources.CacheHelper
import groovyx.gaelyk.resources.MimeTypes

import javax.servlet.http.HttpServletResponse

def path = params.path

if(!path){
    response.status = HttpServletResponse.SC_BAD_REQUEST
    out << "Path not defined!"
    return
}

def fileURL = getClass().getResource('/resources/' + path)
String iMSHeader = request.getHeader('If-Modified-Since')

if (iMSHeader) {
    long ifModifiedSince = CacheHelper.getMillisFromHeaderDate(iMSHeader)
    if (!CacheHelper.isModified(fileURL, ifModifiedSince)) {
        response.sendError HttpServletResponse.SC_NOT_MODIFIED
        response.setHeader('Last-Modified', CacheHelper.getHeaderDateFromMillis(ifModifiedSince))
        return
    }
}

try {
    if(fileURL == null){
        throw new IOException("File not found!")
    }
    response.contentType = MimeTypes.getTypeByFile(path)
    def bytes = fileURL.bytes
    response.contentLength = bytes.size()
    response.setHeader('Last-Modified', CacheHelper.getHeaderDateFromMillis(CacheHelper.lastDeployment))
    response.setHeader 'Cache-Control', "max-age=${CacheHelper.resouceExpiration}"
    response.setHeader 'Expires', CacheHelper.getHeaderDateFromMillis(System.currentTimeMillis() + CacheHelper.resouceExpiration * 1000)
    sout << bytes
} catch (IOException e){
    log.info(e.message)
    response.status = HttpServletResponse.SC_NOT_FOUND
    out << "File not found!"
} catch (Exception e){
    log.warning e.message
    e.printStackTrace(out)
    response.status = HttpServletResponse.SC_INTERNAL_SERVER_ERROR
}




