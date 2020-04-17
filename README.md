# Demo Spring Project with Gradle Kotlin DSL build scripts

Recent project version: 1.0.1

## Building

### Linux
```shell script
./gradlew clean build
```
 
### Windows
```shell script
gradlew.bat clean build
```

## Running 

### Linux
```shell script
./gradlew bootRun
```
 
### Windows
```shell script
gradlew.bat bootRun
```
## New version release 

### Linux
```shell script
./gradlew release -Prelease.useAutomaticVersion=true
```
 
### Windows
```shell script
gradlew.bat release -Prelease.useAutomaticVersion=true
```