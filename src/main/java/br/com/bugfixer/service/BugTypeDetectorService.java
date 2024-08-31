package br.com.bugfixer.service;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@Service
public class BugTypeDetectorService {

    public String detectBugType(String projectPath, String className) {
        try {
            ProcessBuilder builder = new ProcessBuilder("javac", "-d", "out", projectPath + "/src/main/java/br/com/backEnd/pacotinho/service/" + className.replace(".", "/") + ".java");
            builder.directory(new File(projectPath));
            Process process = builder.start();

            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            StringBuilder errorOutput = new StringBuilder();
            String line;
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }

            if (errorOutput.length() > 0) {
                return "Compilation Error: " + errorOutput.toString();
            }

            return "Logical Bug";

        } catch (Exception e) {
            return "Erro ao detectar o tipo de bug: " + e.getMessage();
        }
    }
}
