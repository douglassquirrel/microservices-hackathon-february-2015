# ArenaClock
# will emit a tick every period
# the length of the period is configurable

import time
import requests
import json

length = 1000 # in milliseconds

tick = 0
while 1:
    period = length/1000
    time.sleep(period)
    # Publish the next tick
    response = requests.post('http://52.16.7.112:8000/topics/ArenaClock/facts',
                             json.dumps({'tick': tick, 'what': 'NextTurn'}))
    print(tick)
    tick = tick + 1
end
