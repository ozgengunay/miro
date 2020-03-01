#!/bin/bash -x
echo "Stopping miro process"
APP_ID=$(pgrep -f miro)
if [ -n "${APP_ID}" ]; then
    echo "Stopping instance ${APP_ID}"
    kill ${APP_ID}
else
    echo "miro process not found"    
fi
