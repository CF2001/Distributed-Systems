echo "Executing the Concentration Site node."
cd /home/claudia/HeistToTheMuseum/dirConcSite
java -cp "../genclass.jar:."  serverSide.main.ServerHeistMuseumConcSite 22451 127.0.0.1 22450
echo "Server shutdown."
