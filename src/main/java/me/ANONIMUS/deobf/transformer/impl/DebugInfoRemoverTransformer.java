package me.ANONIMUS.deobf.transformer.impl;

import me.ANONIMUS.deobf.Deobfuscator;
import me.ANONIMUS.deobf.transformer.Transformer;
import me.ANONIMUS.deobf.util.BytecodeUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.util.List;

public class DebugInfoRemoverTransformer extends Transformer {
    @Override
    public void visit(List<ClassNode> classMap) throws AnalyzerException {
        classMap.forEach(classNode -> {
            ClassNode classNodeCopy = new ClassNode();
            new ClassReader(BytecodeUtils.toByteArray(classNode)).accept(classNodeCopy, ClassReader.SKIP_DEBUG);

            Deobfuscator.getInstance().getClasses().remove(classNode);
            Deobfuscator.getInstance().getClasses().add(classNode);
        });
    }
}