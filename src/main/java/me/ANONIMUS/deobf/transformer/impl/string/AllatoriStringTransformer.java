package me.ANONIMUS.deobf.transformer.impl.string;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LdcInsnNode;

import java.util.Map;

public class AllatoriStringTransformer extends Transformer {
    private final int mode;

    public AllatoriStringTransformer(int mode) {
        this.mode = mode;
    }

    @Override
    public void visit(Map<String, ClassNode> classMap) {
        classMap.values().forEach(classNode -> classNode.methods.forEach(methodNode -> {
            AbstractInsnNode[] abstractInsnNodes = methodNode.instructions.toArray();
            for (AbstractInsnNode abstractInsnNode : abstractInsnNodes) {
                if (abstractInsnNode.getType() == AbstractInsnNode.LDC_INSN) {
                    LdcInsnNode ldcInsnNode = (LdcInsnNode) abstractInsnNode;
                    if (ldcInsnNode.cst instanceof String) {
                        if(ldcInsnNode.getNext() != null && ldcInsnNode.getNext().getOpcode() == INVOKESTATIC) {
                            ldcInsnNode.cst = getDecryptedString((String) ldcInsnNode.cst, mode);
                            methodNode.instructions.remove(ldcInsnNode.getNext());
                        }
                    }
                }
            }
        }));
    }

    private String getDecryptedString(String string, int mode) {
        switch (mode) {
            case 0: {
                return decrypt0(string);
            }
            case 1: {
                return decrypt1(string);
            }
            case 2: {
                return decrypt2(string);
            }
            case 3: {
                return decrypt3(string);
            }
            case 4: {
                return decrypt4(string);
            }
            case 5: {
                return decrypt5(string);
            }
            case 6: {
                return decrypt6(string);
            }
            case 7: {
                return decrypt7(string);
            }
        }
        return string;
    }

    private String decrypt0(String s) {
        int n2;
        char[] array = new char[s.length()];
        int i = n2 = array.length - 1;
        int c = 115;
        while (i >= 0) {
            int n3 = n2;
            char c2 = (char)(s.charAt(n2) ^ c);
            char c3 = (char)((char)(n3 ^ c) & 0x3F);
            array[n3] = c2;
            if (--n2 < 0) break;
            int n4 = n2;
            char c4 = (char)(s.charAt(n2) ^ c3);
            c = (char)((char)(n4 ^ c3) & 0x3F);
            array[n4] = c4;
            i = --n2;
        }
        return new String(array);
    }

    private String decrypt1(String s) {
        int n2;
        char[] array = new char[s.length()];
        int i = n2 = array.length - 1;
        int c = 116;
        while (i >= 0) {
            int n3 = n2;
            char c2 = (char)(s.charAt(n2) ^ c);
            char c3 = (char)((char)(n3 ^ c) & 0x3F);
            array[n3] = c2;
            if (--n2 < 0) break;
            int n4 = n2;
            char c4 = (char)(s.charAt(n2) ^ c3);
            c = (char)((char)(n4 ^ c3) & 0x3F);
            array[n4] = c4;
            i = --n2;
        }
        return new String(array);
    }

    private String decrypt2(String s) {
        int i = s.length();
        char[] a = new char[i];
        int i0 = i - 1;
        while(true) {
            if (i0 >= 0) {
                int i1 = s.charAt(i0);
                int i2 = i0 + -1;
                int i3 = (char)(i1 ^ 105);
                a[i0] = (char)i3;
                if (i2 >= 0) {
                    i0 = i2 + -1;
                    int i4 = s.charAt(i2);
                    int i5 = (char)(i4 ^ 59);
                    a[i2] = (char)i5;
                    continue;
                }
            }
            return new String(a);
        }
    }

    private String decrypt3(String s){
        int n = s.length();
        int n2 = n - 1;
        char[] arrc = new char[n];
        int n3 = 5 << 3 ^ 2;
        int n4 = n2;
        int n5 = (3 ^ 5) << 4 ^ 2 << 1;
        while (n4 >= 0) {
            int n6 = n2--;
            arrc[n6] = (char)(s.charAt(n6) ^ n5);
            if (n2 < 0) break;
            int n7 = n2--;
            arrc[n7] = (char)(s.charAt(n7) ^ n3);
            n4 = n2;
        }
        return new String(arrc);
    }

    private String decrypt4(String a){
        final int n = (0x2 ^ 0x5) << 4;
        final int n2 = 2;
        final int n3 = n ^ (n2 << n2 ^ 0x1);
        final int n4 = (0x2 ^ 0x5) << 3 ^ 0x2;
        final int length = a.length();
        final char[] array = new char[length];
        int n5;
        int i = n5 = length - 1;
        final char c = (char)n4;
        while (i >= 0) {
            final int n7 = n5;
            final char char1 = a.charAt(n7);
            --n5;
            array[n7] = (char)(char1 ^ n3);
            if (n5 < 0) {
                break;
            }
            final int n8 = n5--;
            array[n8] = (char)(a.charAt(n8) ^ c);
            i = n5;
        }
        return new String(array);
    }

    private String decrypt5(String a){
        final int n = 4;
        final int n2 = n << n ^ 2 << 1;
        final int n3 = (0x3 ^ 0x5) << 4 ^ 0x3;
        final int length = a.length();
        final char[] array = new char[length];
        int n4;
        int i = n4 = length - 1;
        final char c = (char)n3;
        while (i >= 0) {
            final int n6 = n4;
            final char char1 = a.charAt(n6);
            --n4;
            array[n6] = (char)(char1 ^ n2);
            if (n4 < 0) {
                break;
            }
            final int n7 = n4--;
            array[n7] = (char)(a.charAt(n7) ^ c);
            i = n4;
        }
        return new String(array);
    }

    private String decrypt6(String a){
        final int n = 4;
        final int n2 = n << n ^ 3 << 1;
        final int n3 = (0x3 ^ 0x5) << 4;
        final int n4 = 1;
        final int n5 = n3 ^ n4 << n4;
        final int length = a.length();
        final char[] array = new char[length];
        int n6;
        int i = n6 = length - 1;
        final char c = (char)n5;
        while (i >= 0) {
            final int n8 = n6;
            final char char1 = a.charAt(n8);
            --n6;
            array[n8] = (char)(char1 ^ n2);
            if (n6 < 0) {
                break;
            }
            final int n9 = n6--;
            array[n9] = (char)(a.charAt(n9) ^ c);
            i = n6;
        }
        return new String(array);
    }

    private String decrypt7(String a){
        final int n = (0x3 ^ 0x5) << 3 ^ 0x5;
        final int n2 = 4;
        final int n3 = n2 << n2 ^ (0x3 ^ 0x5) << 1;
        final int length = a.length();
        final char[] array = new char[length];
        int n4;
        int i = n4 = length - 1;
        final char c = (char)n3;
        while (i >= 0) {
            final int n6 = n4;
            final char char1 = a.charAt(n6);
            --n4;
            array[n6] = (char)(char1 ^ n);
            if (n4 < 0) {
                break;
            }
            final int n7 = n4--;
            array[n7] = (char)(a.charAt(n7) ^ c);
            i = n4;
        }
        return new String(array);
    }
}