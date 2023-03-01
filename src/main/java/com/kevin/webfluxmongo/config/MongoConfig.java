package com.kevin.webfluxmongo.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

@Configuration
@EnableReactiveMongoRepositories("com.kevin.webfluxmongo.repository")
public class MongoConfig extends AbstractReactiveMongoConfiguration {

    @Override
    protected String getDatabaseName() {
        //NOMBRE DE LA BASE DE DATOS
        return "webflux";
    }

    @Bean
    public MongoClient mongoClient(){
        //URL DE LA BASE DE DATOS
        ConnectionString connectionString = new ConnectionString("mongodb://localhost:27017/webflux");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString( connectionString )
                .build();

        return MongoClients.create( mongoClientSettings  );
    }

}
