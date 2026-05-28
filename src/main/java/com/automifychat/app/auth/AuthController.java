package com.automifychat.app.auth;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final SignupService signupService;

    public AuthController(SignupService signupService) {
        this.signupService = signupService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

    @GetMapping("/signup")
    public String signupPage(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new SignupForm("", "", ""));
        }
        return "auth/signup";
    }

    @PostMapping("/signup")
    public String submitSignup(@Valid @ModelAttribute("form") SignupForm form,
                               BindingResult binding,
                               Model model) {
        if (binding.hasErrors()) {
            return "auth/signup";
        }
        try {
            signupService.signup(form.username(), form.email(), form.password());
        } catch (SignupService.SignupException e) {
            model.addAttribute("signupError", e.getMessage());
            return "auth/signup";
        }
        return "redirect:/login?registered";
    }

    public record SignupForm(
            @NotBlank @Size(min = 3, max = 32) String username,
            @NotBlank @Email String email,
            @NotBlank @Size(min = 8, max = 100) String password
    ) {}
}
