package me.ANONIMUS.deobf.transformer.impl.shrinking;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.ClassNode;

import java.util.List;

public class InnerClassTransformer extends Transformer {
    @Override
    public void visit(List<ClassNode> classMap) {
        classMap.forEach(classNode -> {
            classNode.outerClass = null;
            classNode.outerMethod = null;
            classNode.outerMethodDesc = null;
            classNode.innerClasses.clear();
        });
    }
}