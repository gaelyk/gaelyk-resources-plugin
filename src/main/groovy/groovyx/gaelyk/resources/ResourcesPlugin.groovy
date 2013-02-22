package groovyx.gaelyk.resources

import groovyx.gaelyk.plugins.PluginBaseScript

class ResourcesPlugin extends PluginBaseScript {
    
    /**
     * This plugin is one of the core plugins, so the number is close to {@link Integer#MIN_VALUE}
     */
    static final int FIRST_ROUTE_INDEX = -60000
    
    @Override
    public Object run() {
        startRoutingAt FIRST_ROUTE_INDEX
        get "/gpr/@path", forward: "/groovyx/gaelyk/resources/server.groovy?&path=@path"
    }

}
