/*L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-password-change/LICENSE.txt for details.
L*/

--login is as sys

DROP VIEW CADSR_USERS;
 
CREATE OR REPLACE FORCE VIEW CADSR_USERS
(
   USERNAME,
   USER_ID,
   ACCOUNT_STATUS,
   LOCK_DATE,
   EXPIRY_DATE,
   DEFAULT_TABLESPACE,
   TEMPORARY_TABLESPACE,
   CREATED,
   PROFILE,
   INITIAL_RSRC_CONSUMER_GROUP,
   EXTERNAL_NAME,
   PTIME
)
AS
   SELECT d."USERNAME",
          d."USER_ID",
          d."ACCOUNT_STATUS",
          d."LOCK_DATE",
          d."EXPIRY_DATE",
          d."DEFAULT_TABLESPACE",
          d."TEMPORARY_TABLESPACE",
          d."CREATED",
          d."PROFILE",
          d."INITIAL_RSRC_CONSUMER_GROUP",
          d."EXTERNAL_NAME",
          u.ptime
     FROM sys.user$ u, sys.dba_users d
    WHERE d.username = u.name AND d.default_tablespace = 'USERS';
 
DROP SYNONYM CADSRPASSWORDCHANGE.CADSR_USERS;
 
CREATE OR REPLACE SYNONYM CADSRPASSWORDCHANGE.CADSR_USERS FOR CADSR_USERS;
 
GRANT SELECT ON CADSR_USERS TO CADSRPASSWORDCHANGE;
