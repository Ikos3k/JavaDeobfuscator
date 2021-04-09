package me.ANONIMUS.deobf.transformer.impl.shrinking;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.util.Map;

public class SourceDebugRemoverTransformer extends Transformer {
    @Override
    public void visit(Map<String, ClassNode> classMap) throws AnalyzerException {
        classMap.values().forEach(classNode -> classNode.sourceDebug = null);
    }
}