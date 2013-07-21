/*L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-password-change/LICENSE.txt for details.
L*/

CREATE OR REPLACE TRIGGER SBREXT." _TRG" 
 BEFORE INSERT OR UPDATE
 ON USER_SECURITY_QUESTIONS
 REFERENCING OLD AS OLD NEW AS NEW
 FOR EACH ROW
-- PL/SQL Block
BEGIN

  IF :new.DATE_MODIFIED IS NULL THEN
    :new.DATE_MODIFIED := SYSDATE;
  END IF;

END;