package com.tehbeard.forge.schematic.extensions;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares the dot separated path of an NBT tag to check for
 * If this tag is found then instantiate an instance of this class 
 * @author James
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SchExtension {
String name();
String checkPath();
}
