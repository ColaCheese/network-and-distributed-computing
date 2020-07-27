// 创建register
java client/RMIClient server/RMIServer 1099 register Joe 123
register Joe1 123
register Joe2 123

register Joe          错误的命令，cmd长度不为3
register Joe 123      重名

// 创建meeting
java client/RMIClient server/RMIServer 1099 add Joe 123 Joe2 2019-12-10-10:10:00 2019-12-15-20:00:00 meeting1
add Joe 123 Joe1 2019-12-08-16:10:00 2019-12-09-20:00:00 meeting2

add Joe 123 Joe2 2019-12-08-16:10:00 2019-12-09-20:00:00 meeting3   和发起者的时间冲突
add Joe2 123 Joe 2019-12-08-16:10:00 2019-12-09-20:00:00 meeting4   和与会者的时间冲突
add Joe1 124 Joe2 2019-12-08-16:10:00 2019-12-09-20:00:00 meeting5  密码不匹配
add Joe1 123 Joe4 2019-12-08-16:10:00 2019-12-09-20:00:00 meeting6  与会者不存在

// 查询meeting
query Joe 123 2019-12-01-16:10:00 2019-12-20-20:00:00

query Joe 124 2019-12-01-16:10:00 2019-12-20-20:00:00  密码不匹配
query Joe 123 2019-12-19-16:10:00 2019-12-20-20:00:00  无会议

// 删除meeting
delete Joe 123 1

delete Joe1 123 2    不是他创建的
delete Joe1 123 10   无这个会议

query Joe 123 2019-12-01-16:10:00 2019-12-20-20:00:00

// 清除meeting
clear Joe 123

query Joe 123 2019-12-08-16:10:00 2019-12-15-20:00:00

