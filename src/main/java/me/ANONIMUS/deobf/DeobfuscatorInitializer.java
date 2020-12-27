package me.ANONIMUS.deobf;

import me.ANONIMUS.deobf.transformer.impl.AnnotationRemoveTransformer;
import me.ANONIMUS.deobf.transformer.impl.FixAccessTransformer;
import me.ANONIMUS.deobf.transformer.impl.LocalVariableRemoverTransformer;
import me.ANONIMUS.deobf.transformer.impl.WatermarkTransformer;

public class DeobfuscatorInitializer {
    public static void main(String[] args) {
        new Deobfuscator()
                .addTransformer(new LocalVariableRemoverTransformer())
                .addTransformer(new AnnotationRemoveTransformer())
                .addTransformer(new FixAccessTransformer())
                .addTransformer(new WatermarkTransformer())
                .run(args);
    }
}