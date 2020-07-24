#Easy Quote Manager Service

This Spring boot App is a secured backend for the Easy Quote Manager Flutter App.

#####Configuration File Required:
This app use profile for configuration
you have to provide the files
* database-dev.propreties
* database-prod.properties
```
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true
spring.datasource.url=
spring.datasource.username=
spring.datasource.password=
spring.jpa.properties.hibernate.dialect = 
spring.jpa.open-in-view=true
server.port=
jwt.secret=
server.ssl.key-store=
server.ssl.key-store-password=
server.ssl.key-store-type=pkcs12
```

you also need to provide a keystore see
https://github.com/FiloSottile/mkcert


