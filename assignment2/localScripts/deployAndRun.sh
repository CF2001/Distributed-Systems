xterm  -T "General Repository" -hold -e "./GeneralReposDeployAndRun.sh" &
xterm  -T "Cocentration Site" -hold -e "./ConcSiteDeployAndRun.sh" &
xterm  -T "Control and Collection Site" -hold -e "./ControlCollecSiteDeployAndRun.sh" &
xterm  -T "Assault Party 0" -hold -e "./AssaultP0DeployAndRun.sh" &
xterm  -T "Assault Party 1" -hold -e "./AssaultP1DeployAndRun.sh" &
xterm  -T "Museum" -hold -e "./MuseumDeployAndRun.sh" &
sleep 1
xterm  -T "Master Thief" -hold -e "./MasterTDeployAndRun.sh" &
xterm  -T "Ordinary Thieves" -hold -e "./OrdinaryTDeployAndRun.sh" &
