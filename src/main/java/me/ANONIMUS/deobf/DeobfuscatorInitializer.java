package me.ANONIMUS.deobf;

import me.ANONIMUS.deobf.transformer.impl.AnnotationRemoveTransformer;
import me.ANONIMUS.deobf.transformer.impl.FixAccessTransformer;
import me.ANONIMUS.deobf.transformer.impl.LocalVariableRemoverTransformer;
import me.ANONIMUS.deobf.transformer.impl.name.ClassNameTransformer;
import me.ANONIMUS.deobf.transformer.impl.name.FieldNameTransformer;
import me.ANONIMUS.deobf.transformer.impl.string.SuperBlaubeere27StringTransformer;

public class DeobfuscatorInitializer {
    public static void main(String[] args) {
        new Deobfuscator()
                .addTransformer(new LocalVariableRemoverTransformer())
                .addTransformer(new AnnotationRemoveTransformer())
                .addTransformer(new FieldNameTransformer())
                .addTransformer(new ClassNameTransformer())
                .addTransformer(new FixAccessTransformer())
                .addTransformer(new SuperBlaubeere27StringTransformer())
                .run(args);
    }
}