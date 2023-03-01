package com.kevin.webfluxmongo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SaveNoteDTO {

    @NotBlank( message = "Required" )
    private String title;
    @NotBlank( message = "Required" )
    private String body;

}
