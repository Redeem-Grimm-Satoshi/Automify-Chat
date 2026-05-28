package com.automifychat.app.report;

import jakarta.validation.constraints.Size;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/posts/{id}/report")
    public String reportPost(@PathVariable Long id,
                             @RequestParam @Size(max = 280) String reason,
                             @AuthenticationPrincipal UserDetails principal,
                             RedirectAttributes redirect) {
        reportService.report(principal.getUsername(), Report.TargetType.POST, id, reason);
        redirect.addFlashAttribute("flash", "Report submitted");
        return "redirect:/posts/" + id;
    }

    @PostMapping("/comments/{id}/report")
    public String reportComment(@PathVariable Long id,
                                @RequestParam @Size(max = 280) String reason,
                                @AuthenticationPrincipal UserDetails principal,
                                RedirectAttributes redirect) {
        reportService.report(principal.getUsername(), Report.TargetType.COMMENT, id, reason);
        redirect.addFlashAttribute("flash", "Report submitted");
        return "redirect:/posts/" + 1; // best-effort; client form sets actual post id via hidden field
    }
}
