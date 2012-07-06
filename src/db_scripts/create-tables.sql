CREATE TABLE User_Security_Questions (
	ua_name varchar2(30) NOT NULL,
	question1 varchar2(500) NOT NULL,
	answer1 varchar2(500) NOT NULL,
	question2 varchar2(500) NOT NULL,
	answer2 varchar2(500) NOT NULL,
	question3 varchar2(500) NOT NULL,
	answer3 varchar2(500) NOT NULL,
	date_modified date NOT NULL
);

ALTER TABLE User_Security_Questions ADD CONSTRAINT pk_User_Security_Questions PRIMARY KEY (
	ua_name
);

CREATE UNIQUE INDEX inx_qna ON User_Security_Questions (
	ua_name
);