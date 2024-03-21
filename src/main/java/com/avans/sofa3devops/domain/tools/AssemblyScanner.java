package com.avans.sofa3devops.domain.tools;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AssemblyScanner {

    public static List<Class<?>> getAllClasses(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            assert classLoader != null;
            String path = packageName.replace('.', '/');
            java.net.URL resource = classLoader.getResource(path);
            if (resource == null) {
                throw new IllegalArgumentException("No resource found for " + path);
            }
            File directory = new File(resource.getFile());
            if (directory.exists()) {
                String[] files = directory.list();
                for (String file : files) {
                    if (file.endsWith(".class")) {
                        String className = packageName + '.' + file.substring(0, file.length() - 6);
                        classes.add(Class.forName(className));
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to get all classes", e);
        }
        return classes;
    }
}
