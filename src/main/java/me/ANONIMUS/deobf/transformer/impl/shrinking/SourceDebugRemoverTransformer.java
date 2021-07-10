package me.ANONIMUS.deobf.transformer.impl.shrinking;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.util.List;

public class SourceDebugRemoverTransformer extends Transformer {
    @Override
    public void visit(List<ClassNode> classMap) throws AnalyzerException {
        classMap.forEach(classNode -> classNode.sourceDebug = null);
    }
}