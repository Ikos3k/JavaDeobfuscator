package me.ANONIMUS.deobf.transformer.impl;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;

import java.util.Map;

public class IllegalVarargsRemoverTransformer extends Transformer {
    @Override
    public void visit(Map<String, ClassNode> classMap) {
        classMap.values().forEach(classNode -> classNode.methods.forEach(methodNode -> {
            Type[] args = Type.getArgumentTypes(methodNode.desc);
            if (args.length > 0 && args[args.length - 1].getSort() != Type.ARRAY) {
                methodNode.access = removeAccess(methodNode.access, ACC_VARARGS);
            }
        }));
    }
}