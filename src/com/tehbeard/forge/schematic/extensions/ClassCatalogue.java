package com.tehbeard.forge.schematic.extensions;


import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides a string -> class mapping
 * @author James
 *
 * @param <T>
 */
public class ClassCatalogue<T> {
    
    private Map<String,Class<? extends T>> catalogue = new HashMap<String, Class<? extends T>>();
    
    public void addProduct(Class<? extends T> _class){
        if(_class.getAnnotation(SchExtension.class) == null){throw new IllegalStateException("No @SchExtension found for class " + _class.getSimpleName());}
        catalogue.put(_class.getAnnotation(SchExtension.class).checkPath(), _class);
    }
    
    public Class<? extends T> get(String tag){
        return catalogue.get(tag);
    }
    
    public Collection<String> getTags(){
        return catalogue.keySet();
    }
    
    public Collection<Class<? extends T>> getClasses(){
    	return catalogue.values();
    }
}
