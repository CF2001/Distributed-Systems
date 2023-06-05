CODEBASE="file:///home/"$1"/HeistToTheMuseum/dirAssaultParty1/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerHeistMuseumAssaultP1 22455 localhost 22456