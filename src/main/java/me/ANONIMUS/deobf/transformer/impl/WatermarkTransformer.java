package me.ANONIMUS.deobf.transformer.impl;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.List;

public class WatermarkTransformer extends Transformer {
    @Override
    public void visit(List<ClassNode> classMap) {
        final MethodNode methodNode = createMethod();
        classMap.stream().filter(classNode -> !isInterface(classNode.access) && !isAbstract(classNode.access) && !isAnnotation(classNode.access)).forEach(classNode -> classNode.methods.add(methodNode));
    }

    private MethodNode createMethod() {
        final MethodNode methodNode = new MethodNode(ACC_PROTECTED, "hi", createDescription(DESC_STRING, DESC_EMPTY), null, null);
        methodNode.visitCode();
        methodNode.visitLdcInsn("github: " + "https://github.com/Ikos3k");
        methodNode.visitVarInsn(ASTORE, 0);
        methodNode.visitLdcInsn("deobf by ANONIMUS(Ikos3k)");
        methodNode.visitInsn(ARETURN);
        methodNode.visitEnd();
        return methodNode;
    }
}