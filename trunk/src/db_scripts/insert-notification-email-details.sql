--run as sbrext

insert into sbrext.tool_options_view_ext (Tool_name, Property, Value) values('PasswordChangeStation', 'EMAIL.NOTIFY_TYPE', '14,7,4')
/
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
insert into sbrext.tool_options_view_ext (Tool_name, Property, Value) values('PasswordChangeStation', 'EMAIL.INTRO', 'Your password of the account ${userid} is about to expire on ${expiryDate}.  To change your password, you can either login to the Password Change Station by visiting https://cadsrpasswordchange.nci.nih.gov, contact the NCI Helpdesk at ncicb@pop.nci.nih.gov or toll free phone number: 888-478-4423.')
/
insert into sbrext.tool_options_view_ext (Tool_name, Property, Value) values('PasswordChangeStation', 'EMAIL.SUBJECT', 'Reminder: caDSR Password Expiration')
/
commit
/
