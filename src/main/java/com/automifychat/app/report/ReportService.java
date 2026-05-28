package com.automifychat.app.report;

import com.automifychat.app.auth.UserAccount;
import com.automifychat.app.auth.UserAccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReportService {

    private final ReportRepository reports;
    private final UserAccountRepository users;

    public ReportService(ReportRepository reports, UserAccountRepository users) {
        this.reports = reports;
        this.users = users;
    }

    @Transactional
    public void report(String reporterUsername, Report.TargetType type, Long targetId, String reason) {
        UserAccount reporter = users.findByUsername(reporterUsername)
                .orElseThrow(() -> new IllegalStateException("No user: " + reporterUsername));
        if (reports.existsByReporterIdAndTargetTypeAndTargetId(reporter.getId(), type, targetId)) {
            return; // idempotent
        }
        reports.save(new Report(reporter, type, targetId, reason == null ? "" : reason));
    }
}
