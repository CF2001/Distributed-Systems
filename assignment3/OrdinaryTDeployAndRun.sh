echo "Transfering data to the Ordinary Thieves node."
sshpass -f password ssh sd406@l040101-ws09.ua.pt 'mkdir -p HeistToTheMuseum'
sshpass -f password ssh sd406@l040101-ws09.ua.pt 'rm -rf HeistToTheMuseum/*'
sshpass -f password scp dirOrdinaryT.zip sd406@l040101-ws09.ua.pt:HeistToTheMuseum

echo "Decompressing data sent to the Ordinary Thieves node."
sshpass -f password ssh sd406@l040101-ws09.ua.pt 'cd HeistToTheMuseum ; unzip -uq dirOrdinaryT.zip'

echo "Executing program at the Ordinary Thieves node."
sshpass -f password ssh sd406@l040101-ws09.ua.pt 'cd HeistToTheMuseum/dirOrdinaryT ; bash ordinaryThief_com_d.sh'

echo "Server Ordinary Thieves shutdown."
