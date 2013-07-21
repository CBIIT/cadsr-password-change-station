/*L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-password-change/LICENSE.txt for details.
L*/

--run as sbrext
update sbr.user_accounts_view set enabled_ind = 'No' where ua_name like 'DEVELOPER%'
/