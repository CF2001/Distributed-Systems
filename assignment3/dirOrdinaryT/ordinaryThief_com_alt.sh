CODEBASE="file:///home/"$1"/HeistToTheMuseum/dirOrdinaryT/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     clientSide.main.ClientHeistToTheMuseumOrdinary localhost 22456