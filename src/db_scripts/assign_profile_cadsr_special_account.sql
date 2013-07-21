/*L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-password-change/LICENSE.txt for details.
L*/

--run as sbrext

alter user GUEST profile "cadsr_special_account"
/
alter user CADSRPASSWORDCHANGE profile "cadsr_special_account"
/
alter user CDEBROWSER profile "cadsr_special_account"
/
alter user CDECURATE profile "cadsr_special_account"
/
alter user FORMBUILDER profile "cadsr_special_account"
/
alter user FREESTYLESEARCH profile "cadsr_special_account"
/
alter user SENTINEL profile "cadsr_special_account"
/
alter user SBR profile "cadsr_special_account"
/
alter user SBREXT profile "cadsr_special_account"
/
alter user CADSRADMIN profile "cadsr_special_account"
/
alter user CADSR_API profile "cadsr_special_account"
/
alter user UMLLDR profile "cadsr_special_account"
/
alter user THE_SIW profile "cadsr_special_account"
/
alter user BLKLDR profile "cadsr_special_account"
/
alter user SITESCOPE profile "cadsr_special_account"
/