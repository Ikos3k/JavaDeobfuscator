package me.ANONIMUS.deobf.util;

import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.InsnList;
import org.objectweb.asm.tree.LdcInsnNode;

import java.util.Arrays;
import java.util.Random;
import java.util.stream.IntStream;

public class SimpleAsmUtils {
    public String createDescription(String returnType, String... arguments) {
        StringBuilder desc = new StringBuilder();
        if (arguments.length > 0)
            desc.append("(");
        if (!returnType.equals("["))
            Arrays.stream(arguments).forEach(desc::append);
        else
            IntStream.range(1, arguments.length).forEach(i -> desc.append(arguments[i]));
        if (arguments.length > 0)
            desc.append(")");
        desc.append(returnType);
        if(returnType.equals("["))
            desc.append(arguments[0]);
        return desc.toString();
    }

    public String randomDescription() {
        String[] descriptions = new String[]{
                "Ljava/lang/Object;", "Ljava/lang/String;", "Ljava/lang/Integer;", "Ljava/lang/Class;", "I", "J", "F", "D", "S", "Z", "C", "B"
        };
        return descriptions[new Random().nextInt(descriptions.length)];
    }

    public int addAccess(int access, int... add) {
        for (int a : add) {
            if ((access & a) == 0) {
                access |= a;
            }
        }
        return access;
    }

    public int removeAccess(int access, int... remove) {
        for (int r : remove) {
            if ((access & r) != 0) {
                access &= ~r;
            }
        }
        return access;
    }

    public interface descriptions {
        String DESC_EMPTY = "";
        String DESC_VOID = "V";
        String DESC_OBJECT = "Ljava/lang/Object;";
        String DESC_STRING = "Ljava/lang/String;";
        String DESC_INTEGER = "Ljava/lang/Integer;";
        String DESC_CLASS = "Ljava/lang/Class;";
        String DESC_INT = "I";
        String DESC_LONG = "J";
        String DESC_FLOAT = "F";
        String DESC_DOUBLE = "D";
        String DESC_SHORT = "S";
        String DESC_BOOLEAN = "Z";
        String DESC_CHAR = "C";
        String DESC_BYTE = "B";
        String DESC_ARRAY = "[";
    }

    public boolean hasInstructions(InsnList insnList, int... opcodes) {
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

    public boolean hasStrings(InsnList insnList, String... strings) {
        int i = 0;
        for(String s : strings) {
            for (AbstractInsnNode ab : insnList.toArray()) {
                if (ab.getType() == AbstractInsnNode.LDC_INSN) {
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