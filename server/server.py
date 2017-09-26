#!/usr/bin/env python2
#-*- coding:utf-8 -*-
#==================================================
# Python Required:  Python 2.7
# Description:      闪讯密码自动获取服务端(Python版)
# Author:           kuretru < kuretru@gmail.com >
# Github:           https://github.com/kuretru/SingleNet-Password
# Version:          0.1.170926
#==================================================

PORT = 8080
INTERFACE = 'wan'
SECRET = '123456'

import os, BaseHTTPServer

class SxHandler(object):
    '''闪讯密码处理类'''

    def get(self, handler):
        cmd = "/sbin/uci get network.{0}.password".format(INTERFACE)
        output = os.popen(cmd)
        handler.send_content(output.read())

    def post(self, handler, password, sec):
        if(sec == SECRET):
            cmd = "/sbin/uci set network.{0}.password {1}".format(INTERFACE,password)
            output = os.popen(cmd)
            handler.send_content(output.read())
        else:
            handler.handle_error("The secret do not match")

class RequestHandler(BaseHTTPServer.BaseHTTPRequestHandler):
    Error_Page = """\
        <html>
        <body>
        <h1>Error accessing {path}</h1>
        <p>{msg}</p>
        </body>
        </html>
        """

    def init(self):
        self.sx = SxHandler()
        self.route = self.path.strip('/')

    def do_GET(self):
        self.init()
        try:
            if(self.route == 'sx'):
                self.sx.get(self)
        except Exception as msg:
            self.handle_error(msg)

    def do_POST(self):
        self.init()
        try:
            if(self.route == 'sx'):
                self.sx.post(self)
        except Exception as msg:
            self.handle_error(msg)

    def handle_error(self, msg):
        content = self.Error_Page.format(path=self.path, msg=msg)
        self.send_content(content, 404)

    def send_content(self, content, status=200):
        self.send_response(status)
        self.send_header("Content-type", "text/html")
        self.send_header("Content-Length", str(len(content)))
        self.end_headers()
        self.wfile.write(content)

#-------------------------------------------------------------------------------

if __name__ == '__main__':
    serverAddress = ('', PORT)
    server = BaseHTTPServer.HTTPServer(serverAddress, RequestHandler)
    server.serve_forever()
