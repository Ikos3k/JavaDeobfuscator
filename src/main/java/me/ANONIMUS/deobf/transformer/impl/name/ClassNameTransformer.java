package me.ANONIMUS.deobf.transformer.impl.name;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.ClassNode;

import java.util.*;

public class ClassNameTransformer extends Transformer {
    @Override
    public void visit(Map<String, ClassNode> classMap) {
        Map<String, String> remap = new HashMap<>();

        List<String> keys = new ArrayList<>(classMap.keySet());
        Collections.shuffle(keys);
        int i = 0;
        for (String key : keys) {
            ClassNode cn = classMap.get(key);
            if(!isMainClass(cn)) {
                remap.put(cn.name, "class_" + i);
                i++;
            }
        }
        applyMappings(classMap, remap);
    }
}