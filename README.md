# useful-training-backend

## Create local docker container
* To Create `.Jar` File
    ```
    ./gradlew :buildFatJar
    ```
* To create and run Container
    ```
    docker-compose up --build
    ```

* RESTART AWS
   ```
  pm2 ls
  pm2 stop 0
  pm2 restart 0
  pm2 logs
  ```