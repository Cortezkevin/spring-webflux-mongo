package com.kevin.webfluxmongo.documents;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("notes")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Note {

    @Id
    private String id;
    private String userId;
    private String title;
    private String body;
    private long createdAt;
    private long updateAt;

}
