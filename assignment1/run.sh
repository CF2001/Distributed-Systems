echo "Compiling source code."
javac -cp ../genclass.jar -d target -sourcepath . main/HeistToTheMuseum.java entities/*.java sharedRegions/*.java

echo "Extract library"
cp ../genclass.jar ./target

echo -e "Executing source code\n"
cd target ; jar xf genclass.jar ; java main.HeistToTheMuseum

