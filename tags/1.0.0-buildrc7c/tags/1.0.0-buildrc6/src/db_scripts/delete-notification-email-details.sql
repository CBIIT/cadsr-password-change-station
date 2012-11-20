--run as sbrext

delete from sbrext.tool_options_view_ext where Tool_name = 'PasswordChangeStation' and Property like 'EMAIL.%'
/