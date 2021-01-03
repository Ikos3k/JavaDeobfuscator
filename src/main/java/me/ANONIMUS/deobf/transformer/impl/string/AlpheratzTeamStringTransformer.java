package me.ANONIMUS.deobf.transformer.impl.string;

import me.ANONIMUS.deobf.Deobfuscator;
import me.ANONIMUS.deobf.transformer.Transformer;
import me.ANONIMUS.deobf.util.BytecodeUtils;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.*;

import java.util.Map;

public class AlpheratzTeamStringTransformer extends Transformer {
    @Override
    public void visit(Map<String, ClassNode> classMap) {
        classMap.values().forEach(classNode -> classNode.methods.forEach(methodNode -> {
            AbstractInsnNode[] abstractInsnNodes = methodNode.instructions.toArray();
            for (AbstractInsnNode abstractInsnNode : abstractInsnNodes) {
                if (abstractInsnNode.getType() == AbstractInsnNode.LDC_INSN) {
                    LdcInsnNode ldcInsnNode = (LdcInsnNode) abstractInsnNode;
                    AbstractInsnNode abInsnNodeNEXT = ldcInsnNode.getNext();
                    if (ldcInsnNode.cst instanceof String) {
                        if (abInsnNodeNEXT.getType() == AbstractInsnNode.METHOD_INSN) {
                            MethodInsnNode methodInsnNode = (MethodInsnNode) abInsnNodeNEXT;
                            if(methodInsnNode.name.toLowerCase().startsWith("i")) {
                                final Type cType = getClassType(classNode, methodInsnNode.name);
                                if (cType != null) {
                                    String className = cType.toString().replace("L", "").replace(";", "");
                                    ldcInsnNode.cst = decrypt((String) ldcInsnNode.cst, BytecodeUtils.computeConstantPoolSize(Deobfuscator.getInstance().getClasses().get(className)));
                                    methodNode.instructions.remove(abInsnNodeNEXT);
                                }
                            }
                        }
                    }
                }
            }
        }));
    }

    private Type getClassType(ClassNode classNode, String name) {
        for (MethodNode mn : classNode.methods) {
            if (mn.name.equals(name)) {
                for(AbstractInsnNode ab : mn.instructions.toArray()) {
                    if(ab.getType() == AbstractInsnNode.METHOD_INSN) {
                        MethodInsnNode mn2 = (MethodInsnNode) ab;
                        if(ab.getNext().getType() == AbstractInsnNode.LDC_INSN) {
                            LdcInsnNode ldcInsnNode = (LdcInsnNode) ab.getNext();
                            if(mn2.owner.equals("sun/misc/SharedSecrets")) {
                                if(ldcInsnNode.cst instanceof Type) {
                                    return (Type) ldcInsnNode.cst;
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    private String decrypt(final String str, int key) {
        final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        final char[] c = str.toCharArray();
        for(int i = 0; i < c.length; i++) {
            c[i] ^= (char) key;
            c[i] ^= (char)stackTrace[0].getClassName().hashCode();
            c[i] ^= (char)stackTrace[1].getClassName().hashCode();
        }
        return new String(c);
    }
}