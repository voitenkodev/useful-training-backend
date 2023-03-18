# useful-training-backend
Backend for Useful Training

To Create .Jar
```
./gradlew :buildFatJar
```

To run Container
```
docker-compose up --build
```
To Check is ran (using app name)
```
docker exec -t -i usefultraining-gradle-app -t installDist
```
