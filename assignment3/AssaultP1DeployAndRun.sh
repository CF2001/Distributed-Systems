echo "Transfering data to the Assault Party 1 node."
sshpass -f password ssh sd406@l040101-ws07.ua.pt 'mkdir -p HeistToTheMuseum'
sshpass -f password ssh sd406@l040101-ws07.ua.pt 'rm -rf HeistToTheMuseum/*'
sshpass -f password scp dirAssaultParty1.zip sd406@l040101-ws07.ua.pt:HeistToTheMuseum

echo "Decompressing data sent to the Assault Party 1 node."
sshpass -f password ssh sd406@l040101-ws07.ua.pt 'cd HeistToTheMuseum ; unzip -uq dirAssaultParty1.zip'

echo "Executing program at the server Assault Party 1."
sshpass -f password ssh sd406@l040101-ws07.ua.pt 'cd HeistToTheMuseum/dirAssaultParty1 ; bash assaultParty1_com_d.sh sd406'