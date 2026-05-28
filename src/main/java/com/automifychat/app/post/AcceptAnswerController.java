package com.automifychat.app.post;

import com.automifychat.app.auth.UserAccount;
import com.automifychat.app.auth.UserAccountRepository;
import com.automifychat.app.comment.Comment;
import com.automifychat.app.comment.CommentRepository;
import com.automifychat.app.notification.NotificationService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AcceptAnswerController {

    private final PostRepository posts;
    private final CommentRepository comments;
    private final UserAccountRepository users;
    private final NotificationService notificationService;

    public AcceptAnswerController(PostRepository posts, CommentRepository comments,
                                  UserAccountRepository users, NotificationService notificationService) {
        this.posts = posts;
        this.comments = comments;
        this.users = users;
        this.notificationService = notificationService;
    }

    @PostMapping("/posts/{postId}/accept/{commentId}")
    @Transactional
    public String accept(@PathVariable Long postId,
                         @PathVariable Long commentId,
                         @AuthenticationPrincipal UserDetails principal,
                         RedirectAttributes redirect) {
        Post post = posts.findByIdWithRelations(postId)
                .orElseThrow(() -> new PostController.PostNotFoundException(postId));
        if (post.getPostType() != PostType.QUESTION) {
            throw new InvalidAcceptException("Only question posts can have accepted answers");
        }
        if (!post.getAuthor().getUsername().equals(principal.getUsername())) {
            throw new AccessDeniedException("Only the post author can accept an answer");
        }
        Comment comment = comments.findById(commentId)
                .orElseThrow(() -> new InvalidAcceptException("Comment not found"));
        if (!comment.getPost().getId().equals(postId)) {
            throw new InvalidAcceptException("Comment does not belong to this post");
        }
        post.setAcceptedAnswerCommentId(commentId);
        UserAccount actor = users.findByUsername(principal.getUsername()).orElseThrow();
        notificationService.onAnswerAccepted(comment, actor);
        redirect.addFlashAttribute("flash", "Marked as accepted answer");
        return "redirect:/posts/" + postId;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public static class InvalidAcceptException extends RuntimeException {
        public InvalidAcceptException(String message) { super(message); }
    }
}
