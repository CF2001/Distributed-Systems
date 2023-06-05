echo "Transfering data to the RMIregistry node."
sshpass -f password ssh sd406@l040101-ws10.ua.pt 'mkdir -p HeistToTheMuseum'
sshpass -f password ssh sd406@l040101-ws10.ua.pt 'rm -rf HeistToTheMuseum/*'
sshpass -f password ssh sd406@l040101-ws10.ua.pt 'mkdir -p Public/classes/interfaces'
sshpass -f password ssh sd406@l040101-ws10.ua.pt 'rm -rf Public/classes/interfaces/*'
sshpass -f password scp dirRMIRegistry.zip sd406@l040101-ws10.ua.pt:HeistToTheMuseum

echo "Decompressing data sent to the RMIregistry node."
sshpass -f password ssh sd406@l040101-ws10.ua.pt 'cd HeistToTheMuseum ; unzip -uq dirRMIRegistry.zip'
sshpass -f password ssh sd406@l040101-ws10.ua.pt 'cd HeistToTheMuseum/dirRMIRegistry ; cp interfaces/*.class /home/sd406/Public/classes/interfaces ; 
cp set_rmiregistry_d.sh /home/sd406'

echo "Executing program at the RMIregistry node."
sshpass -f password ssh sd406@l040101-ws10.ua.pt './set_rmiregistry_d.sh sd406 22456'

echo "RMIregistry node shutdown."