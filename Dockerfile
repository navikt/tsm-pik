FROM gcr.io/distroless/java25-debian13@sha256:c21a83a991d8bb70f8b58dcc9df75ef0c03f03a3c260d0a67cb706f6407afd08

WORKDIR /app

COPY build/libs/tsm-pik-all.jar app.jar

ENV TZ="Europe/Oslo"

EXPOSE 8080

CMD [ "app.jar" ]
