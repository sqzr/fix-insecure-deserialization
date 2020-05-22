package com.sqzr;

import org.objectweb.asm.ClassVisitor;

/**
 *
 */
public interface FixInsecureDeserializationTransformer {

    boolean shouldRetransform(Class clazz);
    boolean shouldTransform(String className);
    ClassVisitor createClassVisitor(ClassVisitor writer);
}
