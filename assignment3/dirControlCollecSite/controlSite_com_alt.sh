CODEBASE="file:///home/"$1"/HeistToTheMuseum/dirControlCollecSite/"
java -Djava.rmi.server.codebase=$CODEBASE\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     serverSide.main.ServerHeistMuseumControlSite 22452 localhost 22456