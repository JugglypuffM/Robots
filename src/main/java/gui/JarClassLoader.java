package gui;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class loader for jar files
 */
public class JarClassLoader extends ClassLoader {
    /**
     * Get bytes of jar entry
     * @param jar jar-archive
     * @param entry entry of this archive
     * @return bytes of entry
     * @throws IOException if the file reading failed
     */
    private byte[] getBytes(JarFile jar, JarEntry entry) throws IOException {
        byte[] buffer = new byte[2048];
        try (InputStream inputStream = jar.getInputStream(entry);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            int read;
            while ((read = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, read);
            }
            return byteArrayOutputStream.toByteArray();
        }
    }

    /**
     * Load class from given file
     * @param file jar file
     * @param name name of class that should be loaded
     * @return some class
     * @throws IOException if the file is not jar or {@link JarClassLoader#getBytes} fails
     */
    public Class<?> loadClass(File file, String name) throws IOException {
        try (JarFile jar = new JarFile(file)) {
            Enumeration<JarEntry> e = jar.entries();
            while (e.hasMoreElements()) {
                JarEntry entry = e.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace(".class", "").replace("/", ".");
                    if (className.toLowerCase().contains(name)) {
                        byte[] bytes = getBytes(jar, entry);
                        return defineClass(className, bytes, 0, bytes.length);
                    }
                }

            }
        }
        return null;
    }
}
