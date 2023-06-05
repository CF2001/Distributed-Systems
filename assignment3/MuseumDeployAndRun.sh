echo "Transfering data to the museum node."
sshpass -f password ssh sd406@l040101-ws05.ua.pt 'mkdir -p HeistToTheMuseum'
sshpass -f password ssh sd406@l040101-ws05.ua.pt 'rm -rf HeistToTheMuseum/*'
sshpass -f password scp dirMuseum.zip sd406@l040101-ws05.ua.pt:HeistToTheMuseum

echo "Decompressing data sent to the museum node."
sshpass -f password ssh sd406@l040101-ws05.ua.pt 'cd HeistToTheMuseum ; unzip -uq dirMuseum.zip'

echo "Executing program at the server museum."
sshpass -f password ssh sd406@l040101-ws05.ua.pt 'cd HeistToTheMuseum/dirMuseum ; bash museum_com_d.sh sd406 '

echo "Server museum shutdown."

