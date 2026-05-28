package com.automifychat.app.vote;

import com.automifychat.app.auth.UserAccount;
import com.automifychat.app.auth.UserAccountRepository;
import com.automifychat.app.comment.Comment;
import com.automifychat.app.comment.CommentRepository;
import com.automifychat.app.post.Post;
import com.automifychat.app.post.PostRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteService {

    private final VoteRepository votes;
    private final PostRepository posts;
    private final CommentRepository comments;
    private final UserAccountRepository users;

    public VoteService(VoteRepository votes,
                       PostRepository posts,
                       CommentRepository comments,
                       UserAccountRepository users) {
        this.votes = votes;
        this.posts = posts;
        this.comments = comments;
        this.users = users;
    }

    public record Result(int newScore, int currentVote) {}

    @Transactional
    public Result vote(String voterUsername, Vote.TargetType type, Long targetId, short value) {
        if (value != 1 && value != -1) throw new IllegalArgumentException("Vote value must be +1 or -1");
        UserAccount voter = users.findByUsername(voterUsername)
                .orElseThrow(() -> new IllegalStateException("No user: " + voterUsername));

        UserAccount targetAuthor;
        int previousScore;
        int delta;

        var existing = votes.findByUserIdAndTargetTypeAndTargetId(voter.getId(), type, targetId);

        if (type == Vote.TargetType.POST) {
            Post post = posts.findById(targetId)
                    .orElseThrow(() -> new IllegalStateException("No post: " + targetId));
            targetAuthor = post.getAuthor();
            previousScore = post.getScore();
            short newValue;
            if (existing.isPresent() && existing.get().getValue() == value) {
                // toggle off
                delta = -value;
                votes.delete(existing.get());
                newValue = 0;
            } else if (existing.isPresent()) {
                // flip
                delta = (short) (value - existing.get().getValue());
                existing.get().setValue(value);
                newValue = value;
            } else {
                delta = value;
                votes.save(new Vote(voter.getId(), type, targetId, value));
                newValue = value;
            }
            post.addScore(delta);
            applyKarma(voter, targetAuthor, delta);
            return new Result(previousScore + delta, newValue);
        } else {
            Comment comment = comments.findById(targetId)
                    .orElseThrow(() -> new IllegalStateException("No comment: " + targetId));
            targetAuthor = comment.getAuthor();
            previousScore = comment.getScore();
            short newValue;
            if (existing.isPresent() && existing.get().getValue() == value) {
                delta = -value;
                votes.delete(existing.get());
                newValue = 0;
            } else if (existing.isPresent()) {
                delta = (short) (value - existing.get().getValue());
                existing.get().setValue(value);
                newValue = value;
            } else {
                delta = value;
                votes.save(new Vote(voter.getId(), type, targetId, value));
                newValue = value;
            }
            comment.addScore(delta);
            applyKarma(voter, targetAuthor, delta);
            return new Result(previousScore + delta, newValue);
        }
    }

    private void applyKarma(UserAccount voter, UserAccount author, int delta) {
        // No self-karma — voting on your own post is allowed but karma stays put
        if (!voter.getId().equals(author.getId())) {
            author.addKarma(delta);
        }
    }
}
