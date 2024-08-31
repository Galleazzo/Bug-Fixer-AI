package br.com.bugfixer.service;

import br.com.bugfixer.model.BugReport;
import br.com.bugfixer.repository.BugReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BugReportService {

    @Autowired
    private BugReportRepository repository;


    public BugReport saveBugReport(BugReport bugReport) {
        return repository.save(bugReport);
    }
}
