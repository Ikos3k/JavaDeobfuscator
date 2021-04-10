package me.ANONIMUS.deobf.transformer.impl.optimization;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.JumpInsnNode;

import java.util.Arrays;
import java.util.Map;

public class GotoInlinerTransformer extends Transformer {
    @Override
    public void visit(Map<String, ClassNode> classMap) {
        classMap.values().forEach(classNode ->
            classNode.methods.forEach(methodNode ->
                Arrays.stream(methodNode.instructions.toArray()).filter(abstractInsnNode -> abstractInsnNode.getOpcode() == GOTO).forEach(abstractInsnNode -> {
                    final JumpInsnNode gotoJump = (JumpInsnNode) abstractInsnNode;
                    final AbstractInsnNode insnAfterTarget = gotoJump.label.getNext();
                    if (insnAfterTarget == null)
                        return;

                    if (insnAfterTarget.getOpcode() != GOTO)
                        return;

                    gotoJump.label = ((JumpInsnNode) insnAfterTarget).label;
                })
            )
        );
    }
}