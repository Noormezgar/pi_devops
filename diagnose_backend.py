import socket
import urllib.request
import urllib.error

ports = [8761, 8082, 8081, 8086, 8088, 8091, 8092, 8094]
print('--- Port test ---')
for p in ports:
    s = socket.socket()
    s.settimeout(1)
    try:
        s.connect(('127.0.0.1', p))
        print(f'{p} -> open')
    except Exception as e:
        print(f'{p} -> closed ({e})')
    finally:
        s.close()

for url in ['http://127.0.0.1:8082/actuator/health', 'http://127.0.0.1:8761/eureka/apps']:
    print('---', url, '---')
    try:
        with urllib.request.urlopen(url, timeout=5) as r:
            print('status', r.status)
            data = r.read(500).decode(errors='replace')
            print(data.replace('\n', ' ')[:300])
    except urllib.error.HTTPError as e:
        print('HTTPError', e.code, e.reason)
        try:
            print(e.read(500).decode(errors='replace').replace('\n', ' '))
        except Exception:
            pass
    except Exception as e:
        print('Error', e)
