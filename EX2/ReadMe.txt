1. 不用GET/PUT方法的时候

客户端提示：Bad request! Please use GET or PUT

2. PUT报文不完整的时候（不为三个分隔字符串）

客户端提示：The message is incomplete!
客户端接受报文头部：HTTP/1.1 400 Bad Request

3. PUT报文指示的文件不存在时（服务器端也不存在）

客户端提示：File do not exist!
服务器端：创建文件但不写入

创建成功客户端接受报文头部：
HTTP/1.1 201 Created
Server: LyyHttpServer/1.1
Content-Location: /xxxx.xxx

创建失败客户端接受报文头部：
HTTP/1.1 500 Internal Server Error

4. PUT报文指示的文件不存在时（服务器端存在）

客户端提示：File do not exist!
服务器端：不创建文件
客户端接受报文头部：
HTTP/1.1 204 No Content
Server: LyyHttpServer/1.1
Content-Location: /xxxx.xxx

5. PUT报文正确（服务器端不存在对应文件）

服务器端：创建文件并写入

创建成功客户端接受报文头部：
HTTP/1.1 201 Created
Server: LyyHttpServer/1.1
Content-type: image/jpeg
Content-length: 265650
Content-Location: /xxxx.xxx

创建失败客户端接受报文头部：
HTTP/1.1 500 Internal Server Error

6. PUT报文正确（服务器端存在对应文件）

服务器端：更新文件
客户端接受报文头部：
HTTP/1.1 204 No Content
Server: LyyHttpServer/1.1
Content-type: image/jpeg
Content-length: 265650
Content-Location: /xxxx.xxx

7. GET报文不正确

客户端接受报文头部：HTTP/1.1 400 Bad Request

8. GET报文指示的文件不存在

客户端接受报文头部：HTTP/1.1 404 NOT FOUND

9. GET报文正确

客户端接受报文头部：
HTTP/1.1 200 OK
Server: LyyHttpServer/1.1
Content-type: image/jpeg
Content-length: 265650
Content-Location: /xxxx.xxx

10. 对带有网络图片和本地图片的HTML页面在客户端GET请求的时候能将图片转为base64保存