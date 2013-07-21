/*L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-password-change/LICENSE.txt for details.
L*/

-- @drop_cadsrpasswordchange_user.sql

@create_cadsrpasswordchange_user.sql

@create_sys.cadsr_users.sql

@create_password_verify_cadsr_user.sql

@create_password_change_prevent.sql
