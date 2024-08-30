
DROP FUNCTION get_author_name;

commit;

CREATE FUNCTION get_author_name(author_id BIGINT) RETURNS VARCHAR(255)
    DETERMINISTIC
BEGIN
    DECLARE author_name VARCHAR(255);
SELECT username INTO author_name FROM member WHERE id = author_id;
RETURN IFNULL(author_name, '사용자 없음');
END;

commit;

DROP VIEW IF EXISTS member_view;

commit;

CREATE VIEW member_view AS
SELECT
    m.id AS member_id,
    m.userid,
    m.username,
    m.role_type,
    m.password,
    CASE
        WHEN m.role_type = 'ROLE_ADMIN' THEN '관리자'
        WHEN m.role_type = 'ROLE_MEMBER' THEN '일반 사용자'
        WHEN m.role_type = 'ROLE_HEADHUNTER' THEN '헤드 헌터'
        WHEN m.role_type = 'ROLE_MANAGER' THEN '기업 관리자'
        WHEN m.role_type = 'ROLE_HR' THEN '채용 담당자'
        ELSE 'ROLE_MEMBER'
        END AS authorities,
    CASE
        WHEN m.role_type = 'ROLE_ADMIN' THEN TRUE
        ELSE FALSE
        END AS is_admin
FROM
    member m;

DROP VIEW IF EXISTS post_details;

CREATE VIEW post_details AS
SELECT
    p.id AS post_id, #게시글 번호
    p.title AS post_title, #게시글 제목
    p.create_date AS post_create_date, #게시글 작성일
    p.modify_date AS post_modify_date, #게시글 수정일
    p.hit AS post_hit, #게시글 조회수
    get_author_name(p.author_id) AS post_author_id, #게시글 작성자
    pc.id AS comment_id, #댓글번호
    get_author_name(pc.author_id) AS comment_author_id, #댓글작성자
    pc.create_date AS comment_create_date, #댓글 작성일
    get_author_name(pl.member_id) AS like_member_id,  #좋아요 작성자
    pl.create_date AS like_create_date #좋아요 작성일
FROM
    post p
        LEFT JOIN
    post_comment pc ON p.id = pc.post_id
        LEFT JOIN
    post_like pl ON p.id = pl.post_id
ORDER BY
    p.id, pc.id, pl.create_date;


DROP VIEW IF EXISTS post_like_detail;

CREATE VIEW post_like_detail AS
sELECT
    p.id AS post_id,
    p.title AS post_title,
    p.create_date AS post_create_date,
    p.modify_date AS post_modify_date,
    p.hit AS post_hit,
    p.author_id AS post_author_id,
    pl.member_id AS like_member_id,
    pl.create_date AS like_create_date
FROM
    post p
        LEFT JOIN
    post_like pl ON p.id = pl.post_id
ORDER BY
    p.id, pl.create_date;

DROP VIEW IF EXISTS post_comment_detail;

CREATE VIEW post_comment_detail AS
SELECT
    p.id AS post_id,
    p.title AS post_title,
    p.create_date AS post_create_date,
    p.modify_date AS post_modify_date,
    p.hit AS post_hit,
    p.author_id AS post_author_id,
    pc.id AS comment_id,
    pc.author_id AS comment_author_id,
    pc.create_date AS comment_create_date
FROM
    post p
        LEFT JOIN
    post_comment pc ON p.id = pc.post_id
ORDER BY
    p.id, pc.id;

DROP VIEW IF EXISTS post_comment_count;

CREATE VIEW post_comment_count AS
SELECT
    p.id AS post_id,
    p.title AS post_title,
    COUNT(pc.id) AS comment_count
FROM
    post p
        LEFT JOIN
    post_comment pc ON p.id = pc.post_id
GROUP BY
    p.id, p.title;

DROP VIEW IF EXISTS post_like_count;

SELECT
    p.id AS post_id,
    p.title AS post_title,
    COUNT(pl.id) AS like_count
FROM
    post p
        LEFT JOIN
    post_like pl ON p.id = pl.post_id
GROUP BY
    p.id, p.title;