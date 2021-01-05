package me.ANONIMUS.deobf;

import me.ANONIMUS.deobf.transformer.impl.AnnotationRemoverTransformer;
import me.ANONIMUS.deobf.transformer.impl.FixAccessTransformer;
import me.ANONIMUS.deobf.transformer.impl.LocalVariableRemoverTransformer;
import me.ANONIMUS.deobf.transformer.impl.WatermarkTransformer;
import me.ANONIMUS.deobf.transformer.impl.string.qProtectStringTransformer;

public class DeobfuscatorInitializer {
    public static void main(String[] args) {
        new Deobfuscator()
                .addTransformer(new qProtectStringTransformer())
                .addTransformer(new LocalVariableRemoverTransformer())
                .addTransformer(new FixAccessTransformer())
                .addTransformer(new AnnotationRemoverTransformer())
                .addTransformer(new WatermarkTransformer())
                .run(args);
    }
}