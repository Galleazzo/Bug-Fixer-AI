package br.com.bugfixer.controller;

import br.com.bugfixer.model.BugReport;
import br.com.bugfixer.service.BugContextualizerService;
import br.com.bugfixer.service.BugReportService;
import br.com.bugfixer.service.GitCloneService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/bugs")
public class BugReportController {

    @Autowired
    private BugReportService reportService;

    @Autowired
    private BugContextualizerService contextualizerService;

    @Autowired
    private GitCloneService gitCloneService;


    @PostMapping
    public ResponseEntity<String> reportBug(@RequestBody BugReport bugReport, @RequestParam String repoUrl, @RequestParam String path) {
        BugReport savedBug = this.reportService.saveBugReport(bugReport);
        String resultMessage = this.contextualizerService.analyzeAndFixBug(savedBug, repoUrl, path);

        return new ResponseEntity<>(resultMessage, HttpStatus.CREATED);

    }
}
