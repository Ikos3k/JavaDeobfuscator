package me.ANONIMUS.deobf.util;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.ClassRemapper;
import org.objectweb.asm.commons.SimpleRemapper;
import org.objectweb.asm.tree.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class BytecodeUtils implements Opcodes {
    public boolean isMainMethod(MethodNode methodNode) {
        return methodNode.name.equals("main") && methodNode.desc.equals(createDescription(DESC_VOID, DESC_ARRAY_STRING)) && isPublic(methodNode.access) && isStatic(methodNode.access);
    }

    public boolean isInitializer(final MethodNode methodNode) {
        return methodNode.name.contains("<") || methodNode.name.contains(">");
    }

    public static boolean checkClassVerify(byte[] bytes) {
        return String.format("%X%X%X%X", bytes[0], bytes[1], bytes[2], bytes[3]).equals("CAFEBABE");
    }

    public boolean isMainClass(ClassNode classNode) {
        for (MethodNode method : classNode.methods)
            if (isMainMethod(method))
                return true;
        return false;
    }

    public boolean isIntInsn(AbstractInsnNode insn) {
        if (insn == null) {
            return false;
        }
        int opcode = insn.getOpcode();
        return ((opcode >= ICONST_M1 && opcode <= ICONST_5)
                || opcode == BIPUSH
                || opcode == SIPUSH
                || (insn instanceof LdcInsnNode
                && ((LdcInsnNode) insn).cst instanceof Integer));
    }

    public int getIntNumber(AbstractInsnNode insn) {
        final int opcode = insn.getOpcode();
        if (opcode >= ICONST_M1 && opcode <= ICONST_5)
            return opcode - 3;
        else if (insn instanceof IntInsnNode && insn.getOpcode() != NEWARRAY)
            return ((IntInsnNode) insn).operand;
        else if (insn instanceof LdcInsnNode && ((LdcInsnNode) insn).cst instanceof Integer)
            return (Integer) ((LdcInsnNode) insn).cst;
        throw new IllegalStateException("Unexpected instruction");
    }

    public <T extends AbstractInsnNode> void forEach(InsnList instructions, Class<T> type, Consumer<T> consumer) {
        AbstractInsnNode[] array = instructions.toArray();
        for (AbstractInsnNode node : array) {
            if (node.getClass() == type) {
                consumer.accept((T) node);
            }
        }
    }

    public void forEach(InsnList instructions, Consumer<AbstractInsnNode> consumer) {
        forEach(instructions, AbstractInsnNode.class, consumer);
    }

    public void applyMappings(List<ClassNode> classMap, Map<String, String> remap) {
        SimpleRemapper remapper = new SimpleRemapper(remap);
        for (ClassNode node : new ArrayList<>(classMap)) {
            ClassNode copy = new ClassNode();
            ClassRemapper adapter = new ClassRemapper(copy, remapper);
            node.accept(adapter);
            classMap.remove(node);
            classMap.add(copy);
        }
    }


    public <T> T findFirst(Collection<T> collection, Predicate<T> predicate) {
        for (T t : collection)
            if (predicate.test(t))
                return t;
        return null;
    }

    public MethodNode getMethod(ClassNode node, String name, String desc) {
        return findFirst(node.methods, m -> m.name.equals(name) && m.desc.equals(desc));
    }

    public ClassNode getOwner(MethodNode m, List<ClassNode> classMap) {
        return findFirst(classMap, c -> c.methods.contains(m));
    }

    public ClassNode getOwner(FieldNode f, List<ClassNode> classMap) {
        return findFirst(classMap, c -> c.fields.contains(f));
    }

    public ClassNode getClassNode(String name, List<ClassNode> classMap) {
        return findFirst(classMap, c -> c.name.equals(name));
    }

    public final byte DESC_EMPTY = -1;
    public final byte DESC_VOID = 0;
    public final byte DESC_OBJECT = 1;
    public final byte DESC_STRING = 2;
    public final byte DESC_INTEGER = 3;
    public final byte DESC_CLASS = 4;
    public final byte DESC_BOOLEAN_TYPE = 5;
    public final byte DESC_INT = 6;
    public final byte DESC_LONG = 7;
    public final byte DESC_FLOAT = 8;
    public final byte DESC_DOUBLE = 9;
    public final byte DESC_SHORT = 10;
    public final byte DESC_BOOLEAN = 11;
    public final byte DESC_CHAR = 12;
    public final byte DESC_BYTE = 13;

    public final byte DESC_ARRAY_OBJECT = 101;
    public final byte DESC_ARRAY_STRING = 102;
    public final byte DESC_ARRAY_INTEGER = 103;
    public final byte DESC_ARRAY_CLASS = 104;
    public final byte DESC_ARRAY_BOOLEAN_TYPE = 105;
    public final byte DESC_ARRAY_INT = 106;
    public final byte DESC_ARRAY_LONG = 107;
    public final byte DESC_ARRAY_FLOAT = 108;
    public final byte DESC_ARRAY_DOUBLE = 109;
    public final byte DESC_ARRAY_SHORT = 110;
    public final byte DESC_ARRAY_BOOLEAN = 111;
    public final byte DESC_ARRAY_CHAR = 112;
    public final byte DESC_ARRAY_BYTE = 113;

    public String createDescription(byte returnType, byte... arguments) {
        StringBuilder desc = new StringBuilder();
        if (arguments.length > 0)
            desc.append("(");
        IntStream.range(0, arguments.length).forEach(i -> desc.append(getDescription(arguments[i])));
        if (arguments.length > 0) {
            desc.append(")");
        }
        return desc.append(getDescription(returnType)).toString();
    }

    private String getDescription(byte type) {
        if (type > 100) {
            return "[" + getDescription((byte) (type - 100));
        }
        switch (type) {
            case -1:
                return "";
            case 0:
                return "V";
            case 2:
                return "Ljava/lang/String;";
            case 3:
                return "Ljava/lang/Integer;";
            case 4:
                return "Ljava/lang/Class;";
            case 5:
                return "Ljava/lang/Boolean;";
            case 6:
                return "I";
            case 7:
                return "J";
            case 8:
                return "F";
            case 9:
                return "D";
            case 10:
                return "S";
            case 11:
                return "Z";
            case 12:
                return "C";
            case 13:
                return "B";
            default:
                return "Ljava/lang/Object;";
        }
    }

    public boolean hasInstructions(InsnList insnList, int... opcodes) {
        AbstractInsnNode[] abstractInsnNodes = insnList.toArray();
        if (opcodes.length > abstractInsnNodes.length) {
            return false;
        }

        int i = 0;
        for (int opcode : opcodes) {
            for (AbstractInsnNode ab : abstractInsnNodes) {
                if (ab.getOpcode() == opcode) {
                    i++;
                }
            }
        }
        return i >= opcodes.length;
    }

    public boolean hasStrings(InsnList insnList, String... strings) {
        AbstractInsnNode[] abstractInsnNodes = insnList.toArray();
        if (strings.length > abstractInsnNodes.length) {
            return false;
        }

        int i = 0;
        for (String s : strings) {
            for (AbstractInsnNode ab : insnList.toArray()) {
                if (ab.getType() == AbstractInsnNode.LDC_INSN) {
                    LdcInsnNode ldcInsnNode = (LdcInsnNode) ab;
                    if (ldcInsnNode.cst.equals(s)) {
                        i++;
                    }
                }
            }
        }
        return i >= strings.length;
    }

    public static byte[] toByteArray(ClassNode classNode) {
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        try {
            classNode.accept(writer);
            return writer.toByteArray();
        } catch (Throwable t) {
            writer = new ClassWriter(0);
            classNode.accept(writer);
            return writer.toByteArray();
        }
    }

    public int computeConstantPoolSize(ClassNode classNode) {
        return new ClassReader(toByteArray(classNode)).getItemCount();
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

    public int setAccess(int... add) {
        int access = 0;
        for (int a : add) {
            access |= a;
        }
        return access;
    }

    public boolean hasAccess(int access, int... check) {
        for (int a : check) {
            if ((access & a) == 0) {
                return false;
            }
        }
        return true;
    }

    public boolean isPublic(int access) {
        return (access & ACC_PUBLIC) != 0;
    }

    public boolean isProtected(int access) {
        return (access & ACC_PROTECTED) != 0;
    }

    public boolean isPrivate(int access) {
        return (access & ACC_PRIVATE) != 0;
    }

    public boolean isStatic(int access) {
        return (access & ACC_STATIC) != 0;
    }

    public boolean isStaticPhase(int access) {
        return (access & ACC_STATIC_PHASE) != 0;
    }

    public boolean isStrict(int access) {
        return (access & ACC_STRICT) != 0;
    }

    public boolean isSuper(int access) {
        return (access & ACC_SUPER) != 0;
    }

    public boolean isNative(int access) {
        return (access & ACC_NATIVE) != 0;
    }

    public boolean isMandated(int access) {
        return (access & ACC_MANDATED) != 0;
    }

    public boolean isModule(int access) {
        return (access & ACC_MODULE) != 0;
    }

    public boolean isOpen(int access) {
        return (access & ACC_OPEN) != 0;
    }

    public boolean isAbstract(int access) {
        return (access & ACC_ABSTRACT) != 0;
    }

    public boolean isFinal(int access) {
        return (access & ACC_FINAL) != 0;
    }

    public boolean isSynthetic(int access) {
        return (access & ACC_SYNTHETIC) != 0;
    }

    public boolean isTransient(int access) {
        return (access & ACC_TRANSIENT) != 0;
    }

    public boolean isTransitive(int access) {
        return (access & ACC_TRANSITIVE) != 0;
    }

    public boolean isVolatile(int access) {
        return (access & ACC_VOLATILE) != 0;
    }

    public boolean isVarargs(int access) {
        return (access & ACC_VARARGS) != 0;
    }

    public boolean isBridge(int access) {
        return (access & ACC_BRIDGE) != 0;
    }

    public boolean isSynchronized(int access) {
        return (access & ACC_SYNCHRONIZED) != 0;
    }

    public boolean isInterface(int access) {
        return (access & ACC_INTERFACE) != 0;
    }

    public boolean isEnum(int access) {
        return (access & ACC_ENUM) != 0;
    }

    public boolean isAnnotation(int access) {
        return (access & ACC_ANNOTATION) != 0;
    }

    public boolean isDeprecated(int access) {
        return (access & ACC_DEPRECATED) != 0;
    }
}