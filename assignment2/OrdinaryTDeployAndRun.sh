echo "Transfering data to the Ordinary Thieves node."
sshpass -f password ssh sd406@l040101-ws09.ua.pt 'mkdir -p HeistToTheMuseum'
sshpass -f password ssh sd406@l040101-ws09.ua.pt 'rm -rf HeistToTheMuseum/*'
sshpass -f password scp dirOrdinaryT.zip sd406@l040101-ws09.ua.pt:HeistToTheMuseum

echo "Decompressing data sent to the Ordinary Thieves node."
sshpass -f password ssh sd406@l040101-ws09.ua.pt 'cd HeistToTheMuseum ; unzip -uq dirOrdinaryT.zip'

echo "Executing program at the Ordinary Thieves node."
sshpass -f password ssh sd406@l040101-ws09.ua.pt 'cd HeistToTheMuseum/dirOrdinaryT ; java clientSide.main.ClientHeistToTheMuseumOrdinary l040101-ws02.ua.pt 22451 l040101-ws03.ua.pt 22452 l040101-ws06.ua.pt 22454 l040101-ws07.ua.pt 22455 l040101-ws10.ua.pt 22453 l040101-ws01.ua.pt 22450'

echo "Server Ordinary Thieves shutdown."
