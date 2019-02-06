#!/usr/bin/env python2
# -*- coding: utf-8 -*-

PORT = 8079

import BaseHTTPServer


class SingleNetRequestHandler(BaseHTTPServer.BaseHTTPRequestHandler):
    def __init__(self):
        self.route = self.path.strip('/')


if __name__ == '__main__':
    http_server = BaseHTTPServer.HTTPServer(('', PORT), SingleNetRequestHandler)
    try:
        http_server.serve_forever()
    except KeyboardInterrupt:
        pass
    http_server.server_close()
