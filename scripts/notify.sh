#!/bin/bash

echo "sleeping and then trying to send deploy message from the service"
ret=0
i=1
while [ ${i} -le 10 -a "${ret}" -ne "200" ]
do
	echo "waiting for 15 secs"
	sleep 15
	ret=`curl -s -o /dev/null -w "%{http_code}" -XPOST http://localhost:8101/message/620393195/deployed_new_version_pw_manager`
	echo "return code: ${ret}"
	i=${i}+1
done