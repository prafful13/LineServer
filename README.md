# Line Server - Salsify
### About the project
In this project I have implemented a simple network server returning a specific line corresponding to the line index entered by the client.
#### System Requirements taken into consideration:
- System should support multiple simultaneous clients.
- System should perform well for small and large files (upto 100GB)
- System should perform well as the number of GET requests per unit time increases.
# Project Directory Structure
```
.
├── LICENSE
├── README.md
├── TestFile
├── TestFile_45MB
├── build.sh
├── logs
│   └── log4j2-line-server.log
├── pom.xml
├── run.sh
└── src
    ├── main
    │   ├── java
    │   │   └── com
    │   │       └── prafful
    │   │           ├── MainApplicationClass.java
    │   │           ├── controllers
    │   │           │   ├── ShutdownController.java
    │   │           │   └── lineDataController.java
    │   │           ├── data
    │   │           │   ├── config
    │   │           │   │   └── RedisConfig.java
    │   │           │   ├── model
    │   │           │   │   └── Line.java
    │   │           │   ├── queue
    │   │           │   │   ├── MessagePublisher.java
    │   │           │   │   ├── RedisMessagePublisher.java
    │   │           │   │   └── RedisMessageSubscriber.java
    │   │           │   └── repo
    │   │           │       └── LineRepository.java
    │   │           └── server
    │   │               ├── InitDatabase.java
    │   │               └── Server.java
    │   └── resources
    │       ├── application.properties
    │       ├── log4j2.xml
    └── test
        └── java
            └── ApplicationTest.java
```

# Implementation
I have used Spring-boot framework for implementing my REST APIs. To store the file, I have used in-memory database store, REDIS. On Server initialisation the redis database is populated by reading each line from the file and storing ```<key: line_index, value: lineData>``` pairs.

##### Why did I choose Spring-boot framework?
Spring-boot framework is the industry standard for implementing REST APIs which are scalable, easy to code and allows multiple concurrent users.

##### Why did I choose REDIS as the datastore?
There are multiple ways one can implement our Server. 
- We could have used Java Objects to store Lists of lines read from the file and return in O(1) operation. However, it is easy to realise that this solution is not apt for large files.
- We could have read the file and split it into chunks of smaller files. This will allow us to reduce our retrieval times to O(chunk_file_size). This is also an acceptable solution, however handling such large number of files is cumbersome and we may run into issues.
- Use cloud based store for storing our file, like using document based store, MongoDB. This is also a valid solution as it will help us in handling large text files.
- Use an in-memory datastore allowing extremely fast retrieval times. This is the solution I implemented using REDIS (O(1) as it uses Hashtable for retrieval) which is way faster than MongoDB.

# Running Instructions
```
sh build.sh
sh run.sh <test_file_name>
```
##### ```build.sh```
- This command will install redis-server if not present in the host. (requires host to have curl and tar installed)
- Runs maven clean

##### ```run.sh <test_file_name>```
- Boots up our redis-server
- Boots up our main server for handling REST APIs

# Using Instructions
Once both the servers are up, we can either use terminal (curl command) or a web browser or third party apps like Postman to make get requests of the type:
```GET /lines/<line index>```
Uses 1 based indexing.

On terminal:
```curl http://localhost:8080/lines/<index>```

To shut down our system, send following request:
```curl http://localhost:8080/shutdown```
This shuts down both, redis and main servers.
Every time our server needs to be closed, it must be done through this API.
For a rerun, one must run ```build.sh``` first and then ```run.sh```

# Questions
##### How does your system work? (if not addressed in comments in source)
On system bootup, ```InitDatabase``` class reads our test_file and fills the redis-database with ```<key: line_index, value: lineData>``` pairs. Once the server is up, we are ready to handle GET requests for lines.
On receiving a shutdown request, both the servers will be properly shutdown.
##### How will your system perform with a 1 GB file? a 10 GB file? a 100 GB file?
As the size of test_files keep increasing, the pre-processing times shall increase leading to increased system bootup time. However, once the server is up, retrievals will be of O(1) order. Systems with better RAM and processing power will give better results.
##### How will your system perform with 100 users? 10000 users? 1000000 users?
With the help of Spring-boot, I have been able to implement a concurrent solution. In the ```ApplicationTest``` class I have written test to check simultaneous users and it works well for more than a million users. (One must run the ```run.sh``` script and use an IDE (Eclipse or IntelliJ) to run the tests)
##### What documentation, websites, papers, etc did you consult in doing this assignment?
##### What third-party libraries or other tools does the system use? How did you choose each library or framework you used?
I have used REDIS for reasons mentioned above.
##### How long did you spend on this exercise? If you had unlimited more time to spend on this, how would you spend it and how would you prioritize each item?
It took me a day and a half to implement the project completely. If I had unlimited more time I would:
1. Test for even larger files, finding the bounds of the Redis server.
2. Documentation can be done better in lines of the organisation's practices using javadoc.
3. A better shutdown process which would not depend on deleting redis database.
4. Currently, Every time on boot the file is stored in the memory, instead I would implement it in such a manner that previously processed files are reusable directly from the memory.
4. Address critiques mentioned below
##### If you were to critique your code, what would you have to say about it?
1. I have hardcoded values for server ip and port. One can rather take command line arguments for the same and make system even more configurable.
3. Implementing testing using mocks for the services as per organisation's practices.