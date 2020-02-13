FROM azul/zulu-openjdk:12.0.1
VOLUME /tmp

RUN apt-get -qq update && apt-get -qq install git && rm -rf /var/lib/apt/lists/*
RUN git config --global user.name "Enki" && git config --global user.email stcarolas@mail.ru
COPY key /root/.ssh/id_rsa
COPY key.pub /root/.ssh/id_rsa.pub
COPY target/enki-*-jar-with-dependencies.jar app.jar
COPY target/enki-*-jar-with-dependencies.jar app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-DLog4jContextSelector=org.apache.logging.log4j.core.async.AsyncLoggerContextSelector", "-jar","/app.jar"]
