/*L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-password-change/LICENSE.txt for details.
L*/

--run as sbrext

insert into sbrext.tool_options_view_ext (Tool_name, Property, Value) values('PasswordChangeStation', 'HELP.ROOT', 'https://wiki.nci.nih.gov/x/vADgB')
/
insert into sbrext.tool_options_view_ext (Tool_name, Property, Value) values('PasswordChangeStation', 'LOGO.ROOT', 'https://cabig.nci.nih.gov')
/