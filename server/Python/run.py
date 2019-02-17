#!/usr/bin/env python
# -*- coding: utf-8 -*-

import BaseHTTPServer
import subprocess

PORT = 8079  # 闪讯机器人API服务器监听端口
INTERFACE = 'wan'  # OpenWrt下外网接口的名称
SECRET = '123456'  # API服务器通信密钥


def get_username():
    result = subprocess.check_output(['uci', 'get', 'network.{}.username'.format(INTERFACE)])
    return result


def get_password():
    result = subprocess.check_output(['uci', 'get', 'network.{}.password'.format(INTERFACE)])
    return result


def set_username(username):
    subprocess.call(['uci', 'set', 'network.{}.username={}'.format(INTERFACE, username)])
    return get_username()


def set_password(password):
    subprocess.call(['uci', 'set', 'network.{}.password={}'.format(INTERFACE, password)])
    return get_password()


class SingleNetRequestHandler(BaseHTTPServer.BaseHTTPRequestHandler):

    def do_GET(self):
        self.handle_method('GET')

    def do_POST(self):
        self.handle_method('POST')

    def do_PUT(self):
        self.handle_method('PUT')

    def do_DELETE(self):
        self.handle_method('DELETE')

    def handle_method(self, method):
        route = self.path.strip('/')
        print(route)
        print(get_username())
        self.send_error(404)


if __name__ == '__main__':
    http_server = BaseHTTPServer.HTTPServer(('', PORT), SingleNetRequestHandler)
    print('Starting SingleNet Robot API Server at port %d' % PORT)
    try:
        http_server.serve_forever()
    except KeyboardInterrupt:
        pass
    print("Stopping SingleNet Robot API Server")
    http_server.server_close()
