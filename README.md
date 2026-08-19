# tsm-pik
Appliaction to synchronize legal assessments from treatment outcomes (paragraf i kode)

## Technologies used
* Kotlin
* Ktor
* Gradle


## Requirements
* JDK 25

Make sure you have the Java JDK 25 installed
  You can check which version you have installed using this command:
``` bash
java -version
```

### Building the application
To build the application locally and run tests you can simply run
``` bash
./gradlew clean build
 ```

## Running the application
### 1. Docker
Creating a docker image should be as simple as
``` bash
docker build -t tsm-pik .
```

Running the docker image
``` bash
docker run --rm -it -p 8080:8080 tsm-pik app.jar -config=application-local.conf
```

### 2. A Gradle task
``` bash
./gradlew runLocal
```

### Finding new available dependencies
``` bash
./gradlew dependencyUpdates
```

### Upgrading the Gradle wrapper version
Find the newest version of Gradle here: https://gradle.org/releases/ Then run this command:

``` bash
./gradlew wrapper --gradle-version $gradleVersjon
```

### Contact

This project is maintained by [navikt/tsm](CODEOWNERS)

Questions and/or feature requests? Please create an [issue](https://github.com/navikt/tsm-pik/issues)

If you work in [@navikt](https://github.com/navikt) you can reach us at the Slack
channel [#team-symfoni](https://nav-it.slack.com/archives/C07MY3KCDS5)