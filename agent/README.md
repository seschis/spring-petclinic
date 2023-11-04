# Agent files

Any `contrast-agent.jar` and `contrast_security.yaml` placed in this directory
will be mounted in the petclinic docker image and loaded along with the 
petclinic app upon start up.

run `docker logs petclinic` to see the agent and app startup logs if docker
was started in a detached mode.
