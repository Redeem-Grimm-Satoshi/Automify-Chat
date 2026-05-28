package com.automifychat.app.post;

import com.automifychat.app.comment.Comment;
import com.automifychat.app.comment.CommentService;
import com.automifychat.app.community.CommunityService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PostController {

    private final PostService postService;
    private final CommunityService communityService;
    private final CommentService commentService;

    public PostController(PostService postService,
                          CommunityService communityService,
                          CommentService commentService) {
        this.postService = postService;
        this.communityService = communityService;
        this.commentService = commentService;
    }

    @GetMapping("/")
    public String home(@RequestParam(name = "sort", required = false, defaultValue = "new") String sort,
                       Model model) {
        model.addAttribute("posts", postService.globalFeed(sort));
        model.addAttribute("sort", sort);
        model.addAttribute("communities", communityService.all());
        return "feed";
    }

    @GetMapping("/posts/new")
    public String newPostPage(@RequestParam(name = "c", required = false) String communitySlug, Model model) {
        if (!model.containsAttribute("form")) {
            model.addAttribute("form", new NewPostForm(
                    communitySlug == null ? "" : communitySlug, "", "", PostType.DISCUSSION, ""));
        }
        model.addAttribute("communities", communityService.all());
        return "posts/new";
    }

    @PostMapping("/posts")
    public String createPost(@Valid @ModelAttribute("form") NewPostForm form,
                             BindingResult binding,
                             @AuthenticationPrincipal UserDetails principal,
                             Model model,
                             RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            model.addAttribute("communities", communityService.all());
            return "posts/new";
        }
        Post saved = postService.createPost(
                principal.getUsername(),
                form.community(),
                form.title(),
                form.bodyMd(),
                form.postType(),
                form.tags()
        );
        redirect.addFlashAttribute("flash", "Post created in c/" + saved.getCommunity().getSlug());
        return "redirect:/posts/" + saved.getId();
    }

    @GetMapping("/posts/{id}")
    public String postDetail(@PathVariable Long id, Model model) {
        Post post = postService.findById(id).orElseThrow(() -> new PostNotFoundException(id));
        List<Comment> comments = commentService.commentsForPost(post.getId());
        model.addAttribute("post", post);
        model.addAttribute("comments", commentService.toTree(comments, post.getAcceptedAnswerCommentId()));
        model.addAttribute("commentForm", new CommentService.NewCommentForm("", null));
        return "posts/detail";
    }

    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable Long id,
                             @AuthenticationPrincipal UserDetails principal,
                             RedirectAttributes redirect) {
        postService.softDelete(id, principal.getUsername());
        redirect.addFlashAttribute("flash", "Post deleted");
        return "redirect:/";
    }

    public record NewPostForm(
            @NotBlank String community,
            @NotBlank @Size(min = 5, max = 300) String title,
            @NotBlank @Size(min = 10, max = 20000) String bodyMd,
            @NotNull PostType postType,
            String tags
    ) {}

    public static class PostNotFoundException extends RuntimeException {
        public PostNotFoundException(Long id) { super("Post not found: " + id); }
    }
}
