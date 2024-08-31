package br.com.bugfixer.service;

import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Service
public class BugFixerService {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${apenAI.token}")
    private String apiKey;

    public String attemptFix(String projectPath, String className, String methodName, String expectedBehavior, String path) {
        try {
            String methodCode = extractMethodCode(projectPath, className, methodName, path);

            String prompt = "Here is a Java method that has a bug:\n\n" + methodCode + "\n\nThe bug is described as follows:\n"
                    + expectedBehavior + "\n\nPlease provide the corrected version of the method.";

            String fixedMethod = callOpenAiApi(prompt);

            replaceMethodInClassFile(projectPath, className, methodName, fixedMethod, path);

            return "Bug fix applied: \n" + fixedMethod;

        } catch (Exception e) {
            return "Erro ao tentar corrigir o bug: " + e.getMessage();
        }
    }

    public String callOpenAiApi(String prompt) {
        String url = "https://api.openai.com/v1/chat/completions";

        JSONObject requestBody = new JSONObject();
        requestBody.put("model", "gpt-3.5-turbo");

        JSONArray messages = new JSONArray();

        JSONObject userMessage = new JSONObject();
        userMessage.put("role", "user");
        userMessage.put("content", prompt);
        messages.put(userMessage);

        requestBody.put("messages", messages);
        requestBody.put("max_tokens", 50);
        requestBody.put("temperature", 0.7);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + apiKey);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);

        JSONObject jsonResponse = new JSONObject(response.getBody());
        return jsonResponse.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content").trim();
    }

    public String extractMethodCode(String projectPath, String className, String methodName, String path) throws Exception {
        String classFilePath = projectPath + path + className.replace(".", "/") + ".java";

        List<String> lines = Files.readAllLines(Paths.get(classFilePath), StandardCharsets.UTF_8);

        int methodStartIndex = findMethodStartIndex(lines, methodName);
        int methodEndIndex = findMethodEndIndex(lines, methodStartIndex);

        if (methodStartIndex == -1 || methodEndIndex == -1) {
            throw new Exception("Método " + methodName + " não encontrado na classe " + className);
        }

        List<String> methodLines = lines.subList(methodStartIndex, methodEndIndex + 1);

        return String.join("\n", methodLines);
    }

    public void replaceMethodInClassFile(String projectPath, String className, String methodName, String fixedMethod, String path) throws Exception {
        String classFilePath = projectPath + path + className.replace(".", "/") + ".java";

        List<String> lines = Files.readAllLines(Paths.get(classFilePath), StandardCharsets.UTF_8);

        int methodStartIndex = findMethodStartIndex(lines, methodName);
        int methodEndIndex = findMethodEndIndex(lines, methodStartIndex);

        if (methodStartIndex == -1 || methodEndIndex == -1) {
            throw new Exception("Método " + methodName + " não encontrado na classe " + className);
        }

        List<String> newLines = replaceMethodCode(lines, methodStartIndex, methodEndIndex, fixedMethod);

        Files.write(Paths.get(classFilePath), newLines, StandardCharsets.UTF_8, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private int findMethodStartIndex(List<String> lines, String methodName) {
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).contains(" " + methodName + "(")) {
                return i;
            }
        }
        return -1;
    }

    private int findMethodEndIndex(List<String> lines, int startIndex) {
        for (int i = startIndex; i < lines.size(); i++) {
            if (lines.get(i).contains("}")) {
                return i;
            }
        }
        return -1;
    }

    private List<String> replaceMethodCode(List<String> lines, int startIndex, int endIndex, String fixedMethod) {
        List<String> newLines = lines.subList(0, startIndex);
        newLines.add(fixedMethod);
        newLines.addAll(lines.subList(endIndex + 1, lines.size())); // Adicionar o resto da classe

        return newLines;
    }
}
