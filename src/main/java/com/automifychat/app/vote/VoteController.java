package com.automifychat.app.vote;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping(value = "/posts/{id}/vote", produces = MediaType.TEXT_HTML_VALUE)
    public String votePost(@PathVariable Long id,
                           @RequestParam short value,
                           @AuthenticationPrincipal UserDetails principal,
                           Model model) {
        var result = voteService.vote(principal.getUsername(), Vote.TargetType.POST, id, value);
        model.addAttribute("targetId", id);
        model.addAttribute("targetType", "post");
        model.addAttribute("score", result.newScore());
        model.addAttribute("currentVote", result.currentVote());
        return "fragments/vote :: widget";
    }

    @PostMapping(value = "/comments/{id}/vote", produces = MediaType.TEXT_HTML_VALUE)
    public String voteComment(@PathVariable Long id,
                              @RequestParam short value,
                              @AuthenticationPrincipal UserDetails principal,
                              Model model) {
        var result = voteService.vote(principal.getUsername(), Vote.TargetType.COMMENT, id, value);
        model.addAttribute("targetId", id);
        model.addAttribute("targetType", "comment");
        model.addAttribute("score", result.newScore());
        model.addAttribute("currentVote", result.currentVote());
        return "fragments/vote :: widget";
    }
}
