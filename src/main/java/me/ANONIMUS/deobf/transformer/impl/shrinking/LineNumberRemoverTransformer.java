package me.ANONIMUS.deobf.transformer.impl.shrinking;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LineNumberNode;

import java.util.Iterator;
import java.util.List;

public class LineNumberRemoverTransformer extends Transformer {
    @Override
    public void visit(List<ClassNode> classMap) {
        classMap.forEach(classNode -> classNode.methods.forEach(methodNode -> {
            Iterator<AbstractInsnNode> it = methodNode.instructions.iterator();
            while (it.hasNext()) {
                if (it.next() instanceof LineNumberNode) {
                    it.remove();
                }
            }
        }));
    }
}