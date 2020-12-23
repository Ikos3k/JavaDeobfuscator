package me.ANONIMUS.deobf.util;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;

public class Utils {
    public static boolean hasInstructions(InsnList insnList, int[] opcodes) {
        AbstractInsnNode[] abstractInsnNodes = insnList.toArray();
        if(opcodes.length > abstractInsnNodes.length) {
            return false;
        }

        int i = 0;
        for(int opcode : opcodes) {
            for(AbstractInsnNode ab : abstractInsnNodes) {
                if(ab.getOpcode() == opcode) {
                    i++;
                }
            }
        }
        return i >= opcodes.length;
    }

    public static boolean hasStrings(InsnList insnList, String[] strings) {
        int i = 0;
        for(String s : strings) {
            for (AbstractInsnNode ab : insnList.toArray()) {
                if (ab.getOpcode() == Opcodes.LDC) {
                    LdcInsnNode ldcInsnNode = (LdcInsnNode) ab;
                    if(ldcInsnNode.cst.equals(s)) {
                        i++;
                    }
                }
            }
        }
        return i >= strings.length;
    }
}