--run as sbrext

insert into sbrext.tool_options_view_ext (Tool_name, Property, Value) values('PasswordChangeStation', 'EMAIL.SUBJECT', 'caDSR Password Expiring Notice')
/
insert into sbrext.tool_options_view_ext (Tool_name, Property, Value) values('PasswordChangeStation', 'EMAIL.INTRO', 'Your password is expiring soon. Please login to Password Change Station or call NCI Helpdesk to change your password.')
/