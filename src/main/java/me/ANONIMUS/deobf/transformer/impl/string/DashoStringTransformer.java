package me.ANONIMUS.deobf.transformer.impl.string;

import lombok.AllArgsConstructor;
import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.util.List;

@AllArgsConstructor
public class DashoStringTransformer extends Transformer {
    private final int mode;

    @Override
    public void visit(List<ClassNode> classMap) throws AnalyzerException {
        classMap.forEach(classNode -> classNode.methods.forEach(methodNode -> {
            for (AbstractInsnNode insn : methodNode.instructions.toArray()) {
                if (insn instanceof LdcInsnNode && ((LdcInsnNode) insn).cst instanceof String && insn.getNext() != null && isIntInsn(insn.getNext())) {
                    AbstractInsnNode abstractInsnNodeNEXT = insn.getNext();
                    methodNode.instructions.set(abstractInsnNodeNEXT, new LdcInsnNode(getDecryptedString((String) ((LdcInsnNode) insn).cst, getIntNumber(abstractInsnNodeNEXT), mode)));
                    methodNode.instructions.remove(insn);
                }
            }
        }));
    }

    private String getDecryptedString(String string, int key, int mode) {
        switch (mode) {
            case 0: {
                return decrypt0(string, key);
            }
            case 1: {
                return decrypt1(string, key);
            }
        }
        return string;
    }

    private String decrypt0(String string, int n) {
        char[] cArray = string.toCharArray();
        int n2 = cArray.length;
        int n3 = 0;
        int n4 = (4 << 4 + 1) - 1 ^ 0x20;
        while (n3 != n2) {
            int n5 = n3++;
            int n6 = n & n4 ^ cArray[n5];
            ++n;
            cArray[n5] = (char) n6;
        }
        return String.valueOf(cArray, 0, n2).intern();
    }

    private String decrypt1(String string, int n) {
        n += 5;
        char[] cArray = string.toCharArray();
        int n2 = cArray.length;
        int n3 = 0;
        int n4 = (4 << 1 + 4) - 1 ^ 0x20;
        while (n3 != n2) {
            int n5 = n3++;
            int n6 = cArray[n5] ^ n & n4;
            n += 7;
            cArray[n5] = (char) n6;
        }
        return String.valueOf(cArray, 0, n2).intern();
    }
}