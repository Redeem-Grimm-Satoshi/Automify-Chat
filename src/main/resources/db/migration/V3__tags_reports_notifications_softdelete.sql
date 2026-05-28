-- Soft delete
ALTER TABLE posts    ADD COLUMN deleted_at TIMESTAMPTZ;
ALTER TABLE comments ADD COLUMN deleted_at TIMESTAMPTZ;

-- Tags (community-scoped; nullable community_id reserved for future global tags)
CREATE TABLE tags (
    id           BIGSERIAL PRIMARY KEY,
    community_id BIGINT      REFERENCES communities(id) ON DELETE CASCADE,
    name         VARCHAR(40) NOT NULL,
    created_at   TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    UNIQUE (community_id, name)
);
CREATE INDEX idx_tags_name ON tags (name);

CREATE TABLE post_tags (
    post_id BIGINT NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    tag_id  BIGINT NOT NULL REFERENCES tags(id)  ON DELETE CASCADE,
    PRIMARY KEY (post_id, tag_id)
);
CREATE INDEX idx_post_tags_tag ON post_tags (tag_id);

-- Reports
CREATE TABLE reports (
    id          BIGSERIAL PRIMARY KEY,
    reporter_id BIGINT       NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    target_type VARCHAR(16)  NOT NULL CHECK (target_type IN ('POST', 'COMMENT')),
    target_id   BIGINT       NOT NULL,
    reason      VARCHAR(280) NOT NULL,
    status      VARCHAR(16)  NOT NULL DEFAULT 'OPEN'
                  CHECK (status IN ('OPEN', 'RESOLVED', 'DISMISSED')),
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW(),
    UNIQUE (reporter_id, target_type, target_id)
);
CREATE INDEX idx_reports_status ON reports (status, created_at DESC);

-- Notifications
CREATE TABLE notifications (
    id              BIGSERIAL PRIMARY KEY,
    recipient_id    BIGINT      NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    actor_id        BIGINT      REFERENCES users(id) ON DELETE SET NULL,
    notif_type      VARCHAR(32) NOT NULL CHECK (notif_type IN
                      ('REPLY_TO_POST', 'REPLY_TO_COMMENT', 'ACCEPTED_ANSWER', 'MENTION')),
    target_post_id  BIGINT      REFERENCES posts(id)    ON DELETE CASCADE,
    target_comment_id BIGINT    REFERENCES comments(id) ON DELETE CASCADE,
    excerpt         VARCHAR(280),
    read_at         TIMESTAMPTZ,
    created_at      TIMESTAMPTZ NOT NULL DEFAULT NOW()
);
CREATE INDEX idx_notifications_recipient ON notifications (recipient_id, created_at DESC);
CREATE INDEX idx_notifications_unread    ON notifications (recipient_id) WHERE read_at IS NULL;
