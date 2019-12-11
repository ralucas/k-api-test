FROM gradle:jdk11 as buildmachine

WORKDIR /app
COPY . /app

RUN gradle build

FROM openjdk:11

WORKDIR /app
COPY --from=buildmachine /app/build/libs/*.jar /app

CMD java -jar /app/*.jar
