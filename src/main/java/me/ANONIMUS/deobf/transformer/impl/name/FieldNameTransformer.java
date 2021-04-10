package me.ANONIMUS.deobf.transformer.impl.name;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldNode;

import java.util.*;
import java.util.stream.Collectors;

public class FieldNameTransformer extends Transformer {
    @Override
    public void visit(Map<String, ClassNode> classMap) {
        Map<String, String> remap = new HashMap<>();

        List<FieldNode> fields = new ArrayList<>();
        classMap.values().forEach(c -> fields.addAll(c.fields));
        Collections.shuffle(fields);
        int i = 0;
        for (FieldNode f : fields) {
            ClassNode c = getOwner(f, classMap);
            Stack<ClassNode> nodeStack = new Stack<>();
            nodeStack.add(c);
            while (nodeStack.size() > 0) {
                ClassNode node = nodeStack.pop();
                remap.put(node.name + "." + f.name, "field_" + i);
                nodeStack.addAll(classMap.values().stream().
                        filter(cn -> cn.superName.equals(node.name)).
                        collect(Collectors.toList()));
            }
            i++;
        }
        applyMappings(classMap, remap);
    }
}