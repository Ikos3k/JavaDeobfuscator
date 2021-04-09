package me.ANONIMUS.deobf.transformer.impl;

import me.ANONIMUS.deobf.Deobfuscator;
import me.ANONIMUS.deobf.transformer.Transformer;
import me.ANONIMUS.deobf.util.BytecodeUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.util.Map;

public class DebugInfoRemoverTransformer extends Transformer {
    @Override
    public void visit(Map<String, ClassNode> classMap) throws AnalyzerException {
       classMap.values().forEach(classNode -> {
            ClassNode classNodeCopy = new ClassNode();
            new ClassReader(BytecodeUtils.toByteArray(classNode)).accept(classNodeCopy, ClassReader.SKIP_DEBUG);

            Deobfuscator.getInstance().getClasses().replace(classNode.name, classNodeCopy);
        });
    }
}