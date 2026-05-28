#!/usr/bin/env bash
set -e
COOK1=/tmp/c1.txt; COOK2=/tmp/c2.txt; COOK3=/tmp/c3.txt
BASE=http://localhost:8080
rm -f $COOK1 $COOK2 $COOK3

csrf() { grep -oE 'name="_csrf"[[:space:]]+value="[^"]+"' "$1" | head -1 | sed -E 's/.*value="([^"]+)".*/\1/'; }

su() {
  local cookies=$1 user=$2 pw=$3
  curl -s -c $cookies -b $cookies "$BASE/signup" -o /tmp/x.html
  curl -s -c $cookies -b $cookies -X POST "$BASE/signup" \
    --data-urlencode "_csrf=$(csrf /tmp/x.html)" --data-urlencode "username=$user" \
    --data-urlencode "email=$user@jvm.dev" --data-urlencode "password=$pw" -o /dev/null
  curl -s -c $cookies -b $cookies "$BASE/login" -o /tmp/x.html
  curl -s -c $cookies -b $cookies -X POST "$BASE/login" \
    --data-urlencode "_csrf=$(csrf /tmp/x.html)" --data-urlencode "username=$user" \
    --data-urlencode "password=$pw" -o /dev/null
}

PASS() { echo "  [PASS] $1"; }
FAIL() { echo "  [FAIL] $1"; }
check() { if eval "$1"; then PASS "$2"; else FAIL "$2"; fi; }

echo "=== users ==="
su $COOK1 alice password123
su $COOK2 bob password123
su $COOK3 charlie password123
echo "  3 users created"

echo
echo "=== alice creates tagged Q&A in c/java, mentioning @bob ==="
curl -s -c $COOK1 -b $COOK1 "$BASE/posts/new" -o /tmp/x.html
TOK=$(csrf /tmp/x.html)
BODY=$'@bob virtual threads question.\n\n```java\nsynchronized (lock) { } // pinning?\n```'
RED=$(curl -s -c $COOK1 -b $COOK1 -X POST "$BASE/posts" \
  --data-urlencode "_csrf=$TOK" --data-urlencode "community=java" \
  --data-urlencode "title=Virtual threads and pinning" \
  --data-urlencode "bodyMd=$BODY" --data-urlencode "postType=QUESTION" \
  --data-urlencode "tags=virtual-threads loom synchronization" \
  -o /dev/null -w "%{redirect_url}")
PID=$(echo "$RED" | grep -oE '[0-9]+$')
echo "  created /posts/$PID"

echo
echo "=== tags rendered ==="
curl -s "$RED" -o /tmp/det.html -w "  detail: %{http_code}\n"
check 'grep -q "#virtual-threads" /tmp/det.html' "tag #virtual-threads rendered"
check 'grep -q "#loom" /tmp/det.html' "tag #loom rendered"
check 'grep -q "language-java" /tmp/det.html' "java code block class"

echo
echo "=== bob got mention notification ==="
curl -s -c $COOK2 -b $COOK2 "$BASE/notifications" -o /tmp/bn.html -w "  /notifications: %{http_code}\n"
check 'grep -q "mentioned you" /tmp/bn.html' "mention notif"
check 'grep -q "Virtual threads and pinning" /tmp/bn.html' "target post linked"

echo
echo "=== unread count in nav bell ==="
curl -s -c $COOK2 -b $COOK2 "$BASE/" -o /tmp/bfeed.html
BELL=$(grep -oE 'class="bell-count"[^>]*>[^<]+' /tmp/bfeed.html | head -1)
echo "  bell: $BELL"
check '[ -n "$BELL" ]' "bell shows unread count"

echo
echo "=== bob comments, mentioning @charlie ==="
curl -s -c $COOK2 -b $COOK2 "$RED" -o /tmp/x.html
TOK=$(csrf /tmp/x.html)
curl -s -c $COOK2 -b $COOK2 -X POST "$BASE/posts/$PID/comments" \
  --data-urlencode "_csrf=$TOK" --data-urlencode "body=Use **ReentrantLock**. @charlie can confirm." \
  --data-urlencode "parentCommentId=" -o /dev/null -w "  comment: %{http_code}\n"

curl -s -c $COOK1 -b $COOK1 "$BASE/notifications" -o /tmp/an.html
check 'grep -q "commented on your post" /tmp/an.html' "alice notified (REPLY_TO_POST)"
curl -s -c $COOK3 -b $COOK3 "$BASE/notifications" -o /tmp/cn.html
check 'grep -q "mentioned you" /tmp/cn.html' "charlie notified (MENTION in comment)"

echo
echo "=== charlie replies to bob's comment ==="
curl -s -c $COOK3 -b $COOK3 "$RED" -o /tmp/x.html
TOK=$(csrf /tmp/x.html)
# Find bob's comment id from the reply-N id attribute or accept-N action
CID=$(grep -oE 'reply-[0-9]+' /tmp/x.html | head -1 | grep -oE '[0-9]+')
echo "  bob's comment id: $CID"
curl -s -c $COOK3 -b $COOK3 -X POST "$BASE/posts/$PID/comments" \
  --data-urlencode "_csrf=$TOK" --data-urlencode "body=Confirmed, ReentrantLock works." \
  --data-urlencode "parentCommentId=$CID" -o /dev/null -w "  reply: %{http_code}\n"
curl -s -c $COOK2 -b $COOK2 "$BASE/notifications" -o /tmp/bn.html
check 'grep -q "replied to your comment" /tmp/bn.html' "bob notified (REPLY_TO_COMMENT)"

echo
echo "=== alice marks bob's comment as accepted ==="
curl -s -c $COOK1 -b $COOK1 "$RED" -o /tmp/x.html
TOK=$(csrf /tmp/x.html)
curl -s -c $COOK1 -b $COOK1 -X POST "$BASE/posts/$PID/accept/$CID" \
  --data-urlencode "_csrf=$TOK" -o /dev/null -w "  accept: %{http_code}\n"
curl -s -c $COOK2 -b $COOK2 "$BASE/notifications" -o /tmp/bn.html
check 'grep -q "marked your answer as accepted" /tmp/bn.html' "bob notified (ACCEPTED_ANSWER)"

echo
echo "=== mark-all-read ==="
curl -s -c $COOK2 -b $COOK2 "$BASE/notifications" -o /tmp/x.html
TOK=$(csrf /tmp/x.html)
curl -s -c $COOK2 -b $COOK2 -X POST "$BASE/notifications/read-all" \
  --data-urlencode "_csrf=$TOK" -o /dev/null -w "  mark all read: %{http_code}\n"
curl -s -c $COOK2 -b $COOK2 "$BASE/" -o /tmp/x.html
if grep -q 'class="bell-count"' /tmp/x.html; then FAIL "bell count cleared"; else PASS "bell count cleared (0 unread)"; fi

echo
echo "=== tag filter on community ==="
curl -s "$BASE/c/java?tag=virtual-threads" -o /tmp/cf.html -w "  /c/java?tag=virtual-threads: %{http_code}\n"
check 'grep -q "Virtual threads and pinning" /tmp/cf.html' "tagged post in filter"
curl -s "$BASE/c/java?tag=does-not-exist" -o /tmp/cf.html
if grep -q "Virtual threads and pinning" /tmp/cf.html; then FAIL "no match for fake tag"; else PASS "no match for fake tag"; fi

echo
echo "=== search ==="
curl -s "$BASE/search?q=pinning" -o /tmp/sr.html -w "  /search?q=pinning: %{http_code}\n"
check 'grep -q "Virtual threads and pinning" /tmp/sr.html' "title hit"
curl -s "$BASE/search?q=ReentrantLock" -o /tmp/sr.html
check 'grep -q "ReentrantLock" /tmp/sr.html' "comment body hit"
curl -s "$BASE/search?q=nonexistentphrase" -o /tmp/sr.html
check 'grep -q "No matching" /tmp/sr.html' "empty results message"

echo
echo "=== user profile pages ==="
curl -s "$BASE/u/alice" -o /tmp/pr.html -w "  /u/alice: %{http_code}\n"
check 'grep -q "u/alice" /tmp/pr.html' "profile loads"
check 'grep -q "Virtual threads and pinning" /tmp/pr.html' "alice posts listed"
curl -s "$BASE/u/bob" -o /tmp/pr.html
check 'grep -q "ReentrantLock" /tmp/pr.html' "bob comments shown"
curl -s "$BASE/u/nonexistent" -o /dev/null -w "  /u/nonexistent: %{http_code} (expect 404)\n"

echo
echo "=== soft delete comment ==="
curl -s -c $COOK3 -b $COOK3 "$RED" -o /tmp/x.html
TOK=$(csrf /tmp/x.html)
RID=$(grep -oE 'action="/comments/[0-9]+/delete"' /tmp/x.html | tail -1 | grep -oE '[0-9]+')
echo "  charlie's reply id: $RID"
curl -s -c $COOK3 -b $COOK3 -X POST "$BASE/comments/$RID/delete" \
  --data-urlencode "_csrf=$TOK" --data-urlencode "postId=$PID" \
  -o /dev/null -w "  delete: %{http_code}\n"
curl -s "$RED" -o /tmp/x.html
check 'grep -qF "[deleted by author]" /tmp/x.html' "deleted placeholder shown"
if grep -q "Confirmed, ReentrantLock works" /tmp/x.html; then FAIL "deleted body hidden"; else PASS "deleted body hidden"; fi

echo
echo "=== soft delete post ==="
curl -s -c $COOK1 -b $COOK1 "$RED" -o /tmp/x.html
TOK=$(csrf /tmp/x.html)
curl -s -c $COOK1 -b $COOK1 -X POST "$BASE/posts/$PID/delete" \
  --data-urlencode "_csrf=$TOK" -o /dev/null -w "  delete: %{http_code}\n"
curl -s "$BASE/" -o /tmp/x.html
if grep -q "Virtual threads and pinning" /tmp/x.html; then FAIL "deleted post hidden from feed"; else PASS "deleted post hidden from feed"; fi

echo
echo "=== report a post ==="
curl -s -c $COOK2 -b $COOK2 "$BASE/posts/new" -o /tmp/x.html
TOK=$(csrf /tmp/x.html)
RED2=$(curl -s -c $COOK2 -b $COOK2 -X POST "$BASE/posts" \
  --data-urlencode "_csrf=$TOK" --data-urlencode "community=kotlin" \
  --data-urlencode "title=Kotlin post to report" \
  --data-urlencode "bodyMd=Body for reporting test." \
  --data-urlencode "postType=DISCUSSION" --data-urlencode "tags=" \
  -o /dev/null -w "%{redirect_url}")
PID2=$(echo "$RED2" | grep -oE '[0-9]+$')
curl -s -c $COOK1 -b $COOK1 "$RED2" -o /tmp/x.html
TOK=$(csrf /tmp/x.html)
curl -s -c $COOK1 -b $COOK1 -X POST "$BASE/posts/$PID2/report" \
  --data-urlencode "_csrf=$TOK" --data-urlencode "reason=Spam" \
  -o /dev/null -w "  report: %{http_code}\n"
curl -s -c $COOK1 -b $COOK1 -X POST "$BASE/posts/$PID2/report" \
  --data-urlencode "_csrf=$TOK" --data-urlencode "reason=Spam" \
  -o /dev/null -w "  report (dup): %{http_code}\n"

echo
echo "=== auth gates ==="
for p in /search /u/alice /c /c/java /notifications; do
  CODE=$(curl -s -o /dev/null -w "%{http_code}" "$BASE$p")
  echo "  $CODE $p"
done
