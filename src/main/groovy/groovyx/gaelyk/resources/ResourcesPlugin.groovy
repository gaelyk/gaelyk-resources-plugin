package groovyx.gaelyk.resources

import groovyx.gaelyk.plugins.PluginBaseScript

class ResourcesPlugin extends PluginBaseScript {

    @Override
    public Object run() {
        get "/gpr/@path", forward: "/groovyx/gaelyk/resources/server.groovy?&path=@path"
    }

}
