echo "Executing the Control and Collection Site node."
cd /home/claudia/HeistToTheMuseum/dirControlCollecSite
java -cp "../genclass.jar:."  serverSide.main.ServerHeistMuseumControlSite 22452 127.0.0.1 22450
echo "Server shutdown."
