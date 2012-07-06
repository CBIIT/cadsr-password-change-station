CREATE TABLE securityQnA (
	username varchar2(30) NOT NULL,
	question1 varchar2(500) NOT NULL,
	answer1 varchar2(500) NOT NULL,
	question2 varchar2(500) NOT NULL,
	answer2 varchar2(500) NOT NULL,
	question3 varchar2(500) NOT NULL,
	answer3 varchar2(500) NOT NULL,
	updated_date date NOT NULL
);

ALTER TABLE securityQnA ADD CONSTRAINT pk_securityQnA PRIMARY KEY (
	username
);

CREATE UNIQUE INDEX inx_qna ON securityQnA (
	username
);