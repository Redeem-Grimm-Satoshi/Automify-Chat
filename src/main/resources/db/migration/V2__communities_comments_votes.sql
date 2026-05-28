CREATE TABLE communities (
    id          BIGSERIAL PRIMARY KEY,
    slug        VARCHAR(40)  NOT NULL UNIQUE,
    name        VARCHAR(80)  NOT NULL,
    description VARCHAR(280) NOT NULL DEFAULT '',
    owner_id    BIGINT       REFERENCES users(id) ON DELETE SET NULL,
    created_at  TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

CREATE TABLE memberships (
    user_id      BIGINT      NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    community_id BIGINT      NOT NULL REFERENCES communities(id) ON DELETE CASCADE,
    role         VARCHAR(16) NOT NULL DEFAULT 'MEMBER'
                   CHECK (role IN ('MEMBER', 'MOD', 'OWNER')),
    joined_at    TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (user_id, community_id)
);

ALTER TABLE posts ADD COLUMN community_id BIGINT REFERENCES communities(id) ON DELETE CASCADE;
ALTER TABLE posts ADD COLUMN accepted_answer_comment_id BIGINT;

CREATE INDEX idx_posts_community ON posts (community_id, created_at DESC);
CREATE INDEX idx_posts_score     ON posts (score DESC);

CREATE TABLE comments (
    id                BIGSERIAL PRIMARY KEY,
    post_id           BIGINT      NOT NULL REFERENCES posts(id) ON DELETE CASCADE,
    author_id         BIGINT      NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    parent_comment_id BIGINT      REFERENCES comments(id) ON DELETE CASCADE,
    body_md           TEXT        NOT NULL,
    score             INTEGER     NOT NULL DEFAULT 0,
    created_at        TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX idx_comments_post   ON comments (post_id, created_at);
CREATE INDEX idx_comments_parent ON comments (parent_comment_id);

ALTER TABLE posts ADD CONSTRAINT fk_accepted_answer
    FOREIGN KEY (accepted_answer_comment_id) REFERENCES comments(id) ON DELETE SET NULL;

CREATE TABLE votes (
    user_id     BIGINT      NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    target_type VARCHAR(16) NOT NULL CHECK (target_type IN ('POST', 'COMMENT')),
    target_id   BIGINT      NOT NULL,
    vote_value  SMALLINT    NOT NULL CHECK (vote_value IN (-1, 1)),
    created_at  TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    PRIMARY KEY (user_id, target_type, target_id)
);

CREATE INDEX idx_votes_target ON votes (target_type, target_id);
