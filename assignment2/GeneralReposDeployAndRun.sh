echo "Transfering data to the general repository node."
sshpass -f password ssh sd406@l040101-ws01.ua.pt 'mkdir -p HeistToTheMuseum'
sshpass -f password ssh sd406@l040101-ws01.ua.pt 'rm -rf HeistToTheMuseum/*'
sshpass -f password scp dirGeneralRepos.zip sd406@l040101-ws01.ua.pt:HeistToTheMuseum

echo "Decompressing data sent to the general repository node."
sshpass -f password ssh sd406@l040101-ws01.ua.pt 'cd HeistToTheMuseum ; unzip -uq dirGeneralRepos.zip'

echo "Executing program at the server general repository."
sshpass -f password ssh sd406@l040101-ws01.ua.pt 'cd HeistToTheMuseum/dirGeneralRepos ; java serverSide.main.ServerHeistMuseumGeneralRepos 22450'

echo "Server General repository shutdown."
sshpass -f password ssh sd406@l040101-ws01.ua.pt 'cd HeistToTheMuseum/dirGeneralRepos ; less log.txt'

