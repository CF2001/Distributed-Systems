echo "Transfering data to the Master Thief node."
sshpass -f password ssh sd406@l040101-ws08.ua.pt 'mkdir -p HeistToTheMuseum'
sshpass -f password ssh sd406@l040101-ws08.ua.pt 'rm -rf HeistToTheMuseum/*'
sshpass -f password scp dirMasterT.zip sd406@l040101-ws08.ua.pt:HeistToTheMuseum

echo "Decompressing data sent to the Master Thief node."
sshpass -f password ssh sd406@l040101-ws08.ua.pt 'cd HeistToTheMuseum ; unzip -uq dirMasterT.zip'

echo "Executing program at the Master Thief node."
sshpass -f password ssh sd406@l040101-ws08.ua.pt 'cd HeistToTheMuseum/dirMasterT ; java clientSide.main.ClientHeistToTheMuseumMaster l040101-ws02.ua.pt 22451 l040101-ws03.ua.pt 22452 l040101-ws06.ua.pt 22454 l040101-ws07.ua.pt 22455 l040101-ws01.ua.pt 22450'

echo "Server Master Thief shutdown."
