package com.automifychat.app.community;

import com.automifychat.app.post.PostService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CommunityController {

    private final CommunityService communityService;
    private final PostService postService;

    public CommunityController(CommunityService communityService, PostService postService) {
        this.communityService = communityService;
        this.postService = postService;
    }

    @GetMapping("/c")
    public String listCommunities(Model model) {
        model.addAttribute("communities", communityService.all());
        return "communities/list";
    }

    @GetMapping("/c/new")
    public String newCommunityPage(Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new NewCommunityForm("", "", ""));
        }
        return "communities/new";
    }

    @PostMapping("/c")
    public String createCommunity(@Valid @ModelAttribute("form") NewCommunityForm form,
                                  BindingResult binding,
                                  @AuthenticationPrincipal UserDetails principal,
                                  Model model,
                                  RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            return "communities/new";
        }
        try {
            Community c = communityService.createCommunity(principal.getUsername(), form.slug(), form.name(), form.description());
            redirect.addFlashAttribute("flash", "Community c/" + c.getSlug() + " created");
            return "redirect:/c/" + c.getSlug();
        } catch (CommunityService.CommunityException e) {
            model.addAttribute("communityError", e.getMessage());
            return "communities/new";
        }
    }

    @GetMapping("/c/{slug}")
    public String communityFeed(@PathVariable String slug,
                                @RequestParam(name = "sort", required = false, defaultValue = "new") String sort,
                                @RequestParam(name = "tag", required = false) String tag,
                                Model model) {
        Community c = communityService.findBySlug(slug)
                .orElseThrow(() -> new CommunityNotFoundException(slug));
        model.addAttribute("community", c);
        if (tag != null && !tag.isBlank()) {
            model.addAttribute("posts", postService.feedForCommunityAndTag(c.getId(), tag));
            model.addAttribute("activeTag", tag.toLowerCase());
        } else {
            model.addAttribute("posts", postService.feedForCommunity(c.getId(), sort));
        }
        model.addAttribute("sort", sort);
        return "communities/detail";
    }

    @PostMapping("/c/{slug}/join")
    public String join(@PathVariable String slug,
                       @AuthenticationPrincipal UserDetails principal,
                       RedirectAttributes redirect) {
        communityService.join(principal.getUsername(), slug);
        redirect.addFlashAttribute("flash", "Joined c/" + slug);
        return "redirect:/c/" + slug;
    }

    public record NewCommunityForm(
            @NotBlank @Size(min = 3, max = 40) String slug,
            @NotBlank @Size(min = 2, max = 80) String name,
            @Size(max = 280) String description
    ) {}

    public static class CommunityNotFoundException extends RuntimeException {
        public CommunityNotFoundException(String slug) { super("c/" + slug + " not found"); }
    }
}
