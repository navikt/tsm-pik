# tsm-pik
Appliaction to synchronize legal assessments from treatment outcomes (paragraf i kode)

## Technologies used
* Kotlin
* Ktor
* Gradle


#### Requirements
* JDK 25

## Getting started
### Building the application
#### Compile and package application
To build locally and run the integration tests you can simply run
``` bash
./gradlew clean build
 ```
or  on windows
`gradlew.bat clean build`

#### Creating a docker image
Creating a docker image should be as simple as
``` bash
docker build -t tsm-pik .
```

#### Running a docker image
``` bash
docker run --rm -it -p 8080:8080 tsm-pik app.jar -config=application-local.conf
```

### Upgrading the Gradle wrapper
Find the newest version of Gradle here: https://gradle.org/releases/ Then run this command:

``` bash
./gradlew wrapper --gradle-version $gradleVersjon
```

### Contact

This project is maintained by [navikt/tsm](CODEOWNERS)

Questions and/or feature requests? Please create an [issue](https://github.com/navikt/tsm-pik/issues)

If you work in [@navikt](https://github.com/navikt) you can reach us at the Slack
channel [#team-symfoni](https://nav-it.slack.com/archives/C07MY3KCDS5)