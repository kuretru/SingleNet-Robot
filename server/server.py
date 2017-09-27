#!/usr/bin/env python2
#-*- coding:utf-8 -*-
#==================================================
# Python Required:  Python 2.7
# Description:      闪讯密码自动获取服务端(Python版)
# Author:           kuretru < kuretru@gmail.com >
# Github:           https://github.com/kuretru/SingleNet-Password
# Version:          0.2.170927
#==================================================

PORT = 8080
INTERFACE = 'wan'
SECRET = '123456'

import os, BaseHTTPServer, urlparse, json

class SxHandler(object):
    '''闪讯密码处理类'''

    def get(self, handler):
        cmd = "/sbin/uci get network.{0}.password".format(INTERFACE)
        output = os.popen(cmd)
        handler.send_content(output.read())

    def post(self, handler, postvars):
        pwd = str(json.loads(postvars['password'][0]))
        print "password:" + pwd
        sec = str(json.loads(postvars['secret'][0]))
        if(sec == SECRET):
            cmd = "/sbin/uci set network.{0}.password={1}".format(INTERFACE,pwd)
            output = os.popen(cmd)
            output.read()
            self.get(handler)
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
            length = int(self.headers.getheader('Content-Length'))
            postvars = urlparse.parse_qs(self.rfile.read(length),True)
            if(self.route == 'sx'):
                self.sx.post(self,postvars)
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
