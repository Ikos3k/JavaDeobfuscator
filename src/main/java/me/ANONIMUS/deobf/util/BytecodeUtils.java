package me.ANONIMUS.deobf.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.Map;
import java.util.function.Consumer;

import static org.objectweb.asm.Opcodes.*;

public class BytecodeUtils {
    public static boolean isMainMethod(MethodNode methodNode) { return methodNode.name.equals("main") && methodNode.desc.equals("([Ljava/lang/String;)V") && isPublic(methodNode.access) && isStatic(methodNode.access); }

    public static boolean matchMethodNode(MethodInsnNode methodInsnNode, String s) { return s.equals(methodInsnNode.owner + "." + methodInsnNode.name + ":" + methodInsnNode.desc); }

    public static AbstractInsnNode getNumberInsn(int number) {
        if (number >= -1 && number <= 5) {
            return new InsnNode(number + 3);
        } else if (number >= -128 && number <= 127) {
            return new IntInsnNode(Opcodes.BIPUSH, number);
        } else if (number >= -32768 && number <= 32767) {
            return new IntInsnNode(Opcodes.SIPUSH, number);
        } else {
            return new LdcInsnNode(number);
        }
    }

    public static <T extends AbstractInsnNode> void forEach(InsnList instructions, Class<T> type, Consumer<T> consumer) {
        AbstractInsnNode[] array = instructions.toArray();
        for (AbstractInsnNode node : array) {
            if (node.getClass() == type) {
                consumer.accept((T) node);
            }
        }
    }

    public static void forEach(InsnList instructions, Consumer<AbstractInsnNode> consumer) {
        forEach(instructions, AbstractInsnNode.class, consumer);
    }

    public static void applyMappings(Map<String, ClassNode> classMap, Map<String, String> remap) {
        for (Map.Entry<String, String> entry : remap.entrySet()) {
            String k = entry.getKey();
            String v = entry.getValue();
            if (k.equals(v))
                continue;
            int n = k.indexOf('.');
            if (n != -1 && v.length() >= n && v.substring(n).equals(k)) {
                continue;
            }
        }
        SimpleRemapper remapper = new SimpleRemapper(remap);
        for (ClassNode node : new ArrayList<>(classMap.values())) {
            ClassNode copy = new ClassNode();
            ClassRemapper adapter = new ClassRemapper(copy, remapper);
            node.accept(adapter);
            classMap.put(node.name, copy);
        }
    }

    public static byte[] toByteArray(ClassNode classNode) {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        try {
            classNode.accept(writer);
            return writer.toByteArray();
        } catch (Throwable t) {
            writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            classNode.accept(writer);
            return writer.toByteArray();
        }
    }

    public static int computeConstantPoolSize(ClassNode classNode) { return new ClassReader(toByteArray(classNode)).getItemCount(); }

    public static boolean isPublic(int access) { return (access & ACC_PUBLIC) != 0; }

    public static boolean isProtected(int access) { return (access & ACC_PROTECTED) != 0; }

    public static boolean isPrivate(int access) { return (access & ACC_PRIVATE) != 0; }

    public static boolean isStatic(int access) { return (access & ACC_STATIC) != 0; }

    public static boolean isStaticPhase(int access) { return (access & ACC_STATIC_PHASE) != 0; }

    public static boolean isStrict(int access) { return (access & ACC_STRICT) != 0; }

    public static boolean isSuper(int access) { return (access & ACC_SUPER) != 0; }

    public static boolean isNative(int access) {
        return (access & ACC_NATIVE) != 0;
    }

    public static boolean isMandated(int access) {
        return (access & ACC_MANDATED) != 0;
    }

    public static boolean isModule(int access) {
        return (access & ACC_MODULE) != 0;
    }

    public static boolean isOpen(int access) {
        return (access & ACC_OPEN) != 0;
    }

    public static boolean isAbstract(int access) {
        return (access & ACC_ABSTRACT) != 0;
    }

    public static boolean isFinal(int access) {
        return (access & ACC_FINAL) != 0;
    }

    public static boolean isSynthetic(int access) { return (access & ACC_SYNTHETIC) != 0; }

    public static boolean isTransient(int access) {
        return (access & ACC_TRANSIENT) != 0;
    }

    public static boolean isTransitive(int access) {
        return (access & ACC_TRANSITIVE) != 0;
    }

    public static boolean isVolatile(int access) {
        return (access & ACC_VOLATILE) != 0;
    }

    public static boolean isVarargs(int access) {
        return (access & ACC_VARARGS) != 0;
    }

    public static boolean isBridge(int access) {
        return (access & ACC_BRIDGE) != 0;
    }

    public static boolean isSynchronized(int access) {
        return (access & ACC_SYNCHRONIZED) != 0;
    }

    public static boolean isInterface(int access) {
        return (access & ACC_INTERFACE) != 0;
    }

    public static boolean isEnum(int access) {
        return (access & ACC_ENUM) != 0;
    }

    public static boolean isAnnotation(int access) {
        return (access & ACC_ANNOTATION) != 0;
    }

    public static boolean isDeprecated(int access) {
        return (access & ACC_DEPRECATED) != 0;
    }
}