echo "Transfering data to the control and collection site node."
sshpass -f password ssh sd406@l040101-ws03.ua.pt 'mkdir -p HeistToTheMuseum'
sshpass -f password ssh sd406@l040101-ws03.ua.pt 'rm -rf HeistToTheMuseum/*'
sshpass -f password scp dirControlCollecSite.zip sd406@l040101-ws03.ua.pt:HeistToTheMuseum

echo "Decompressing data sent to the control and collection site node."
sshpass -f password ssh sd406@l040101-ws03.ua.pt 'cd HeistToTheMuseum ; unzip -uq dirControlCollecSite.zip'

echo "Executing program at the server control and collection site."
sshpass -f password ssh sd406@l040101-ws03.ua.pt 'cd HeistToTheMuseum/dirControlCollecSite ; java serverSide.main.ServerHeistMuseumControlSite 22452 l040101-ws01.ua.pt 22450'

echo "Server control and collection shutdown."
