# Gaelyk Resources Plugin

[Gaelyk](http://gaelyk.appspot.com) Resources Plugin serves static resources packed inside
[Gaelyk's](http://gaelyk.appspot.com) binary plugins.  

## Accessing plugin files
Any files packed in `META-INF/gaelyk-plugins/resources/` folder
of JARed binary plugin are served under `/gpr/<pluginName>/resource` URL of your application. 
If you have binary plugin with following structure

```
+ myPlugin.jar
|
+ META-INF/gaelyk-plugins
  |
  + myPlugin.groovy
  |
  + resources
    |
    + folder
      |
      + resource.xyz

```

the resource `resource.xyz` will be available for the application on


```
/gpr/myPlugin/folder/resource.xyz
```

## Caching

Resources are send with `Last-Modified` header with the time stamp of the original JARed file 
(e.g. the build time stamp) for better browser caching. If `If-Modified-Since` the plugin responds 
`NOT MODIFIED` until the timestamp of the JARed file changes e.g. due updated dependency.

## MIME types

MIME type is derived using [utility class MimeTypes](https://github.com/musketyr/gaelyk-resources-plugin/blob/master/src/main/groovy/groovyx/gaelyk/resources/MimeTypes.groovy).
The class still needs some polishing. Feel free to contribute new MIME type or suggest the right MIME type for
particular extension. You can do this directly from the GitHub interface using *Edit this File* button.

## Overriding plugin resources

You can always override the resource from the plugin for example for having different theme of CSS library. To do
so declare new route in `routes.groovy` which will redirect to your resource. Do not use forward because it
may harm the dependencies of given file (e.g. icons).

```
get '/gpr/pluginName/folder/resource.xyz', redirect: '/WEB-INF/xyzs/myResource.xyz'
```