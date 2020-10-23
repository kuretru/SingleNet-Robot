#!/usr/bin/env python3
# coding=utf-8

from http.server import BaseHTTPRequestHandler, HTTPServer
from urllib.parse import urlparse, parse_qs
import json
import subprocess
import io
import time

PORT = 8079  # 闪讯机器人API服务器监听端口
ACCESS_TOKEN = '123456'  # RESTful API通信密钥


def ping():
    now = time.strftime('%Y-%m-%d %H:%M:%S', time.localtime())
    return success_response(now)


def get_network_option(network_interface: str):
    username = run_subprocess(['uci', 'get', 'network.{}.username'.format(network_interface)])
    password = run_subprocess(['uci', 'get', 'network.{}.password'.format(network_interface)])
    return success_response({'username': username, 'password': password})


def set_network_option(network_interface: str, username: str, password: str):
    run_subprocess(['uci', 'set', 'network.{}.username={}'.format(network_interface, username)])
    run_subprocess(['uci', 'set', 'network.{}.password={}'.format(network_interface, password)])
    run_subprocess(['uci', 'commit', 'network.{}'.format(network_interface)])
    return success_response(network_interface)


def get_interface_status(network_interface: str):
    result = run_subprocess(['/sbin/ifstatus', network_interface])
    return success_response(json.loads(result))


def set_interface_up(network_interface: str):
    result = run_subprocess(['/sbin/ifdown', network_interface, '&&', '/sbin/ifup', network_interface])
    return success_response(json.loads(result))


def success_response(data):
    return build_response(100, 'success', data)


def build_response(code: int, message: str, data, status_code=200):
    body = {'code': code, 'message': message, 'data': data}
    return {'body': body, 'status_code': status_code}


def run_subprocess(args):
    result = subprocess.run(args, check=True, stdout=subprocess.PIPE).stdout
    return result.decode().replace('\n', '').strip()


class SingleNetRequestHandler(BaseHTTPRequestHandler):
    routes = []

    def ping(self):
        return ping()

    def get_network_option(self):
        params = parse_qs(urlparse(self.path).query)
        return get_network_option(params['interface'][0])

    def get_interface_status(self):
        params = parse_qs(urlparse(self.path).query)
        return get_interface_status(params['interface'][0])

    def _prepare_request(self, method: str):
        """预处理HTTP Request"""
        if 'Access-Token' not in self.headers:
            self._prepare_response(build_response(10200, 'user authentication failed', '请求头未携带Access-Token', 401))
        access_token = self.headers['Access-Token']
        if not self._valid_access_token(access_token):
            self._prepare_response(build_response(10200, 'user authentication failed', 'Access-Token错误', 401))
        for route in SingleNetRequestHandler.routes:
            path = urlparse(self.path).path
            if path == route['path'] and method == route['method']:
                self._handle_request(route['handler'])
        self._prepare_response(build_response(10400, 'user request error', '不存在该路由', 404))

    def _handle_request(self, handler):
        self._prepare_response(handler(self))

    def _prepare_response(self, data: dict):
        """封装HTTP Response Body"""
        self.send_response(data['status_code'])
        self._send_response(data['body'])

    def _send_response(self, body):
        """封装HTTP Response"""
        body = json.dumps(body, ensure_ascii=False)
        data = body.encode('UTF-8')
        self.send_header('Content-Length', str(len(data)))
        self.send_header('Content-Type', 'application/json')
        self.end_headers()
        self.wfile.write(data)

    def _valid_access_token(self, access_token: str):
        """验证Access-Token的合法性，暂时直接比对"""
        return ACCESS_TOKEN == access_token

    def do_method(self, method: str):
        self._prepare_request(method)

    def do_OPTIONS(self):
        self.do_method('OPTIONS')

    def do_GET(self):
        self.do_method('GET')

    def do_HEAD(self):
        self.do_method('HEAD')

    def do_POST(self):
        self.do_method('POST')

    def do_PUT(self):
        self.do_method('PUT')

    def do_DELETE(self):
        self.do_method('DELETE')

    def do_TRACE(self):
        self.do_method('TRACE')

    def do_CONNECT(self):
        self.do_method('CONNECT')


if __name__ == '__main__':
    SingleNetRequestHandler.routes = [
        {'path': '/api/ping', 'method': 'GET', 'handler': SingleNetRequestHandler.ping},
        {'path': '/api/network/option', 'method': 'GET', 'handler': SingleNetRequestHandler.get_network_option},
        {'path': '/api/network/status', 'method': 'GET', 'handler': SingleNetRequestHandler.get_interface_status}
    ]
    httpd = HTTPServer(('0.0.0.0', PORT), SingleNetRequestHandler)
    print('Starting SingleNet Robot RESTful API Server at port %d' % PORT)
    try:
        httpd.serve_forever()
    except KeyboardInterrupt:
        pass
    print('Stopping SingleNet Robot RESTful API Server')
    httpd.server_close()
