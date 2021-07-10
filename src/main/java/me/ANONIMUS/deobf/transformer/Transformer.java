package me.ANONIMUS.deobf.transformer;

import me.ANONIMUS.deobf.util.BytecodeUtils;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.util.List;

public abstract class Transformer extends BytecodeUtils implements Opcodes {
    public abstract void visit(List<ClassNode> classMap) throws AnalyzerException;
}