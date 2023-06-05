echo "Transfering data to the museum node."
sshpass -f password ssh sd406@l040101-ws10.ua.pt 'mkdir -p HeistToTheMuseum'
sshpass -f password ssh sd406@l040101-ws10.ua.pt 'rm -rf HeistToTheMuseum/*'
sshpass -f password scp dirMuseum.zip sd406@l040101-ws10.ua.pt:HeistToTheMuseum

echo "Decompressing data sent to the museum node."
sshpass -f password ssh sd406@l040101-ws10.ua.pt 'cd HeistToTheMuseum ; unzip -uq dirMuseum.zip'

echo "Executing program at the server museum."
sshpass -f password ssh sd406@l040101-ws10.ua.pt 'cd HeistToTheMuseum/dirMuseum ; java serverSide.main.ServerHeistMuseumMuseum 22453 l040101-ws01.ua.pt 22450'

echo "Server museum shutdown."
