package com.automifychat.app.search;

import com.automifychat.app.comment.Comment;
import com.automifychat.app.comment.CommentRepository;
import com.automifychat.app.post.Post;
import com.automifychat.app.post.PostService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class SearchController {

    private final PostService postService;
    private final CommentRepository commentRepository;

    public SearchController(PostService postService, CommentRepository commentRepository) {
        this.postService = postService;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "q", required = false) String q, Model model) {
        String query = q == null ? "" : q.trim();
        List<Post> postHits = query.isBlank() ? List.of() : postService.search(query);
        List<Comment> commentHits = query.isBlank() ? List.of() : commentRepository.searchByText(query);
        model.addAttribute("q", query);
        model.addAttribute("postHits", postHits);
        model.addAttribute("commentHits", commentHits);
        return "search";
    }
}
