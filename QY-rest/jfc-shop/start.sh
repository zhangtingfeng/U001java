name=jfc-shop
echo "开始关闭jfc-shop服务"
kill -9 `ps -ef | grep ${name}-0.0.1-SNAPSHOT.jar |grep -v grep| awk '{print $2}'`
echo "关闭服务结束..."
echo "jfc-shop"
nohup java -jar ./${name}-0.0.1-SNAPSHOT.jar --spring.profiles.active=test>./log.out 2>&1 &
tail -f ./log.out
echo "已启动jfc-shop服务..."
