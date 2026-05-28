package com.automifychat.app.comment;

import com.automifychat.app.post.Post;
import com.automifychat.app.post.PostService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CommentController {

    private final CommentService commentService;
    private final PostService postService;

    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @PostMapping("/posts/{postId}/comments")
    public String createComment(@PathVariable Long postId,
                                @Valid @ModelAttribute("commentForm") CommentService.NewCommentForm form,
                                BindingResult binding,
                                @AuthenticationPrincipal UserDetails principal,
                                Model model,
                                RedirectAttributes redirect) {
        if (binding.hasErrors()) {
            Post post = postService.findById(postId).orElseThrow();
            model.addAttribute("post", post);
            model.addAttribute("comments", commentService.toTree(
                    commentService.commentsForPost(postId), post.getAcceptedAnswerCommentId()));
            return "posts/detail";
        }
        commentService.createComment(principal.getUsername(), postId, form.parentCommentId(), form.body());
        redirect.addFlashAttribute("flash", "Comment posted");
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/comments/{id}/delete")
    public String deleteComment(@PathVariable Long id,
                                @RequestParam Long postId,
                                @AuthenticationPrincipal UserDetails principal,
                                RedirectAttributes redirect) {
        commentService.softDelete(id, principal.getUsername());
        redirect.addFlashAttribute("flash", "Comment deleted");
        return "redirect:/posts/" + postId;
    }
}
