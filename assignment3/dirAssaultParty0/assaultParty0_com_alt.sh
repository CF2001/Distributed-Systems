CODEBASE="file:///home/"$1"/HeistToTheMuseum/dirAssaultParty0/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerHeistMuseumAssaultP0 22454 localhost 22456