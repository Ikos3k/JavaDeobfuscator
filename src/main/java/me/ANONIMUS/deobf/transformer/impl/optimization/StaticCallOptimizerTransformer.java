package me.ANONIMUS.deobf.transformer.impl.optimization;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;

import java.util.Map;

public class StaticCallOptimizerTransformer extends Transformer {
    @Override
    public void visit(Map<String, ClassNode> classMap) {
        classMap.values().forEach(classNode -> classNode.methods.forEach(methodNode -> {
            for (AbstractInsnNode insnNode : methodNode.instructions.toArray()) {
                if (insnNode instanceof MethodInsnNode) {
                    MethodInsnNode methodInsnNode = (MethodInsnNode) insnNode;
                    AbstractInsnNode prev = methodInsnNode.getPrevious();

                    if(prev instanceof LdcInsnNode && ((LdcInsnNode) prev).cst instanceof String) {
                        if(matchMethodNode(methodInsnNode, "java/lang/Object.hashCode:()I") || matchMethodNode(methodInsnNode, "java/lang/String.hashCode:()I")) {
                            methodNode.instructions.insert(insnNode, getNumberInsn(((LdcInsnNode) prev).cst.hashCode()));
                            methodNode.instructions.remove(insnNode);
                            methodNode.instructions.remove(prev);
                        } else if (matchMethodNode(methodInsnNode, "java/lang/String.length:()I")) {
                            methodNode.instructions.insert(insnNode, getNumberInsn(((String) ((LdcInsnNode) prev).cst).length()));
                            methodNode.instructions.remove(insnNode);
                            methodNode.instructions.remove(prev);
                        } else if (matchMethodNode(methodInsnNode, "java/lang/String.toUpperCase:()Ljava/lang/String;")) {
                            methodNode.instructions.insert(insnNode, new LdcInsnNode(((String) ((LdcInsnNode) prev).cst).toUpperCase()));
                            methodNode.instructions.remove(insnNode);
                            methodNode.instructions.remove(prev);
                        } else if(matchMethodNode(methodInsnNode, "java/lang/String.toLowerCase:()Ljava/lang/String;")) {
                            methodNode.instructions.insert(insnNode, new LdcInsnNode(((String) ((LdcInsnNode) prev).cst).toLowerCase()));
                            methodNode.instructions.remove(insnNode);
                            methodNode.instructions.remove(prev);
                        }
                    }
                }
            }
        }));
    }
}