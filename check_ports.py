import socket
ports=[8081,8082,8084,8090,8091,8092,8093,8761]
for p in ports:
    s=socket.socket()
    try:
        s.settimeout(0.5)
        s.connect(('127.0.0.1', p))
        print(p, 'open')
    except Exception:
        print(p, 'closed')
    finally:
        s.close()
