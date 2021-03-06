package me.ANONIMUS.deobf;

import me.ANONIMUS.deobf.transformer.Transformer;
import org.apache.commons.io.IOUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.analysis.AnalyzerException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

public class Deobfuscator {
    private static Deobfuscator instance;

    private final List<Transformer> transformers;
    private final Map<String, ClassNode> classes;
    private final Map<String, byte[]> files;

    public Deobfuscator() {
        instance = this;
        this.classes = new HashMap<>();
        this.files = new HashMap<>();
        this.transformers = new ArrayList();
    }

    public void run(String[] args) {
        if (args.length == 0) {
            System.err.println("[ERROR] Input jar must be specified");
            return;
        }

        String input = args[0];
        if (!input.endsWith(".jar")) {
            input += ".jar";
        }

        File in = new File(input);
        if (!in.exists()) {
            System.err.println("[ERROR] Input file not found");
            return;
        }

        String output = input;
        output = output.substring(0, output.length() - ".jar".length()) + "Deobf.jar";

        File out = new File(output);
        if (out.exists()) {
            if (!out.delete()) {
                System.err.println("[ERROR] Could not delete out file");
                return;
            }
        }

        loadJar(in);
        System.out.println("[INFO] Successful loaded " + in.getName() + " [" + classes.size() + " class] [" + files.size() + " files]");

        System.out.println();
        transformers.forEach(transformer -> {
            System.out.println(transformer.getClass().getSimpleName() + " running...");
            try {
                transformer.visit(classes);
            } catch (AnalyzerException e) {
                System.err.println("[ERROR] An error has occurred in " + transformer.getClass().getSimpleName());
                e.printStackTrace();
            }
        });

        System.out.println();
        System.out.println("[INFO] Finished, saving the file...");
        try {
            try (JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(out))) {
                saveJar(jarOutputStream);
                System.out.println("[INFO] Successful saved " + out.getName());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void loadJar(File file) {
        try (JarFile jarFile = new JarFile(file)) {
            final Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                final JarEntry jarEntry = entries.nextElement();
                try (InputStream inputStream = jarFile.getInputStream(jarEntry)) {
                    final byte[] bytes = IOUtils.toByteArray(inputStream);
                    if (!jarEntry.getName().endsWith(".class")) {
                        files.put(jarEntry.getName(), bytes);
                        continue;
                    }

                    try {
                        final ClassNode classNode = new ClassNode();
                        final ClassReader classReader = new ClassReader(bytes);
                        classReader.accept(classNode, ClassReader.EXPAND_FRAMES);
                        this.classes.put(classNode.name, classNode);
                    } catch (Exception e) {
                        System.err.println("[ERROR] There was an error loading " + jarEntry.getName());
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void saveJar(JarOutputStream jarOutputStream) throws IOException {
        for (ClassNode classNode : this.classes.values()) {
            final JarEntry jarEntry = new JarEntry(classNode.name + ".class");
            final ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);

            jarOutputStream.putNextEntry(jarEntry);
            classNode.accept(classWriter);
            jarOutputStream.write(classWriter.toByteArray());
            jarOutputStream.closeEntry();
        }

        for (Map.Entry<String, byte[]> entry : this.files.entrySet()) {
            jarOutputStream.putNextEntry(new JarEntry(entry.getKey()));
            jarOutputStream.write(entry.getValue());
            jarOutputStream.closeEntry();
        }
    }

    public Deobfuscator addTransformer(Transformer transformer) {
        if(!transformers.contains(transformer))
            transformers.add(transformer);
        return this;
    }

    public Map<String, byte[]> getFiles() { return files; }

    public Map<String, ClassNode> getClasses() { return classes; }

    public static Deobfuscator getInstance() { return instance; }
}