package me.ANONIMUS.deobf;

public class DeobfuscatorInitializer {
    public static void main(String[] args) {
        new Deobfuscator()
//                .addTransformer(new LocalVariableRemoverTransformer())
//                .addTransformer(new SignatureRemoverTransformer())
//                .addTransformer(new FieldNameTransformer())
                .run(args);
    }
}