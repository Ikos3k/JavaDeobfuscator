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
        int acc = 0;
        if(BytecodeUtils.isPublic(access)) {
            acc = addAccess(ACC_PUBLIC);
        } else if(BytecodeUtils.isPrivate(access)) {
            acc = addAccess(ACC_PRIVATE);
        } else if(BytecodeUtils.isProtected(access)) {
            acc = addAccess(ACC_PROTECTED);
        }
        if(BytecodeUtils.isStatic(access)) {
            acc = addAccess(ACC_STATIC);
        }
        if(BytecodeUtils.isAbstract(access)) {
            acc = addAccess(ACC_ABSTRACT);
        }
        if(BytecodeUtils.isInterface(access)) {
            acc = addAccess(ACC_INTERFACE);
        }
        if(BytecodeUtils.isAnnotation(access)) {
            acc = addAccess(ACC_ANNOTATION);
        }
        if(BytecodeUtils.isSuper(access)) {
            acc = addAccess(ACC_SUPER);
        }
        return acc;
    }
}