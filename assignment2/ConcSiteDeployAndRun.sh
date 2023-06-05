echo "Transfering data to the concentration site node."
sshpass -f password ssh sd406@l040101-ws02.ua.pt 'mkdir -p HeistToTheMuseum'
sshpass -f password ssh sd406@l040101-ws02.ua.pt 'rm -rf HeistToTheMuseum/*'
sshpass -f password scp dirConcSite.zip sd406@l040101-ws02.ua.pt:HeistToTheMuseum

echo "Decompressing data sent to the concentration site node."
sshpass -f password ssh sd406@l040101-ws02.ua.pt 'cd HeistToTheMuseum ; unzip -uq dirConcSite.zip'

echo "Executing program at the server concentration site."
sshpass -f password ssh sd406@l040101-ws02.ua.pt 'cd HeistToTheMuseum/dirConcSite ; java serverSide.main.ServerHeistMuseumConcSite 22451 l040101-ws01.ua.pt 22450'

echo "Server concentration site shutdown."

