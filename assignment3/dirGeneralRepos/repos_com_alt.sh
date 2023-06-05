CODEBASE="file:///home/"$1"/HeistToTheMuseum/dirGeneralRepos/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerHeistMuseumGeneralRepos 22450 localhost 22456