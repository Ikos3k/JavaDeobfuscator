package me.ANONIMUS.deobf.transformer;

import me.ANONIMUS.deobf.util.BytecodeUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.util.Map;

public abstract class Transformer extends BytecodeUtils implements Opcodes {
    public abstract void visit(Map<String, ClassNode> classMap) throws AnalyzerException;
}