package org.example.dasafio.entity;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ImgDTO( @JsonAlias("image/jpeg") String imagen) {

}
