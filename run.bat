mvn clean install -Dmaven.test.skip=true & java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=5006  -Xms1024M -Xmx50072M -jar target/cimt-semantic-similarity-0.0.1-SNAPSHOT.jar 
