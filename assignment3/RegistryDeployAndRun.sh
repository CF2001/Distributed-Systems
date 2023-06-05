echo "Transfering data to the registry node."
sshpass -f password ssh sd406@l040101-ws10.ua.pt 'mkdir -p HeistToTheMuseum'
sshpass -f password scp dirRegistry.zip sd406@l040101-ws10.ua.pt:HeistToTheMuseum

echo "Decompressing data sent to the registry node."
sshpass -f password ssh sd406@l040101-ws10.ua.pt 'cd HeistToTheMuseum ; unzip -uq dirRegistry.zip'

echo "Executing program at the registry node."
sshpass -f password ssh sd406@l040101-ws10.ua.pt 'cd HeistToTheMuseum/dirRegistry ; ./registry_com_d.sh sd406'

echo "Registry node shutdown."