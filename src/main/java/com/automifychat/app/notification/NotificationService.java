package com.automifychat.app.notification;

import com.automifychat.app.auth.UserAccount;
import com.automifychat.app.auth.UserAccountRepository;
import com.automifychat.app.comment.Comment;
import com.automifychat.app.post.Post;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class NotificationService {

    private static final Pattern MENTION = Pattern.compile("@([A-Za-z0-9_]{3,32})");
    private static final int EXCERPT_MAX = 200;

    private final NotificationRepository notifications;
    private final UserAccountRepository users;

    public NotificationService(NotificationRepository notifications, UserAccountRepository users) {
        this.notifications = notifications;
        this.users = users;
    }

    @Transactional
    public void onCommentCreated(Comment comment, Long parentCommentId, UserAccount parentCommentAuthor) {
        Post post = comment.getPost();
        UserAccount actor = comment.getAuthor();
        Set<Long> notified = new HashSet<>();
        notified.add(actor.getId()); // never notify yourself

        // Top-level → notify post author
        if (parentCommentId == null) {
            UserAccount postAuthor = post.getAuthor();
            if (notified.add(postAuthor.getId())) {
                notifications.save(new Notification(postAuthor, actor, Notification.Type.REPLY_TO_POST,
                        post, comment, excerpt(comment.getBodyMd())));
            }
        } else if (parentCommentAuthor != null) {
            // Reply → notify parent comment author
            if (notified.add(parentCommentAuthor.getId())) {
                notifications.save(new Notification(parentCommentAuthor, actor, Notification.Type.REPLY_TO_COMMENT,
                        post, comment, excerpt(comment.getBodyMd())));
            }
        }

        // Mentions in the comment body
        notifyMentions(comment.getBodyMd(), actor, post, comment, notified);
    }

    @Transactional
    public void onPostCreated(Post post) {
        // Only mentions matter for new posts
        notifyMentions(post.getBodyMd(), post.getAuthor(), post, null, new HashSet<>(Set.of(post.getAuthor().getId())));
    }

    @Transactional
    public void onAnswerAccepted(Comment acceptedComment, UserAccount actor) {
        UserAccount commentAuthor = acceptedComment.getAuthor();
        if (commentAuthor.getId().equals(actor.getId())) return;
        notifications.save(new Notification(
                commentAuthor, actor, Notification.Type.ACCEPTED_ANSWER,
                acceptedComment.getPost(), acceptedComment,
                excerpt(acceptedComment.getBodyMd())));
    }

    private void notifyMentions(String body, UserAccount actor, Post post, Comment comment, Set<Long> alreadyNotified) {
        if (body == null) return;
        Matcher m = MENTION.matcher(body);
        Set<String> seen = new HashSet<>();
        while (m.find()) {
            String username = m.group(1);
            if (!seen.add(username.toLowerCase())) continue;
            users.findByUsername(username).ifPresent(mentioned -> {
                if (alreadyNotified.add(mentioned.getId())) {
                    notifications.save(new Notification(mentioned, actor, Notification.Type.MENTION,
                            post, comment, excerpt(body)));
                }
            });
        }
    }

    private String excerpt(String body) {
        if (body == null) return null;
        String s = body.replaceAll("\\s+", " ").trim();
        return s.length() > EXCERPT_MAX ? s.substring(0, EXCERPT_MAX - 1) + "…" : s;
    }

    @Transactional(readOnly = true)
    public List<Notification> recentFor(String username) {
        UserAccount u = users.findByUsername(username).orElseThrow();
        return notifications.findRecentForUser(u.getId());
    }

    @Transactional(readOnly = true)
    public long unreadCount(String username) {
        UserAccount u = users.findByUsername(username).orElse(null);
        return u == null ? 0 : notifications.countUnreadForUser(u.getId());
    }

    @Transactional
    public void markAllRead(String username) {
        UserAccount u = users.findByUsername(username).orElseThrow();
        for (Notification n : notifications.findRecentForUser(u.getId())) {
            if (!n.isRead()) n.markRead();
        }
    }

    @Transactional
    public void markRead(Long notificationId, String username) {
        Notification n = notifications.findById(notificationId).orElseThrow();
        if (!n.getRecipient().getUsername().equals(username)) {
            throw new AccessDeniedException("Not your notification");
        }
        if (!n.isRead()) n.markRead();
    }
}
