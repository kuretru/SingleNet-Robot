#!/usr/bin/env python
# -*- coding: utf-8 -*-

import BaseHTTPServer

PORT = 8079  # 闪讯机器人API服务器监听端口
INTERFACE = 'wan'  # OpenWrt下外网接口的名称
SECRET = '123456'  # API服务器通信密钥


class SingleNetRequestHandler(BaseHTTPServer.BaseHTTPRequestHandler):
    def __init__(self):
        self.route = self.path.strip('/')


if __name__ == '__main__':
    http_server = BaseHTTPServer.HTTPServer(('', PORT), SingleNetRequestHandler)
    print('Starting SingleNet Robot API Server at port %d' % PORT)
    try:
        http_server.serve_forever()
    except KeyboardInterrupt:
        pass
    print("Stopping SingleNet Robot API Server")
    http_server.server_close()
