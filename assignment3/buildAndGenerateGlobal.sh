echo "Compiling source code."
javac -source 8 -target 8 -cp ../genclass.jar */*.java */*/*.java

echo "Distributing intermediate code to the different execution environments."

echo "  RMI registry"
rm -rf dirRMIRegistry/interfaces
mkdir -p dirRMIRegistry/interfaces
cp interfaces/*.class dirRMIRegistry/interfaces

echo "  Register Remote Objects"
rm -rf dirRegistry/serverSide dirRegistry/interfaces
mkdir -p dirRegistry/serverSide dirRegistry/serverSide/main dirRegistry/serverSide/objects dirRegistry/interfaces
cp serverSide/main/ServerRegisterRemoteObject.class dirRegistry/serverSide/main
cp serverSide/objects/RegisterRemoteObject.class dirRegistry/serverSide/objects
cp interfaces/Register.class dirRegistry/interfaces


echo "  General Repository of Information"
rm -rf dirGeneralRepos/serverSide dirGeneralRepos/clientSide dirGeneralRepos/interfaces
mkdir -p dirGeneralRepos/serverSide dirGeneralRepos/serverSide/main dirGeneralRepos/serverSide/objects dirGeneralRepos/interfaces \
         dirGeneralRepos/clientSide dirGeneralRepos/clientSide/entities
cp serverSide/main/SimulPar.class serverSide/main/ServerHeistMuseumGeneralRepos.class dirGeneralRepos/serverSide/main
cp serverSide/objects/GeneralRepository.class dirGeneralRepos/serverSide/objects
cp interfaces/Register.class interfaces/GeneralReposInterface.class dirGeneralRepos/interfaces
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class dirGeneralRepos/clientSide/entities


echo "  Concentration Site"
rm -rf dirConcSite/serverSide dirConcSite/clientSide dirConcSite/interfaces dirConcSite/commInfra
mkdir -p dirConcSite/serverSide dirConcSite/serverSide/main dirConcSite/serverSide/objects dirConcSite/interfaces \
         dirConcSite/clientSide dirConcSite/clientSide/entities dirConcSite/commInfra

cp serverSide/main/SimulPar.class serverSide/main/ServerHeistMuseumConcSite.class dirConcSite/serverSide/main
cp serverSide/objects/ConcentrationSite.class dirConcSite/serverSide/objects
cp interfaces/*.class dirConcSite/interfaces
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class dirConcSite/clientSide/entities
cp commInfra/*.class dirConcSite/commInfra


echo "  Control and Collection Site"
rm -rf dirControlCollecSite/serverSide dirControlCollecSite/clientSide dirControlCollecSite/interfaces dirControlCollecSite/commInfra
mkdir -p dirControlCollecSite/serverSide dirControlCollecSite/serverSide/main dirControlCollecSite/serverSide/objects dirControlCollecSite/interfaces \
         dirControlCollecSite/clientSide dirControlCollecSite/clientSide/entities dirControlCollecSite/commInfra

cp serverSide/main/SimulPar.class serverSide/main/ServerHeistMuseumControlSite.class dirControlCollecSite/serverSide/main
cp serverSide/objects/ControlCollectionSite.class dirControlCollecSite/serverSide/objects
cp interfaces/*.class dirControlCollecSite/interfaces
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class dirControlCollecSite/clientSide/entities
cp commInfra/*.class dirControlCollecSite/commInfra

echo "  Museum"
rm -rf dirMuseum/serverSide dirMuseum/clientSide dirMuseum/interfaces dirMuseum/commInfra
mkdir -p dirMuseum/serverSide dirMuseum/serverSide/main dirMuseum/serverSide/objects dirMuseum/interfaces \
         dirMuseum/clientSide dirMuseum/clientSide/entities dirMuseum/commInfra

cp serverSide/main/SimulPar.class serverSide/main/ServerHeistMuseumMuseum.class dirMuseum/serverSide/main
cp serverSide/objects/Museum.class dirMuseum/serverSide/objects
cp interfaces/*.class dirMuseum/interfaces
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class dirMuseum/clientSide/entities
cp commInfra/*.class dirMuseum/commInfra


echo "  Assault Party 0"
rm -rf dirAssaultParty0/serverSide dirAssaultParty0/clientSide dirAssaultParty0/interfaces dirAssaultParty0/commInfra
mkdir -p dirAssaultParty0/serverSide dirAssaultParty0/serverSide/main dirAssaultParty0/serverSide/objects dirAssaultParty0/interfaces \
         dirAssaultParty0/clientSide dirAssaultParty0/clientSide/entities dirAssaultParty0/commInfra

cp serverSide/main/SimulPar.class serverSide/main/ServerHeistMuseumAssaultP0.class dirAssaultParty0/serverSide/main
cp serverSide/objects/AssaultParty.class dirAssaultParty0/serverSide/objects
cp interfaces/*.class dirAssaultParty0/interfaces
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class dirAssaultParty0/clientSide/entities
cp commInfra/*.class dirAssaultParty0/commInfra


echo "  Assault Party 1"
rm -rf dirAssaultParty1/serverSide dirAssaultParty1/clientSide dirAssaultParty1/interfaces dirAssaultParty1/commInfra
mkdir -p dirAssaultParty1/serverSide dirAssaultParty1/serverSide/main dirAssaultParty1/serverSide/objects dirAssaultParty1/interfaces \
         dirAssaultParty1/clientSide dirAssaultParty1/clientSide/entities dirAssaultParty1/commInfra

cp serverSide/main/SimulPar.class serverSide/main/ServerHeistMuseumAssaultP1.class dirAssaultParty1/serverSide/main
cp serverSide/objects/AssaultParty.class dirAssaultParty1/serverSide/objects
cp interfaces/*.class dirAssaultParty1/interfaces
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class dirAssaultParty1/clientSide/entities
cp commInfra/*.class dirAssaultParty1/commInfra


echo "  Master Thief"
rm -rf dirMasterT/serverSide dirMasterT/clientSide dirMasterT/interfaces
mkdir -p dirMasterT/serverSide dirMasterT/serverSide/main dirMasterT/clientSide dirMasterT/clientSide/main dirMasterT/clientSide/entities \
         dirMasterT/interfaces

cp serverSide/main/SimulPar.class dirMasterT/serverSide/main
cp clientSide/main/ClientHeistToTheMuseumMaster.class dirMasterT/clientSide/main
cp clientSide/entities/MasterThief.class clientSide/entities/MasterThiefStates.class dirMasterT/clientSide/entities
cp interfaces/ConcSiteInterface.class interfaces/ControlCollecSiteInterface.class interfaces/AssaultPartyInterface.class \
    interfaces/GeneralReposInterface.class interfaces/ReturnInt.class interfaces/ReturnBoolean.class interfaces/ReturnArrayInt.class \
    interfaces/ReturnArrayBoolean.class dirMasterT/interfaces

echo "  Ordinary Thieves"
rm -rf dirOrdinaryT/serverSide dirOrdinaryT/clientSide dirOrdinaryT/interfaces
mkdir -p dirOrdinaryT/serverSide dirOrdinaryT/serverSide/main dirOrdinaryT/clientSide dirOrdinaryT/clientSide/main dirOrdinaryT/clientSide/entities \
         dirOrdinaryT/interfaces

cp serverSide/main/SimulPar.class dirOrdinaryT/serverSide/main
cp clientSide/main/ClientHeistToTheMuseumOrdinary.class dirOrdinaryT/clientSide/main
cp clientSide/entities/OrdinaryThief.class clientSide/entities/OrdinaryThiefStates.class dirOrdinaryT/clientSide/entities
cp interfaces/ConcSiteInterface.class interfaces/ControlCollecSiteInterface.class interfaces/AssaultPartyInterface.class interfaces/MuseumInterface.class \
    interfaces/GeneralReposInterface.class interfaces/ReturnInt.class interfaces/ReturnBoolean.class interfaces/ReturnArrayInt.class \
    interfaces/ReturnArrayBoolean.class dirOrdinaryT/interfaces

echo "Compressing execution environments."

echo "  RMI registry"
rm -f  dirRMIRegistry.zip
zip -rq dirRMIRegistry.zip dirRMIRegistry

echo "  Register Remote Objects"
rm -f  dirRegistry.zip
zip -rq dirRegistry.zip dirRegistry

echo "  General Repository of Information"
rm -f  dirGeneralRepos.zip
zip -rq dirGeneralRepos.zip dirGeneralRepos

echo "  Concentration Site"
rm -f  dirConcSite.zip
zip -rq dirConcSite.zip dirConcSite

echo "  Control and Collection Site"
rm -f  dirControlCollecSite.zip
zip -rq dirControlCollecSite.zip dirControlCollecSite

echo "  Museum"
rm -f  dirMuseum.zip
zip -rq dirMuseum.zip dirMuseum

echo "  Assault Party 0"
rm -f  dirAssaultParty0.zip
zip -rq dirAssaultParty0.zip dirAssaultParty0

echo "  Assault Party 1"
rm -f  dirAssaultParty1.zip
zip -rq dirAssaultParty1.zip dirAssaultParty1

echo "  Master Thief"
rm -f  dirMasterT.zip
zip -rq dirMasterT.zip dirMasterT

echo "  Ordinary Thieves"
rm -f  dirOrdinaryT.zip
zip -rq dirOrdinaryT.zip dirOrdinaryT