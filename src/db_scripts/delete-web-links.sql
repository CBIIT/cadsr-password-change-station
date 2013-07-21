/*L
  Copyright SAIC-F Inc.

  Distributed under the OSI-approved BSD 3-Clause License.
  See http://ncip.github.com/cadsr-password-change/LICENSE.txt for details.
L*/

--run as sbrext

delete from sbrext.tool_options_view_ext where Tool_name = 'PasswordChangeStation' and Property = 'HELP.ROOT'
/
delete from sbrext.tool_options_view_ext where Tool_name = 'PasswordChangeStation' and Property = 'LOGO.ROOT'
/