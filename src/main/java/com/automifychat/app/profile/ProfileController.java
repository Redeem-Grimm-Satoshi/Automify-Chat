package com.automifychat.app.profile;

import com.automifychat.app.auth.UserAccount;
import com.automifychat.app.auth.UserAccountRepository;
import com.automifychat.app.comment.CommentService;
import com.automifychat.app.post.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ProfileController {

    private final UserAccountRepository users;
    private final PostService postService;
    private final CommentService commentService;

    public ProfileController(UserAccountRepository users,
                             PostService postService,
                             CommentService commentService) {
        this.users = users;
        this.postService = postService;
        this.commentService = commentService;
    }

    @GetMapping("/u/{username}")
    public String profile(@PathVariable String username, Model model) {
        UserAccount user = users.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        model.addAttribute("profile", user);
        model.addAttribute("posts", postService.postsByAuthor(username));
        model.addAttribute("comments", commentService.commentsByAuthor(username));
        return "profile";
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class UserNotFoundException extends RuntimeException {
        public UserNotFoundException(String u) { super("User not found: " + u); }
    }
}
