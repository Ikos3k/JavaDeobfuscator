package me.ANONIMUS.deobf.transformer.impl;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.Attribute;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.util.Arrays;
import java.util.Map;

public class UnknownAttributeRemoverTransformer extends Transformer {
    @Override
    public void visit(Map<String, ClassNode> classMap) throws AnalyzerException {
        Arrays.stream(classMap.values().toArray(new ClassNode[0])).filter(classNode -> classNode.attrs != null).forEach(classNode -> classNode.attrs.stream().filter(Attribute::isUnknown).forEach(attr -> classNode.attrs.remove(attr)));
    }
}