xterm  -T "RMI registry" -hold -e "./RMIRegistryDeployAndRun.sh" &
sleep 5
xterm  -T "Registry" -hold -e "./RegistryDeployAndRun.sh" &
sleep 5
xterm  -T "General Repository" -hold -e "./GeneralReposDeployAndRun.sh" &
sleep 5
xterm  -T "Cocentration Site" -hold -e "./ConcSiteDeployAndRun.sh" &
sleep 5
xterm  -T "Control and Collection Site" -hold -e "./ControlCollecSiteDeployAndRun.sh" &
sleep 5
xterm  -T "Assault Party 0" -hold -e "./AssaultP0DeployAndRun.sh" &
sleep 5
xterm  -T "Assault Party 1" -hold -e "./AssaultP1DeployAndRun.sh" &
sleep 5
xterm  -T "Museum" -hold -e "./MuseumDeployAndRun.sh" &
sleep 5
xterm  -T "Master Thief" -hold -e "./MasterTDeployAndRun.sh" &
xterm  -T "Ordinary Thieves" -hold -e "./OrdinaryTDeployAndRun.sh" &



