package me.ANONIMUS.deobf.transformer.impl;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.ClassNode;

import java.util.List;

public class AnnotationRemoverTransformer extends Transformer {
    @Override
    public void visit(List<ClassNode> classMap) {
        classMap.forEach(classNode -> {
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