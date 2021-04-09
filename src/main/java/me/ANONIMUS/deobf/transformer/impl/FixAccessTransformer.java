package me.ANONIMUS.deobf.transformer.impl;

import me.ANONIMUS.deobf.transformer.Transformer;
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
        int acc = ACC_PUBLIC;
        if(isStatic(access)) {
            acc = addAccess(acc, ACC_STATIC);
        }
        if(isAbstract(access)) {
            acc = addAccess(acc, ACC_ABSTRACT);
        }
        if(isInterface(access)) {
            acc = addAccess(acc, ACC_INTERFACE);
        }
        return acc;
    }
}