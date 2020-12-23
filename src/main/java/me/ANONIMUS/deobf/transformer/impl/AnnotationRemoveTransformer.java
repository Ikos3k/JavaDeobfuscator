package me.ANONIMUS.deobf.transformer.impl;


import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.ClassNode;

import java.util.Map;

public class AnnotationRemoveTransformer extends Transformer {
    @Override
    public void visit(Map<String, ClassNode> classMap) {
        classMap.values().forEach(classNode -> {
            classNode.invisibleAnnotations = null;
            classNode.visibleAnnotations = null;

            classNode.methods.forEach(methodNode -> {
                methodNode.invisibleAnnotations = null;
                methodNode.visibleAnnotations = null;
            });

            classNode.fields.forEach(fieldNode -> {
                fieldNode.invisibleAnnotations = null;
                fieldNode.visibleAnnotations = null;
            });
        });
    }
}