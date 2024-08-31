package br.com.bugfixer.service;

import br.com.bugfixer.model.BugReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BugContextualizerService {

    @Autowired
    private TestRunnerService testRunnerService;

    @Autowired
    private CodeAnalyzerService codeAnalyzerService;

    @Autowired
    private BugTypeDetectorService bugTypeDetectorService;

    @Autowired
    private BugFixerService bugFixerService;


    public String analyzeAndFixBug(BugReport bugReport, String projectPath, String path) {
        // Detectando o tipo de bug
       /* String bugType = bugTypeDetectorService.detectBugType(projectPath, bugReport.getClassName());

        // Se for erro de compilação, tentamos corrigir
        if (bugType.startsWith("Compilation Error")) {
            return "Erro de compilação detectado: " + bugType;
        }*/

        // Se for bug lógico, tentamos a correção
        return this.bugFixerService.attemptFix(projectPath, bugReport.getClassName(), bugReport.getMethodName(), bugReport.getExpectedBehavior(), path);
    }
}
