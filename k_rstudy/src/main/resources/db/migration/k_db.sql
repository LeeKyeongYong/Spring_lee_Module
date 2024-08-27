CREATE FUNCTION get_author_name(author_id BIGINT) RETURNS VARCHAR(255)
    DETERMINISTIC
BEGIN
    DECLARE author_name VARCHAR(255);
SELECT username INTO author_name FROM member WHERE id = author_id;
RETURN author_name;
END;

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
        END AS isAdmin
FROM
    member m;