package me.ANONIMUS.deobf.transformer.impl.string;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.LdcInsnNode;

import java.util.Map;

public class qProtectStringTransformer extends Transformer {
    @Override
    public void visit(Map<String, ClassNode> classMap) {
        classMap.values().forEach(classNode -> classNode.methods.forEach(methodNode -> {
            for(AbstractInsnNode insn : methodNode.instructions.toArray()) {
                if(insn instanceof LdcInsnNode) {
                    if(((LdcInsnNode) insn).cst instanceof String) {
                        if(insn.getPrevious() instanceof InsnNode && insn.getPrevious().getPrevious() instanceof LdcInsnNode) {
                             methodNode.instructions.remove(insn.getNext());
                             methodNode.instructions.set(insn, new LdcInsnNode(decode((String) ((LdcInsnNode) insn).cst)));
                        }
                    }
                }
            }
        }));
    }

    private String decode(String var0) {
        try {
            char[] var1 = var0.toCharArray();
            int[] var3 = new int[] {18, 42886, 42197, 40977, 43365, 32022, 36394, 9242, 35671, 13335, 18};
            for(int i = 0; i < var1.length; i++) {
                var1[i] ^= var3[i];
            }
            return new String(var1);
        } catch (Exception var7) {
            return var0;
        }
    }
}