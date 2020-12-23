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
            classNode.methods.forEach(methodNode -> {
               methodNode.access = fixAccess(methodNode.access);
            });
            classNode.fields.forEach(fieldNode -> {
               fieldNode.access = fixAccess(fieldNode.access);
            });
        });
    }

    public int fixAccess(int access) {
        int acc = 0;
        if(BytecodeUtils.isPublic(access)) {
            acc |= ACC_PUBLIC;
        } else if(BytecodeUtils.isPrivate(access)) {
            acc |= ACC_PRIVATE;
        } else if(BytecodeUtils.isProtected(access)) {
            acc |= ACC_PROTECTED;
        }
        return acc;
    }
}