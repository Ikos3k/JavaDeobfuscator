package me.ANONIMUS.deobf.transformer.impl;

import me.ANONIMUS.deobf.transformer.Transformer;
import me.ANONIMUS.deobf.util.BytecodeUtils;
import org.objectweb.asm.tree.ClassNode;

import java.util.Map;

public class FixAccessTransformer extends Transformer {
    @Override
    public void visit(Map<String, ClassNode> classMap) {
        classMap.values().forEach(classNode -> {
            classNode.access = fixAccess(classNode.access);
            classNode.methods.forEach(methodNode -> methodNode.access = fixAccess(methodNode.access));
            classNode.fields.forEach(fieldNode -> fieldNode.access = fixAccess(fieldNode.access));
        });
    }

    public int fixAccess(int access) {
        int acc = addAccess(0, ACC_PUBLIC);
        if(BytecodeUtils.isStatic(access)) {
            acc = addAccess(access, ACC_STATIC);
        }
        if(BytecodeUtils.isAbstract(access)) {
            acc = addAccess(access, ACC_ABSTRACT);
        }
        if(BytecodeUtils.isInterface(access)) {
            acc = addAccess(access, ACC_INTERFACE);
        }
        if(BytecodeUtils.isAnnotation(access)) {
            acc = addAccess(access, ACC_ANNOTATION);
        }
        if(BytecodeUtils.isSuper(access)) {
            acc = addAccess(access, ACC_SUPER);
        }
        return acc;
    }
}