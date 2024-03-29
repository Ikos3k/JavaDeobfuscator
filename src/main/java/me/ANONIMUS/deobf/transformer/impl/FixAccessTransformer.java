package me.ANONIMUS.deobf.transformer.impl;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.ClassNode;

import java.util.List;

public class FixAccessTransformer extends Transformer {
    @Override
    public void visit(List<ClassNode> classMap) {
        classMap.forEach(classNode -> {
            classNode.access = fixAccess(classNode.access);
            classNode.methods.forEach(methodNode -> {
                methodNode.access = fixAccess(methodNode.access);
                if (methodNode.parameters != null) {
                    methodNode.parameters.forEach(pn -> pn.access = fixAccess(pn.access));
                }
            });
            classNode.fields.forEach(fieldNode -> fieldNode.access = fixAccess(fieldNode.access));
        });
    }

    public int fixAccess(int access) {
        int acc = ACC_PUBLIC;
        if (isStatic(access)) {
            acc = addAccess(acc, ACC_STATIC);
        }
        if (isAbstract(access)) {
            acc = addAccess(acc, ACC_ABSTRACT);
        }
        if (isInterface(access)) {
            acc = addAccess(acc, ACC_INTERFACE);
        }
        return acc;
    }
}