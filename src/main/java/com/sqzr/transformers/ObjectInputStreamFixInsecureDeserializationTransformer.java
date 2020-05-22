package com.sqzr.transformers;

import com.sqzr.FixInsecureDeserializationTransformer;
import org.objectweb.asm.ClassVisitor;

/**
 *
 */
public class ObjectInputStreamFixInsecureDeserializationTransformer implements FixInsecureDeserializationTransformer {
    public boolean shouldRetransform(Class clazz) {
        return "java.io.ObjectInputStream".equals(clazz.getName());
    }

    public boolean shouldTransform(String className) {
        return "java/io/ObjectInputStream".equals(className);
    }

    public ClassVisitor createClassVisitor(ClassVisitor writer) {
        return new ObjectInputStreamClassVisitor(writer);
    }
}
