package br.com.bugfixer.service;

import br.com.bugfixer.model.BugReport;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class TestRunnerService {

    public boolean validateBug(BugReport bugReport) {
        String className = bugReport.getClassName();
        String methodName = bugReport.getMethodName();

        boolean testPasses = runTest(className, methodName);

        return !testPasses;
    }

    private boolean runTest(String className, String methodName) {

        return false;
    }
}
