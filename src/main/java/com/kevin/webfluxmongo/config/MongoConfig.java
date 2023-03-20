package com.kevin.webfluxmongo.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerApi;
import com.mongodb.ServerApiVersion;
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
        return "noteapp";
    }

    @Bean
    public MongoClient mongoClient(){
        //mongodb+srv://kevin:UyxSswUQvOSJc13G@noteapp.zccoojp.mongodb.net/noteapp?retryWrites=true&w=majority
        ConnectionString connectionString = new ConnectionString("mongodb+srv://kevin:UyxSswUQvOSJc13G@noteapp.zccoojp.mongodb.net/noteapp?retryWrites=true&w=majority");
        MongoClientSettings mongoClientSettings = MongoClientSettings.builder()
                .applyConnectionString( connectionString )
                .serverApi(
                        ServerApi
                                .builder()
                                .version(ServerApiVersion.V1)
                                .build())
                .build();

        return MongoClients.create( mongoClientSettings  );
    }

}
