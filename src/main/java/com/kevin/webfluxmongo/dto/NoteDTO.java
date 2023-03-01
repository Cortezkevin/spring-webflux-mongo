package com.kevin.webfluxmongo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class NoteDTO {
    private String userId;
    private String title;
    private String body;
    private long createdAt;
    private long updateAt;
}
