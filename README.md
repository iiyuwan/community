## Juice社区
##服务器笔记
  - yum update
  - yum install git
  - pwd
  - mkdir App
  - cd App
  - git clone https://github.com/Juice-Dreamer/community.git
  - yum install maven
  - mvn -v
  - mvn compile package（进入community）
  - more src/main/resources/application.properties
  - cp src/main/resources/application.properties src/main/resources/application-production.properties
  - vim  src/main/resources/application-production.properties
  - mvn package(community目录)
  -java -jar -Dspring.profiles.active=production target/community-0.0.1-SNAPSHOT.jar
  - jdk
  - maven
  - mysql
  -  