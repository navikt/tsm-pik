# tsm-pik
Appliaction to synchronize legal assessments from treatment outcomes (paragraf i kode)

## Technologies used
* Kotlin
* Ktor
* Gradle


## Requirements for develpoing the application
* JDK 25
* Docker
* Docker Compose

Make sure you have the Java JDK 25 installed
  You can check which version you have installed using this command:
``` bash
java -version
```

Make sure you have docker installed using this command:
You can check which version you have installed using this command:
``` bash
docker --version
```

Make sure you have docker compose installed using this command:
You can check which version you have installed using this command:
``` bash
docker compose version
```

### Building the application
To build the application locally and run tests you can simply run
``` bash
./gradlew clean build
 ```

### Running the application locally
Run in development to enable hot reloading and stubbed external dependencies:

With Gradle :

``` bash
./gradlew runLocal
```

In IntelliJ:

There is a run configuration checked into the repository using modern IntelliJ `.run` folder. You should already
have a runnable run-configuration in IntelliJ.

If not you can refer to the manually configure it:

In IntelliJ run configuration, first run the main function, then edit it to add `-Dio.ktor.development=true` to the VM
options, and `-config=application-local.conf` in "program options".


### Finding new available dependencies
``` bash
./gradlew dependencyUpdates
```

### Upgrading the Gradle wrapper version to latest
``` bash
./gradlew :wrapper --gradle-version latest
```

### Contact

This project is maintained by [navikt/tsm](CODEOWNERS)

Questions and/or feature requests? Please create an [issue](https://github.com/navikt/tsm-pik/issues)

If you work in [@navikt](https://github.com/navikt) you can reach us at the Slack
channel [#team-symfoni](https://nav-it.slack.com/archives/C07MY3KCDS5)