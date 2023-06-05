echo "Transfering data to the Master Thief node."
sshpass -f password ssh sd406@l040101-ws08.ua.pt 'mkdir -p HeistToTheMuseum'
sshpass -f password ssh sd406@l040101-ws08.ua.pt 'rm -rf HeistToTheMuseum/*'
sshpass -f password scp dirMasterT.zip sd406@l040101-ws08.ua.pt:HeistToTheMuseum

echo "Decompressing data sent to the Master Thief node."
sshpass -f password ssh sd406@l040101-ws08.ua.pt 'cd HeistToTheMuseum ; unzip -uq dirMasterT.zip'

echo "Executing program at the Master Thief node."
sshpass -f password ssh sd406@l040101-ws08.ua.pt 'cd HeistToTheMuseum/dirMasterT ; bash masterThief_com_d.sh'

echo "Server Master Thief shutdown."
