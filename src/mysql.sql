
create table sms(
	id bigint primary key,
	msg_content varchar(150),
	recipient text,
	sendtime datetime);
	
create table  s_logsmssubmit
	(
	id bigint primary key,
	msgid varchar(20), #submit_resp的msg_id字段,长度为16
	pk_total int,  #默认为1 
	pk_number int,  # 默认为1
	msg_src varchar(6), #spid
	src_id varchar(21), #spnumber
	dest_id   varchar(21), #接收用户号码
	msg_fmt int, #默认15
	Msg_content  varchar(150), #消息内容
	feetype varchar(2), #计费类型
	feecode varchar(6), #计费费用
	service_id varchar(10), #长度为10，业务id
	linkid varchar(20),
	sendtime  datetime, #短信提交给ismg的时间
	sendresptime datetime, #接受到ismg提交消息回应的时间
	tomttime datetime, #短信提交到用户终端的时间YYMMDDHHMM，report中的submit_time字段
	errcode varchar(10), #submit_resp的result字段 
	errmsg varchar(100) #report消息的stat字段
	);

create table  s_logsmsdeliver
	(
	id bigint primary key,
	msgid varchar(20),
	dest_id varchar(21), #spnumber
	src_id  varchar(21), #用户号码
	tp_pid int,
	tp_udhi int,
	service_id  varchar(10),
	registered_delivery int, #默认为1
	msg_fmt int,
	msg_content  varchar(150),
	msg_length int,
	recvtime datetime,
	linkid varchar(20)
	)