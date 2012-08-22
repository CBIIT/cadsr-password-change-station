--run as sbrext

insert into sbrext.tool_options_view_ext (Tool_name, Property, Value) values('PasswordChangeStation', 'EMAIL.ADDR', 'warzeld@mail.nih.gov')
/
insert into sbrext.tool_options_view_ext (Tool_name, Property, Value) values('PasswordChangeStation', 'EMAIL.ADMIN.NAME', 'caDSR Alert Administrator')
/
insert into sbrext.tool_options_view_ext (Tool_name, Property, Value) values('PasswordChangeStation', 'EMAIL.ERROR', 'One or more errors occurred generating the password expiration notification emails.  Contact the Alert Administrator to determine the cause. You may attempt to Run the Alert Definition manually, however until the cause for the error is determined this may also fail.')
/
insert into sbrext.tool_options_view_ext (Tool_name, Property, Value) values('PasswordChangeStation', 'EMAIL.HOST', 'mailfwd.nih.gov')
/
insert into sbrext.tool_options_view_ext (Tool_name, Property, Value) values('PasswordChangeStation', 'EMAIL.PORT', '25')
/
insert into sbrext.tool_options_view_ext (Tool_name, Property, Value) values('PasswordChangeStation', 'EMAIL.INTRO', 'Your password is about to expire in ${daysLeft} days. Please login to Password Change Station or call NCI Helpdesk to change your password.')
/
insert into sbrext.tool_options_view_ext (Tool_name, Property, Value) values('PasswordChangeStation', 'EMAIL.SUBJECT', 'caDSR Password Expiration Notice (in ${daysLeft} day(s))')
/
