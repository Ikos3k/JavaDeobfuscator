package me.ANONIMUS.deobf.transformer.impl;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.util.CheckClassAdapter;

import java.util.List;

public class IllegalSignatureRemoverTransformer extends Transformer {
    @Override
    public void visit(List<ClassNode> classMap) {
        classMap.forEach(classNode -> {
            if (classNode.signature != null) {
                try {
                    CheckClassAdapter.checkClassSignature(classNode.signature);
                } catch (IllegalArgumentException | StringIndexOutOfBoundsException ignored) {
                    classNode.signature = null;
                }
            }
            classNode.methods.forEach(methodNode -> {
                if (methodNode.signature != null) {
                    try {
                        CheckClassAdapter.checkMethodSignature(methodNode.signature);
                    } catch (IllegalArgumentException | StringIndexOutOfBoundsException ignored) {
                        methodNode.signature = null;
                    }
                }
            });
            classNode.fields.forEach(fieldNode -> {
                if (fieldNode.signature != null) {
                    try {
                        CheckClassAdapter.checkFieldSignature(fieldNode.signature);
                    } catch (IllegalArgumentException | StringIndexOutOfBoundsException ignored) {
                        fieldNode.signature = null;
                    }
                }
            });
        });
    }
}