CODEBASE="file:///home/"$1"/HeistToTheMuseum/dirConcSite/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerHeistMuseumConcSite 22451 localhost 22456