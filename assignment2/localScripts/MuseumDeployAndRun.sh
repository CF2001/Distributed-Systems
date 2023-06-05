echo "Executing the Museum node."
cd /home/claudia/HeistToTheMuseum/dirMuseum
java -cp "../genclass.jar:."  serverSide.main.ServerHeistMuseumMuseum 22453 127.0.0.1 22450
echo "Server shutdown."
