--run as sbrext

update sbrext.tool_options_view_ext set value = 1000 where tool_name = 'caDSR' and property = 'LOCKOUT.THRESHOLD'
/
