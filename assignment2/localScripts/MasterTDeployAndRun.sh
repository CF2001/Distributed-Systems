echo "Transfering data to the Master Thief node."
cd /home/claudia/HeistToTheMuseum/dirMasterT
java -cp "../genclass.jar:."  clientSide.main.ClientHeistToTheMuseumMaster 127.0.0.1 22451 127.0.0.1 22452 127.0.0.1 22454 127.0.0.1 22455 127.0.0.1 22450
