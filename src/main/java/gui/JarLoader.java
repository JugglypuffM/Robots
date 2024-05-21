package gui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarLoader extends ClassLoader {
    private byte[] getBytes(JarFile jar, JarEntry entry) throws IOException {
        byte[] buffer = new byte[1000000];
        InputStream inputStream = jar.getInputStream(entry);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int read;
        while ((read = inputStream.read(buffer)) != -1) {
            byteArrayOutputStream.write(buffer, 0, read);
        }
        return byteArrayOutputStream.toByteArray();
    }
    public void loadClass(File file, Class<?> model, Class<?> visualizer) throws IOException {
        try (JarFile jar = new JarFile(file)) {
            Enumeration<JarEntry> e = jar.entries();
            while (e.hasMoreElements()) {
                JarEntry entry = e.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace(".class", "").replace("/", ".");
                    if (className.toLowerCase().contains("game")) {
                        if (className.toLowerCase().contains("model")) {
                            byte[] bytes = getBytes(jar, entry);
                            model = defineClass(className, bytes, 0, bytes.length);
                        }
                        if (className.toLowerCase().contains("visualizer")) {
                            byte[] bytes = getBytes(jar, entry);
                            visualizer = defineClass(className, bytes, 0, bytes.length);
                        }
                    }
                }
            }

        }
    }
}
