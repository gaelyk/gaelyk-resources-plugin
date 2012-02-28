package groovyx.gaelyk.resources

import com.google.appengine.api.NamespaceManager;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Text;
import com.google.appengine.api.datastore.Category;
import com.google.appengine.api.datastore.Query.FilterOperator;

import groovyx.gaelyk.GaelykCategory;

class ConsoleScriptRepository {

	static final String KIND = 'script'
	static final String TEXT = 'text'
	static final String TAGS = 'tags'
	static final String NAMESPACE = 'gaelyk_console'

	static Key save(String name, String scriptText, String... tags){
		if(!((name+tags.join('')) =~ /[a-zA-Z0-9_\- ]/)){
			throw new IllegalArgumentException("Only letters, numbers, underscore, tilda and space is allowed as name or tag")
		}
		def oldNs = NamespaceManager.get()
		NamespaceManager.set(NAMESPACE)
		try {
			Entity en = new Entity(KIND, name);
			en.setUnindexedProperty(TEXT, new Text(scriptText))
			en.setProperty(TAGS, tags.collect{ new Category(it)} ?: [])
			return DatastoreServiceFactory.getDatastoreService().put(en);
		} finally {
			NamespaceManager.set(oldNs)
		}
	}
	
	static load(String name){
		def oldNs = NamespaceManager.get()
		NamespaceManager.set(NAMESPACE)
		Entity script = null
		try {
			script = DatastoreServiceFactory.datastoreService.get(KeyFactory.createKey(KIND, name))
		} catch(EntityNotFoundException e) {
			// script is null
		} finally {
			NamespaceManager.set(oldNs)
		}
		asConsoleScript(script)
	}
	
	static void delete (String name){
		def oldNs = NamespaceManager.get()
		NamespaceManager.set(NAMESPACE)
		Entity script = null
		try {
			DatastoreServiceFactory.datastoreService.delete(KeyFactory.createKey(KIND, name))
		} finally {
			NamespaceManager.set(oldNs)
		}
	}
	
	static list(String... tags){
		def oldNs = NamespaceManager.get()
		NamespaceManager.set(NAMESPACE)
		try {
			Query q = new Query(KIND)
			for(tag in tags){
				q.addFilter(TAGS, FilterOperator.EQUAL, tag)
			}
			PreparedQuery pq = DatastoreServiceFactory.datastoreService.prepare(q);
			pq.asList(FetchOptions.Builder.withDefaults()).collect{ asConsoleScript(it)}
		} finally {
			NamespaceManager.set(oldNs)
		}
	}

	private static asConsoleScript(Entity script) {
		if(!script){
			return null;
		}
		def ret = [name: script.key.name]
		ret.text = script.getProperty(TEXT)?.value
		ret.tags = script.getProperty(TAGS)*.category
		ret
	}
	
	
}
