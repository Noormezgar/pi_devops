import json
import urllib.request
import urllib.error

url = 'http://127.0.0.1:8082/api/partners'
data = json.dumps({
    'name': 'Test Partner',
    'contactEmail': 'testpartner@example.com',
    'contactPhone': '+123456789'
}).encode('utf-8')
req = urllib.request.Request(url, data=data, headers={'Content-Type': 'application/json'}, method='POST')
try:
    with urllib.request.urlopen(req, timeout=10) as r:
        print('status', r.status)
        print(r.read(500).decode(errors='replace'))
except urllib.error.HTTPError as e:
    print('HTTP', e.code, e.reason)
    print(e.read(500).decode(errors='replace'))
except Exception as e:
    print('ERROR', e)
