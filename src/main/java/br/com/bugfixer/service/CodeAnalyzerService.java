package br.com.bugfixer.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

@Service
public class CodeAnalyzerService {

    public String findBugLocation(String projectPath, String className, String methodName) {
        try {
            File file = new File(projectPath);
            URL url = file.toURI().toURL();
            URLClassLoader classLoader = new URLClassLoader(new URL[]{url});
            Class<?> clazz = Class.forName(className, true, classLoader);
            Method method = clazz.getDeclaredMethod(methodName);

            return "Bug localizado na classe " + className + ", método " + method.getName();
        } catch (Exception e) {
            return "Erro ao localizar o método: " + e.getMessage();
        }
    }
}
