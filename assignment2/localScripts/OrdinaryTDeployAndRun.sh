echo "Transfering data to the Ordinary Thieves node."
cd /home/claudia/HeistToTheMuseum/dirOrdinaryT
java -cp "../genclass.jar:."  clientSide.main.ClientHeistToTheMuseumOrdinary 127.0.0.1 22451 127.0.0.1 22452 127.0.0.1 22454 127.0.0.1 22455 127.0.0.1 22453 127.0.0.1 22450
