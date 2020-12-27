package me.ANONIMUS.deobf.transformer.impl;

import me.ANONIMUS.deobf.transformer.Transformer;
import me.ANONIMUS.deobf.util.BytecodeUtils;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Map;

public class WatermarkTransformer extends Transformer {
    @Override
    public void visit(Map<String, ClassNode> classMap) {
        final MethodNode methodNode = createMethod();
        classMap.values().stream().filter(classNode -> !BytecodeUtils.isInterface(classNode.access)).forEach(classNode -> classNode.methods.add(methodNode));
    }

    private MethodNode createMethod() {
        final MethodNode methodNode = new MethodNode(ACC_PROTECTED, "hi", createDescription(DESC_STRING, DESC_EMPTY), null, null);
        methodNode.visitCode();
        methodNode.visitLdcInsn("github: " + "https://github.com/Ikos3k");
        methodNode.visitVarInsn(ASTORE, 0);
        methodNode.visitLdcInsn("obf by ANONIMUS");
        methodNode.visitInsn(ARETURN);
        methodNode.visitEnd();
        return methodNode;
    }
}