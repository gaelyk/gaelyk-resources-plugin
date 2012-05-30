# Gaelyk Resources Plugin

[Gaelyk](http://gaelyk.appspot.com) Resources Plugin serves static resources packed inside
[Gaelyk's](http://gaelyk.appspot.com) binary plugins.  

#Installation

The plugin is distributed using Maven Central as `org.gaelyk:gaelyk-resources:1.1`. 
To install the plugin declare it as `compile` dependency in the Gradle build file.

```
  dependencies {
     ...
     compile 'org.gaelyk:gaelyk-resources:1.1'
     ...
  }
```


## Accessing plugin files
Any files packed in `resources` folder
of JARed binary plugin are served under `/gpr/<resourcePath>` URL of your application. 
If you have binary plugin with following structure

```
+ myPlugin.jar
  |
  + resources
    |
    + folder
      |
      + resource.xyz

```

the resource `resource.xyz` will be available for the application on


```
/gpr/folder/resource.xyz
```

## Caching

Resources are send with `Last-Modified` header with the time stamp of the original JARed file 
(e.g. the build time stamp) for better browser caching. If `If-Modified-Since` the plugin responds 
`NOT MODIFIED` until the timestamp of the JARed file changes e.g. due updated dependency.

## MIME types

MIME type is derived using [utility class MimeTypes](https://github.com/musketyr/gaelyk-resources-plugin/blob/master/src/main/groovy/groovyx/gaelyk/resources/MimeTypes.groovy).
The class still needs some polishing. Feel free to contribute new MIME type or suggest the right MIME type for
particular extension. You can do this directly from the GitHub interface using *Edit this File* button.