package me.ANONIMUS.deobf.transformer.impl;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.analysis.Analyzer;
import org.objectweb.asm.tree.analysis.AnalyzerException;
import org.objectweb.asm.tree.analysis.BasicInterpreter;
import org.objectweb.asm.tree.analysis.Frame;

import java.util.Map;

public class InvalidTryCatchRemoverTransformer extends Transformer {
    @Override
    public void visit(Map<String, ClassNode> classMap) throws AnalyzerException {
        classMap.values().forEach(classNode -> classNode.methods.forEach(methodNode -> {
            methodNode.tryCatchBlocks.removeIf(tcb -> tcb.start == tcb.end || methodNode.instructions.indexOf(tcb.start) >= methodNode.instructions.indexOf(tcb.end));

            Analyzer<?> analyzer = new Analyzer<>(new BasicInterpreter());
            try {
                analyzer.analyze(classNode.name, methodNode);
            } catch (AnalyzerException ignored) {
                return;
            }
            Frame<?>[] frames = analyzer.getFrames();
            AbstractInsnNode[] insns = methodNode.instructions.toArray();
            for (int i = 0; i < frames.length; i++) {
                AbstractInsnNode insn = insns[i];
                if (frames[i] == null && insn.getType() != AbstractInsnNode.LABEL) {
                    methodNode.instructions.remove(insn);
                    insns[i] = null;
                }
            }
        }));
    }
}