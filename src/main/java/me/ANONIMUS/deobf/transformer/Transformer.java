package me.ANONIMUS.deobf.transformer;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.util.Map;

public abstract class Transformer implements Opcodes {
    public abstract void visit(Map<String, ClassNode> classMap) throws AnalyzerException;
}