package br.com.bugfixer.repository;

import br.com.bugfixer.model.BugReport;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BugReportRepository extends JpaRepository<BugReport, Long> {
}
