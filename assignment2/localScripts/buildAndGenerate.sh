echo "Compiling source code."
javac -cp ../genclass.jar */*.java */*/*.java

echo "Distributing intermediate code to the different execution environments."

echo "  General Repository of Information"
rm -rf dirGeneralRepos
mkdir -p dirGeneralRepos dirGeneralRepos/serverSide dirGeneralRepos/serverSide/main dirGeneralRepos/serverSide/entities dirGeneralRepos/serverSide/sharedRegions \
         dirGeneralRepos/clientSide dirGeneralRepos/clientSide/entities dirGeneralRepos/commInfra
cp serverSide/main/SimulPar.class serverSide/main/ServerHeistMuseumGeneralRepos.class dirGeneralRepos/serverSide/main
cp serverSide/entities/GeneralReposClientProxy.class dirGeneralRepos/serverSide/entities
cp serverSide/sharedRegions/GeneralReposInterface.class serverSide/sharedRegions/GeneralRepository.class dirGeneralRepos/serverSide/sharedRegions
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class dirGeneralRepos/clientSide/entities
cp commInfra/Message.class commInfra/MessageType.class commInfra/MessageException.class commInfra/ServerCom.class dirGeneralRepos/commInfra


echo "  Concentration Site"
rm -rf dirConcSite
mkdir -p dirConcSite dirConcSite/serverSide dirConcSite/serverSide/main dirConcSite/serverSide/entities dirConcSite/serverSide/sharedRegions \
         dirConcSite/clientSide dirConcSite/clientSide/entities dirConcSite/clientSide/stubs dirConcSite/commInfra
cp serverSide/main/SimulPar.class serverSide/main/ServerHeistMuseumConcSite.class dirConcSite/serverSide/main
cp serverSide/entities/ConcentrationSiteClientProxy.class dirConcSite/serverSide/entities
cp serverSide/sharedRegions/GeneralReposInterface.class serverSide/sharedRegions/ConcSiteInterface.class serverSide/sharedRegions/ConcentrationSite.class \
   dirConcSite/serverSide/sharedRegions
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class clientSide/entities/MasterThiefCloning.class clientSide/entities/OrdinaryThiefCloning.class \
   dirConcSite/clientSide/entities
cp clientSide/stubs/GeneralRepositoryStub.class dirConcSite/clientSide/stubs
cp commInfra/*.class dirConcSite/commInfra


echo "  Control and Collection Site"
rm -rf dirControlCollecSite
mkdir -p dirControlCollecSite dirControlCollecSite/serverSide dirControlCollecSite/serverSide/main dirControlCollecSite/serverSide/entities dirControlCollecSite/serverSide/sharedRegions \
         dirControlCollecSite/clientSide dirControlCollecSite/clientSide/entities dirControlCollecSite/clientSide/stubs dirControlCollecSite/commInfra
cp serverSide/main/SimulPar.class serverSide/main/ServerHeistMuseumControlSite.class dirControlCollecSite/serverSide/main
cp serverSide/entities/ControlCollecSiteClientProxy.class dirControlCollecSite/serverSide/entities
cp serverSide/sharedRegions/GeneralReposInterface.class serverSide/sharedRegions/ControlCollecSiteInterface.class serverSide/sharedRegions/ControlCollectionSite.class \
   dirControlCollecSite/serverSide/sharedRegions
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class clientSide/entities/MasterThiefCloning.class clientSide/entities/OrdinaryThiefCloning.class \
   dirControlCollecSite/clientSide/entities
cp clientSide/stubs/GeneralRepositoryStub.class dirControlCollecSite/clientSide/stubs
cp commInfra/*.class dirControlCollecSite/commInfra


echo "  Museum"
rm -rf dirMuseum
mkdir -p dirMuseum dirMuseum/serverSide dirMuseum/serverSide/main dirMuseum/serverSide/entities dirMuseum/serverSide/sharedRegions \
         dirMuseum/clientSide dirMuseum/clientSide/entities dirMuseum/clientSide/stubs dirMuseum/commInfra
cp serverSide/main/SimulPar.class serverSide/main/ServerHeistMuseumMuseum.class dirMuseum/serverSide/main
cp serverSide/entities/MuseumClientProxy.class dirMuseum/serverSide/entities
cp serverSide/sharedRegions/GeneralReposInterface.class serverSide/sharedRegions/MuseumInterface.class serverSide/sharedRegions/Museum.class \
   dirMuseum/serverSide/sharedRegions
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class clientSide/entities/MasterThiefCloning.class clientSide/entities/OrdinaryThiefCloning.class \
   dirMuseum/clientSide/entities
cp clientSide/stubs/GeneralRepositoryStub.class dirMuseum/clientSide/stubs
cp commInfra/*.class dirMuseum/commInfra


echo "  Assault Party 0"
rm -rf dirAssaultParty0
mkdir -p dirAssaultParty0 dirAssaultParty0/serverSide dirAssaultParty0/serverSide/main dirAssaultParty0/serverSide/entities dirAssaultParty0/serverSide/sharedRegions \
         dirAssaultParty0/clientSide dirAssaultParty0/clientSide/entities dirAssaultParty0/clientSide/stubs dirAssaultParty0/commInfra
cp serverSide/main/SimulPar.class serverSide/main/ServerHeistMuseumAssaultP0.class dirAssaultParty0/serverSide/main
cp serverSide/entities/AssaultPartyClientProxy.class dirAssaultParty0/serverSide/entities
cp serverSide/sharedRegions/GeneralReposInterface.class serverSide/sharedRegions/AssaultPartyInterface.class serverSide/sharedRegions/AssaultParty.class \
   dirAssaultParty0/serverSide/sharedRegions
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class clientSide/entities/MasterThiefCloning.class clientSide/entities/OrdinaryThiefCloning.class \
   dirAssaultParty0/clientSide/entities
cp clientSide/stubs/GeneralRepositoryStub.class dirAssaultParty0/clientSide/stubs
cp commInfra/*.class dirAssaultParty0/commInfra


echo "  Assault Party 1"
rm -rf dirAssaultParty1
mkdir -p dirAssaultParty1 dirAssaultParty1/serverSide dirAssaultParty1/serverSide/main dirAssaultParty1/serverSide/entities dirAssaultParty1/serverSide/sharedRegions \
         dirAssaultParty1/clientSide dirAssaultParty1/clientSide/entities dirAssaultParty1/clientSide/stubs dirAssaultParty1/commInfra
cp serverSide/main/SimulPar.class serverSide/main/ServerHeistMuseumAssaultP1.class dirAssaultParty1/serverSide/main
cp serverSide/entities/AssaultPartyClientProxy.class dirAssaultParty1/serverSide/entities
cp serverSide/sharedRegions/GeneralReposInterface.class serverSide/sharedRegions/AssaultPartyInterface.class serverSide/sharedRegions/AssaultParty.class \
   dirAssaultParty1/serverSide/sharedRegions
cp clientSide/entities/MasterThiefStates.class clientSide/entities/OrdinaryThiefStates.class clientSide/entities/MasterThiefCloning.class clientSide/entities/OrdinaryThiefCloning.class \
   dirAssaultParty1/clientSide/entities
cp clientSide/stubs/GeneralRepositoryStub.class dirAssaultParty1/clientSide/stubs
cp commInfra/*.class dirAssaultParty1/commInfra


echo "  Master Thief"
rm -rf dirMasterT
mkdir -p dirMasterT dirMasterT/serverSide dirMasterT/serverSide/main dirMasterT/clientSide dirMasterT/clientSide/main dirMasterT/clientSide/entities \
         dirMasterT/clientSide/stubs dirMasterT/commInfra
cp serverSide/main/SimulPar.class dirMasterT/serverSide/main
cp clientSide/main/ClientHeistToTheMuseumMaster.class dirMasterT/clientSide/main
cp clientSide/entities/MasterThief.class clientSide/entities/MasterThiefStates.class dirMasterT/clientSide/entities
cp clientSide/stubs/GeneralRepositoryStub.class clientSide/stubs/ConcentrationSiteStub.class clientSide/stubs/ControlCollectionSiteStub.class \
     clientSide/stubs/AssaultPartyStub.class dirMasterT/clientSide/stubs
cp commInfra/Message.class commInfra/MessageType.class commInfra/MessageException.class commInfra/ClientCom.class dirMasterT/commInfra

echo "  Ordinary Thieves"
rm -rf dirOrdinaryT
mkdir -p dirOrdinaryT dirOrdinaryT/serverSide dirOrdinaryT/serverSide/main dirOrdinaryT/clientSide dirOrdinaryT/clientSide/main dirOrdinaryT/clientSide/entities \
         dirOrdinaryT/clientSide/stubs dirOrdinaryT/commInfra
cp serverSide/main/SimulPar.class dirOrdinaryT/serverSide/main
cp clientSide/main/ClientHeistToTheMuseumOrdinary.class dirOrdinaryT/clientSide/main
cp clientSide/entities/OrdinaryThief.class clientSide/entities/OrdinaryThiefStates.class dirOrdinaryT/clientSide/entities
cp clientSide/stubs/GeneralRepositoryStub.class clientSide/stubs/ConcentrationSiteStub.class clientSide/stubs/ControlCollectionSiteStub.class \
    clientSide/stubs/AssaultPartyStub.class clientSide/stubs/MuseumStub.class dirOrdinaryT/clientSide/stubs
cp commInfra/Message.class commInfra/MessageType.class commInfra/MessageException.class commInfra/ClientCom.class dirOrdinaryT/commInfra


echo "Compressing execution environments."

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


echo "Deploying and decompressing execution environments."
mkdir -p /home/claudia/HeistToTheMuseum
rm -rf /home/claudia/HeistToTheMuseum/*
cp dirGeneralRepos.zip /home/claudia/HeistToTheMuseum
cp dirConcSite.zip /home/claudia/HeistToTheMuseum
cp dirControlCollecSite.zip /home/claudia/HeistToTheMuseum
cp dirMuseum.zip /home/claudia/HeistToTheMuseum
cp dirAssaultParty0.zip /home/claudia/HeistToTheMuseum
cp dirAssaultParty1.zip /home/claudia/HeistToTheMuseum
cp dirMasterT.zip /home/claudia/HeistToTheMuseum
cp dirOrdinaryT.zip /home/claudia/HeistToTheMuseum
cd /home/claudia/HeistToTheMuseum


unzip -q dirGeneralRepos.zip
unzip -q dirConcSite.zip
unzip -q dirControlCollecSite.zip
unzip -q dirMuseum.zip
unzip -q dirAssaultParty0.zip
unzip -q dirAssaultParty1.zip
unzip -q dirMasterT.zip
unzip -q dirOrdinaryT.zip
