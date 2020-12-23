package me.ANONIMUS.deobf.transformer.impl.name;

import me.ANONIMUS.deobf.transformer.Transformer;
import me.ANONIMUS.deobf.util.BytecodeUtils;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.*;
import java.util.function.Predicate;

public class MethodNameTransformer extends Transformer {
    @Override
    public void visit(Map<String, ClassNode> classMap) {
        Map<String, String> remap = new HashMap<>();

        List<MethodNode> methods = new LinkedList<>();
        classMap.values().forEach(c -> methods.addAll(c.methods));

        Collections.shuffle(methods);
        int i = 0;
        methods:
        for (MethodNode m : methods) {
            ClassNode owner = getOwner(classMap, m);

            if (m.name.indexOf('<') != -1 || BytecodeUtils.isNative(m.access) || BytecodeUtils.isMainMethod(m)) {
                continue;
            }
            Stack<ClassNode> nodeStack = new Stack<>();
            nodeStack.add(owner);
            while (nodeStack.size() > 0) {
                ClassNode node = nodeStack.pop();
                if (node != owner && getMethod(node, m.name, m.desc) != null)
                    continue methods;
                ClassNode parent = classMap.get(node.superName);
                if (parent != null)
                    nodeStack.push(parent);
                Set<ClassNode> interfaces = new HashSet<>();
                String[] interfacesNames = node.interfaces.toArray(new String[0]);
                for (String str : interfacesNames) {
                    ClassNode cn = classMap.get(str);
                    if (cn != null) {
                        interfaces.add(cn);
                    }
                }
                nodeStack.addAll(interfaces);
            }
            String name = "method_" + i;
            nodeStack.add(owner);
            while (nodeStack.size() > 0) {
                ClassNode node = nodeStack.pop();
                String key = node.name + '.' + m.name + m.desc;
                remap.put(key, name);
                classMap.values().forEach(c -> {
                    if (c.superName.equals(node.name) || c.interfaces.contains(node.name))
                        nodeStack.push(c);
                });
            }
            i++;
        }
        BytecodeUtils.applyMappings(classMap, remap);
    }

    private MethodNode getMethod(ClassNode node, String name, String desc) {
        return findFirst(node.methods, m -> m.name.equals(name) && m.desc.equals(desc));
    }

    private ClassNode getOwner(Map<String, ClassNode> classMap, MethodNode m) {
        return findFirst(classMap.values(), c -> c.methods.contains(m));
    }

    private <T> T findFirst(Collection<T> collection, Predicate<T> predicate) {
        for (T t : collection)
            if (predicate.test(t))
                return t;
        return null;
    }
}