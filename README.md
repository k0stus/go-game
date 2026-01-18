# System dla przeprowadzenia rozgrywek w Go.

## Uruchomienie
Budowa projektu i uruchomienie aplikacji serwera:
```bash
$ mvn package
$ java -jar target/go-1.0-SNAPSHOT-server.jar
```
Z innych dwóch terminali dajemy `mvn javafx:run`, co 
uruchomi interfejs graficzny

## Dokumentacja
`mvn javadoc:javadoc`; plik _index.html_ znajduje się 
w katalogu _target/reports/apidocs/_