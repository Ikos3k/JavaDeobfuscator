package me.ANONIMUS.deobf.transformer.impl;

import me.ANONIMUS.deobf.transformer.Transformer;
import me.ANONIMUS.deobf.util.BytecodeUtils;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.Map;

public class WatermarkTransformer extends Transformer {
    @Override
    public void visit(Map<String, ClassNode> classMap) {
        classMap.values().forEach(classNode -> {
            if (!BytecodeUtils.isInterface(classNode.access)) {
                classNode.methods.add(createMethod());
            }
        });
    }

    private MethodNode createMethod() {
        final MethodNode methodNode = new MethodNode(ACC_PROTECTED, "hi", "()Ljava/lang/String;", null, null);

        methodNode.visitCode();
        methodNode.visitLdcInsn("obf by ANONIMUS");
        methodNode.visitInsn(ARETURN);
        methodNode.visitEnd();

        return methodNode;
    }
}