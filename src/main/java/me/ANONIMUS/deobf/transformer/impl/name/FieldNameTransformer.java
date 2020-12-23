package me.ANONIMUS.deobf.transformer.impl.name;

import me.ANONIMUS.deobf.transformer.Transformer;
import me.ANONIMUS.deobf.util.BytecodeUtils;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.*;
import java.util.stream.Collectors;

public class FieldNameTransformer extends Transformer {
    @Override
    public void visit(Map<String, ClassNode> classMap) {
        Map<String, String> remap = new HashMap<>();

        List<FieldNode> fields = new ArrayList<>();
        for (ClassNode c : classMap.values()) {
            fields.addAll(c.fields);
        }
        Collections.shuffle(fields);
        int i = 0;
        for (FieldNode f : fields) {
            ClassNode c = getOwner(f, classMap);
            String name = "field_" + i;
            Stack<ClassNode> nodeStack = new Stack<>();
            nodeStack.add(c);
            while (nodeStack.size() > 0) {
                ClassNode node = nodeStack.pop();
                String key = node.name + "." + f.name;
                remap.put(key, name);
                nodeStack.addAll(classMap.values().stream().
                        filter(cn -> cn.superName.equals(node.name)).
                        collect(Collectors.toList()));
            }
            i++;
        }
        BytecodeUtils.applyMappings(classMap, remap);
    }

    private ClassNode getOwner(FieldNode f, Map<String, ClassNode> classMap) {
        for (ClassNode c : classMap.values())
            if (c.fields.contains(f))
                return c;
        return null;
    }
}