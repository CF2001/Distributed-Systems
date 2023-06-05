CODEBASE="file:///home/"$1"/HeistToTheMuseum/dirMuseum/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerHeistMuseumMuseum 22453 localhost 22456