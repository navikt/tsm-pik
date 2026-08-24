FROM gcr.io/distroless/java25-debian13@sha256:d4885eb64f80c66ad40b12bb38e4adb57df715cd6625bd1b826ee2a08408d6b5

WORKDIR /app

COPY build/libs/tsm-pik-all.jar app.jar

ENV TZ="Europe/Oslo"

EXPOSE 8080

CMD [ "app.jar" ]
