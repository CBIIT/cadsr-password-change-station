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
--	attempted_count1 number(19),
--	attempted_count2 number(19),
--	attempted_count3 number(19)
);

ALTER TABLE USER_SECURITY_QUESTIONS ADD CONSTRAINT pk_USER_SECURITY_QUESTIONS PRIMARY KEY (
	ua_name
);

CREATE UNIQUE INDEX inx_qna ON USER_SECURITY_QUESTIONS (
	ua_name
);