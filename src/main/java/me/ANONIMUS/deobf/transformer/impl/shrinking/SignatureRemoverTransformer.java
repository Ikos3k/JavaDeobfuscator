package me.ANONIMUS.deobf.transformer.impl.shrinking;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.util.List;

public class SignatureRemoverTransformer extends Transformer {
    @Override
    public void visit(List<ClassNode> classMap) throws AnalyzerException {
        classMap.forEach(classNode -> {
            classNode.signature = null;
            classNode.methods.forEach(methodNode -> methodNode.signature = null);
            classNode.fields.forEach(fieldNode -> fieldNode.signature = null);
        });
    }
}
