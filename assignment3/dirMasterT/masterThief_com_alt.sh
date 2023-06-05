CODEBASE="file:///home/"$1"/HeistToTheMuseum/dirMasterT/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     clientSide.main.ClientHeistToTheMuseumMaster localhost 22456