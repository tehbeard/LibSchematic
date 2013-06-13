package com.tehbeard.forge.schematic.extensions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation declaring data about a SchematicExtension
 * @author James
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SchExtension {
    /**
     * Name of schematic extension for logging purposes
     * @return
     */
String name();
/**
 * Dot separated path of an NBT element to look for, if found then load this extension
 * @return
 */
String checkPath();
}
