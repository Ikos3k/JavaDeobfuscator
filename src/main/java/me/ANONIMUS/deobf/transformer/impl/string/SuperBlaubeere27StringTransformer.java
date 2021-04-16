package me.ANONIMUS.deobf.transformer.impl.string;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.*;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.*;

public class SuperBlaubeere27StringTransformer extends Transformer {
    @Override
    public void visit(Map<String, ClassNode> classMap) {
        classMap.values().forEach(classNode -> {
            List<MethodNode> methods = new ArrayList<>();
            classNode.methods.forEach(methodNode -> {
                AbstractInsnNode[] abstractInsnNodes = methodNode.instructions.toArray();
                for (AbstractInsnNode abstractInsnNode : abstractInsnNodes) {
                    if (abstractInsnNode.getType() == AbstractInsnNode.LDC_INSN) {
                        LdcInsnNode ldcInsnNode = (LdcInsnNode) abstractInsnNode;
                        if (ldcInsnNode.getNext() != null && ldcInsnNode.getNext() instanceof LdcInsnNode) {
                            LdcInsnNode ldcInsnNodeNext = (LdcInsnNode) ldcInsnNode.getNext();
                            if (ldcInsnNodeNext.getNext().getOpcode() == INVOKESTATIC && ldcInsnNodeNext.getNext().getType() == AbstractInsnNode.METHOD_INSN) {
                                String name = ((MethodInsnNode) ldcInsnNodeNext.getNext()).name;
                                for (MethodNode mn : classNode.methods) {
                                    if (mn.name.equals(name) && mn.desc.equals(createDescription(DESC_STRING, DESC_STRING, DESC_STRING))) {
                                        ldcInsnNodeNext.cst = decrypt(mn, (String) ldcInsnNode.cst, (String) ldcInsnNodeNext.cst);
                                        methodNode.instructions.remove(ldcInsnNodeNext.getNext());
                                        methodNode.instructions.remove(ldcInsnNode);
                                        if(!methods.contains(mn))
                                            methods.add(mn);
                                    }
                                }
                            }
                        }
                    }
                }
            });
            classNode.methods.removeAll(methods);
        });
    }

    private String decrypt(MethodNode methodNode, String obj, String key) {
        if (hasInstructions(methodNode.instructions, DUP, LDC, INVOKESTATIC, ALOAD, GETSTATIC, INVOKEVIRTUAL, INVOKEVIRTUAL, LDC) && hasStrings(methodNode.instructions, "MD5", "Blowfish")) {
            return ll(obj, key);
        }
        if (hasInstructions(methodNode.instructions, DUP, LDC, INVOKESTATIC, ALOAD, GETSTATIC, INVOKEVIRTUAL, INVOKEVIRTUAL, BIPUSH) && hasStrings(methodNode.instructions, "MD5")) {
            return lI(obj, key);
        }
        if (hasInstructions(methodNode.instructions, DUP, LDC, INVOKESTATIC, ALOAD, LDC, INVOKEVIRTUAL, INVOKEVIRTUAL, LDC) && hasStrings(methodNode.instructions, "SHA-256", "AES")) {
            return l(obj, key);
        }
        if (hasInstructions(methodNode.instructions, DUP, INVOKESTATIC, ALOAD, GETSTATIC, INVOKEVIRTUAL, INVOKEVIRTUAL, GETSTATIC, INVOKESPECIAL)) {
            return I(obj, key);
        }
        return null;
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