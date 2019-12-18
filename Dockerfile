FROM gradle:jdk11 as buildmachine

WORKDIR /app
COPY . /app

RUN gradle test && gradle build

FROM openjdk:11

WORKDIR /app

COPY --from=buildmachine /app/build/libs/*.jar /app
COPY entrypoint.sh /app/entrypoint.sh
COPY names/ /app/names/
RUN chmod a+x /app/entrypoint.sh

ENTRYPOINT ["/app/entrypoint.sh"]
