package me.ANONIMUS.deobf;

import me.ANONIMUS.deobf.transformer.impl.string.SuperBlaubeere27StringTransformer;

public class DeobfuscatorInitializer {
    public static void main(String[] args) {
        new Deobfuscator()
                .addTransformer(new SuperBlaubeere27StringTransformer())
                .run(args);
    }
}