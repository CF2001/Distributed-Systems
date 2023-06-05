echo "Executing the GeneraRepos node."
cd /home/claudia/HeistToTheMuseum/dirGeneralRepos
java -cp "../genclass.jar:."  serverSide.main.ServerHeistMuseumGeneralRepos 22450
echo "Server shutdown."
