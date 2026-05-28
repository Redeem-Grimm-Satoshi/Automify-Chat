package com.automifychat.app.notification;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications")
    public String list(@AuthenticationPrincipal UserDetails principal, Model model) {
        model.addAttribute("notifications", notificationService.recentFor(principal.getUsername()));
        return "notifications";
    }

    @PostMapping("/notifications/{id}/read")
    public String markRead(@PathVariable Long id,
                           @AuthenticationPrincipal UserDetails principal,
                           RedirectAttributes redirect) {
        notificationService.markRead(id, principal.getUsername());
        return "redirect:/notifications";
    }

    @PostMapping("/notifications/read-all")
    public String markAllRead(@AuthenticationPrincipal UserDetails principal,
                              RedirectAttributes redirect) {
        notificationService.markAllRead(principal.getUsername());
        redirect.addFlashAttribute("flash", "All notifications marked read");
        return "redirect:/notifications";
    }
}
