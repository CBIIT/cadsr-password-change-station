/*L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-password-change/LICENSE.txt for details.
L*/

--@drop_security_questions_tables.sql

@create_security_questions_tables.sql

@delete-web-links.sql

@insert-web-links.sql

--@drop_password_expiration_queue.sql

@create_password_expiration_queue.sql

@delete-notification-email-details.sql

@insert-notification-email-details.sql
