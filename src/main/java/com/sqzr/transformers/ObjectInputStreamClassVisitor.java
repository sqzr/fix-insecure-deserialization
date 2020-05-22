package com.sqzr.transformers;

import com.sqzr.Options;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.ObjectStreamClass;

/**
 *
 */
public class ObjectInputStreamClassVisitor extends ClassVisitor {

    private final String resolveClassDesc = "(Ljava/io/ObjectStreamClass;)Ljava/lang/Class;";

    private final String callBackDescriptor = "(Ljava/io/ObjectStreamClass;)Ljava/io/ObjectStreamClass;";

    public ObjectInputStreamClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM5, cv);
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
        return new ResolveClassCallSiteVisitor(mv);
    }

    public static ObjectStreamClass onBeforeResolveClass(ObjectStreamClass desc) {
        String className = desc.getName();
        Options.getInstance().getFixInsecureDeserialization().onBeforeResolveClass(className);
        return desc;
    }

    private class ResolveClassCallSiteVisitor extends MethodVisitor {
        public ResolveClassCallSiteVisitor(MethodVisitor mv) {
            super(Opcodes.ASM5, mv);
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String desc, boolean itf) {
            if (name.equals("resolveClass") && resolveClassDesc.equals(desc)) {
                mv.visitMethodInsn(Opcodes.INVOKESTATIC, Type.getType(ObjectInputStreamClassVisitor.class).getInternalName(), "onBeforeResolveClass", callBackDescriptor, false);
            }
            super.visitMethodInsn(opcode, owner, name, desc, itf);
        }
    }
}
