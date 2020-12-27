package me.ANONIMUS.deobf.transformer.impl.string;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;

public class SuperBlaubeere27StringTransformer extends Transformer {
    @Override
    public void visit(Map<String, ClassNode> classMap) {
        classMap.values().forEach(classNode -> classNode.methods.forEach(methodNode -> {
            AbstractInsnNode[] abstractInsnNodes = methodNode.instructions.toArray();
            for (AbstractInsnNode abstractInsnNode : abstractInsnNodes) {
                if (abstractInsnNode.getType() == AbstractInsnNode.LDC_INSN) {
                    LdcInsnNode ldcInsnNode = (LdcInsnNode) abstractInsnNode;
                    if (ldcInsnNode.getNext() != null && ldcInsnNode.getNext() instanceof LdcInsnNode) {
                        LdcInsnNode ldcInsnNodeNext = (LdcInsnNode) ldcInsnNode.getNext();
                        if (ldcInsnNodeNext.getNext().getOpcode() == INVOKESTATIC) {
                            String name = ((MethodInsnNode) ldcInsnNodeNext.getNext()).name;
                            for (MethodNode mn : classNode.methods) {
                                if (mn.name.equals(name)) {
                                    int method = getMethod(mn);
                                    ldcInsnNodeNext.cst = decrypt((String) ldcInsnNode.cst, (String) ldcInsnNodeNext.cst, method);
                                    methodNode.instructions.remove(ldcInsnNodeNext.getNext());
                                    methodNode.instructions.remove(ldcInsnNode);
                                }
                            }
                        }
                    }
                }
            }
        }));
    }

    private String decrypt(String obj, String key, int method) {
        switch (method) {
            case 0: {
                return ll(obj, key);
            }
            case 1: {
                return lI(obj, key);
            }
            case 2: {
                return l(obj, key);
            }
            case 3: {
                return I(obj, key);
            }
        }
        return null;
    }

    private int getMethod(MethodNode methodNode) {
        if(methodNode.desc.equals(createDescription(DESC_STRING, DESC_STRING, DESC_STRING))) {
            if (hasInstructions(methodNode.instructions, DUP, LDC, INVOKESTATIC, ALOAD, GETSTATIC, INVOKEVIRTUAL, INVOKEVIRTUAL, LDC) && hasStrings(methodNode.instructions, "MD5", "Blowfish")) {
                return 0;
            }
            if (hasInstructions(methodNode.instructions, DUP, LDC, INVOKESTATIC, ALOAD, GETSTATIC, INVOKEVIRTUAL, INVOKEVIRTUAL, BIPUSH) && hasStrings(methodNode.instructions, "MD5")) {
                return 1;
            }
            if (hasInstructions(methodNode.instructions, DUP, LDC, INVOKESTATIC, ALOAD, LDC, INVOKEVIRTUAL, INVOKEVIRTUAL, LDC) && hasStrings(methodNode.instructions, "SHA-256", "AES")) {
                return 2;
            }
            if (hasInstructions(methodNode.instructions, DUP, INVOKESTATIC, ALOAD, GETSTATIC, INVOKEVIRTUAL, INVOKEVIRTUAL, GETSTATIC, INVOKESPECIAL)) {
                return 3;
            }
        }
        return -1;
    }

    private String l(String obj, String key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(MessageDigest.getInstance("SHA-256").digest(key.getBytes(StandardCharsets.UTF_8)), "AES");
            Cipher des = Cipher.getInstance("AES");
            des.init(2, keySpec);
            return new String(des.doFinal(Base64.getDecoder().decode(obj.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
        } catch (Exception var4) {
            return null;
        }
    }

    private String lI(String obj, String key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(Arrays.copyOf(MessageDigest.getInstance("MD5").digest(key.getBytes(StandardCharsets.UTF_8)), 8), "DES");
            Cipher des = Cipher.getInstance("DES");
            des.init(2, keySpec);
            return new String(des.doFinal(Base64.getDecoder().decode(obj.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
        } catch (Exception var4) {
            return null;
        }
    }

    private String I(String obj, String key) {
        obj = new String(Base64.getDecoder().decode(obj.getBytes(StandardCharsets.UTF_8)), StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();
        char[] keyChars = key.toCharArray();
        int i = 0;
        char[] var5 = obj.toCharArray();

        for (char c : var5) {
            sb.append((char) (c ^ keyChars[i % keyChars.length]));
            ++i;
        }

        return sb.toString();
    }

    private String ll(String obj, String key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(MessageDigest.getInstance("MD5").digest(key.getBytes(StandardCharsets.UTF_8)), "Blowfish");
            Cipher des = Cipher.getInstance("Blowfish");
            des.init(2, keySpec);
            return new String(des.doFinal(Base64.getDecoder().decode(obj.getBytes(StandardCharsets.UTF_8))), StandardCharsets.UTF_8);
        } catch (Exception var4) {
            return null;
        }
    }
}