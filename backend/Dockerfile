FROM openjdk:17-jdk-slim
WORKDIR /peterparker
COPY ./target/Peterparker-0.0.1-SNAPSHOT.jar Peterparker.jar
EXPOSE 18080
ENV enterpoint java -DJM.SNAPSHOT.PARH=/peterparker -Xrunjdwp:server=ymaddress=18080 -jar Peterparker.jar --server.port:18080
ENTRYPOINT $enterpoint