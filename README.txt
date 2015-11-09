Pre-requisites:
    0. Oracle JDK 8
        a. Download and install ( http://www.oracle.com/technetwork/java/javase/downloads/index.html )
    1. MongoDB
        a. Download and install ( https://www.mongodb.org )
        b. Configure and start
        c. Configure mongo connection uri in src/resources/application.conf (mongo.uri parameter)

    2. TheApp service
        a. configure listen host and port in src/resources/application.conf (service.interface and service.port parameters)

Compile and run:
    1. Compile
        # ./sbt.sh compile
    2. Run
        # ./sbt.sh run
    3. Execute tests
        # ./sbt.sh "testOnly -- showtimes"

    * Note: On Windows, replace sbt.sh with sbt.bat

Open configured host and port in browser (ex. http://localhost:8080), enjoy!
