package com.tehbeard.forge.schematic.extensions;

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Provides a string -> class mapping
 * 
 * @author James
 * 
 * @param <T>
 */
public abstract class ClassCatalogue<T> {

    private Map<String, Class<? extends T>> catalogue = new HashMap<String, Class<? extends T>>();

    private Class<? extends Annotation> ann;

    public ClassCatalogue(Class<? extends Annotation> annotation) {
        this.ann = annotation;
    }

    public void addProduct(Class<? extends T> _class) {
        if (_class.getAnnotation(ann) == null)
            throw new IllegalStateException("No annotation found for class "
                    + _class.getSimpleName());
        catalogue.put(getTag(_class), _class);
    }

    protected abstract String getTag(Class<? extends T> _class);

    public Class<? extends T> get(String tag) {
        return catalogue.get(tag);
    }

    public Collection<String> getTags() {
        return catalogue.keySet();
    }

    public Collection<Class<? extends T>> getClasses() {
        return catalogue.values();
    }
}
