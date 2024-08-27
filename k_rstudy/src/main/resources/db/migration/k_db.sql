CREATE FUNCTION get_author_name(author_id BIGINT) RETURNS VARCHAR(255)
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
    m.roleType,
    m.password,
    CASE
        WHEN m.roleType = 'ADMIN' THEN 'ROLE_ADMIN'
        WHEN m.roleType = 'USER' THEN 'ROLE_USER'
        ELSE 'ROLE_UNKNOWN'
        END AS authorities,
    CASE
        WHEN m.roleType = 'ADMIN' THEN TRUE
        ELSE FALSE
        END AS isAdmin
FROM
    member m;
