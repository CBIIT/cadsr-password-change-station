--run as sbrext

CREATE TABLE USER_SECURITY_QUESTIONS (
    ua_name varchar2(30) NOT NULL,
    question1 varchar2(500) NOT NULL,
    answer1 varchar2(500) NOT NULL,
    question2 varchar2(500) NOT NULL,
    answer2 varchar2(500) NOT NULL,
    question3 varchar2(500) NOT NULL,
    answer3 varchar2(500) NOT NULL,
    date_modified date NOT NULL,
    attempted_count number(19)
)
/
grant select, insert, update, delete on user_security_questions to cadsrpasswordchange
/
