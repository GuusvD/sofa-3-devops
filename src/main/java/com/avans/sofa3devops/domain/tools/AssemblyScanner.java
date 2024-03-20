package com.avans.sofa3devops.domain.tools;

import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.List;

public class AssemblyScanner {

    public static List<Class<?>> getSubclassesOf(Class<?> baseType, String packageName) {
        List<Class<?>> subclasses = new ArrayList<>();
        for (Class<?> clazz : getAllClasses(packageName)) {
            if (baseType.isAssignableFrom(clazz) && !clazz.isInterface() && !clazz.isEnum()) {
                subclasses.add(clazz);
            }
        }
        return subclasses;
    }

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
