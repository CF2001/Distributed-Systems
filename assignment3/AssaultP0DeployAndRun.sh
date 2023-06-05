echo "Transfering data to the Assault Party 0 node."
sshpass -f password ssh sd406@l040101-ws06.ua.pt 'mkdir -p HeistToTheMuseum'
sshpass -f password ssh sd406@l040101-ws06.ua.pt 'rm -rf HeistToTheMuseum/*'
sshpass -f password scp dirAssaultParty0.zip sd406@l040101-ws06.ua.pt:HeistToTheMuseum

echo "Decompressing data sent to the Assault Party 0 node."
sshpass -f password ssh sd406@l040101-ws06.ua.pt 'cd HeistToTheMuseum ; unzip -uq dirAssaultParty0.zip'

echo "Executing program at the server Assault Party 0."
sshpass -f password ssh sd406@l040101-ws06.ua.pt 'cd HeistToTheMuseum/dirAssaultParty0 ; bash assaultParty0_com_d.sh sd406'