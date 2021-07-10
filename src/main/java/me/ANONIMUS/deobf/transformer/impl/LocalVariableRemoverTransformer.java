package me.ANONIMUS.deobf.transformer.impl;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.ClassNode;

import java.util.List;

public class LocalVariableRemoverTransformer extends Transformer {
    @Override
    public void visit(List<ClassNode> classMap) {
        classMap.forEach(classNode -> classNode.methods.forEach(methodNode -> methodNode.localVariables = null));
    }
}