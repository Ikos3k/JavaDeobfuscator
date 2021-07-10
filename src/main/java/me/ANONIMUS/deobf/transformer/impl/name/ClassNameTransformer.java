package me.ANONIMUS.deobf.transformer.impl.name;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.ClassNode;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClassNameTransformer extends Transformer {
    @Override
    public void visit(List<ClassNode> classMap) {
        Map<String, String> remap = new HashMap<>();

        List<String> keys = classMap.stream().map(c -> c.name).collect(Collectors.toList());
        Collections.shuffle(keys);
        int i = 0;
        for (String key : keys) {
            ClassNode cn = getClassNode(key, classMap);
            if (!isMainClass(cn)) {
                remap.put(cn.name, "class_" + i);
                i++;
            }
        }
        applyMappings(classMap, remap);
    }
}