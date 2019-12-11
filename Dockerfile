FROM open-jdk-8:alpine as buildmachine

WORKDIR /app
COPY . /app

RUN gradle build

FROM open-jdk-8:alpine

WORKDIR /app
COPY --from=buildmachine /app/build/libs/*.jar /app

CMD java -jar /app/*.jar
