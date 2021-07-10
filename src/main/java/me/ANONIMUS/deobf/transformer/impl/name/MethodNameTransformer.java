package me.ANONIMUS.deobf.transformer.impl.name;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import java.util.*;

public class MethodNameTransformer extends Transformer {
    @Override
    public void visit(List<ClassNode> classMap) {
        Map<String, String> remap = new HashMap<>();

        List<MethodNode> methods = new LinkedList<>();
        classMap.forEach(c -> methods.addAll(c.methods));

        Collections.shuffle(methods);
        int i = 0;
        methods:
        for (MethodNode m : methods) {
            ClassNode owner = getOwner(m, classMap);

            if (isInitializer(m) || isNative(m.access) || isMainMethod(m)) {
                continue;
            }
            Stack<ClassNode> nodeStack = new Stack<>();
            nodeStack.add(owner);
            while (nodeStack.size() > 0) {
                ClassNode node = nodeStack.pop();
                if (node != owner && getMethod(node, m.name, m.desc) != null)
                    continue methods;
                ClassNode parent = getClassNode(node.superName, classMap);
                if (parent != null)
                    nodeStack.push(parent);
                Set<ClassNode> interfaces = new HashSet<>();
                for (String str : node.interfaces.toArray(new String[0])) {
                    ClassNode cn = getClassNode(str, classMap);
                    if (cn != null) {
                        interfaces.add(cn);
                    }
                }
                nodeStack.addAll(interfaces);
            }
            nodeStack.add(owner);
            while (nodeStack.size() > 0) {
                ClassNode node = nodeStack.pop();
                remap.put(node.name + '.' + m.name + m.desc, "method_" + i);
                classMap.stream().filter(c -> c.superName.equals(node.name) || c.interfaces.contains(node.name)).forEach(nodeStack::push);
            }
            i++;
        }
        applyMappings(classMap, remap);
    }
}