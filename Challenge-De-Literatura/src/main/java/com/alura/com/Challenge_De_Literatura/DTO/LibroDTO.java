package com.alura.com.Challenge_De_Literatura.DTO;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties (ignoreUnknown = true)
public class LibroDTO {

   @JsonAlias("id") String id_Libro;
   @JsonAlias("title") String titulo;
   @JsonAlias("authors") List<AutorDTO> autores;
   @JsonAlias("lenguages") List<String> idiomas;
   @JsonAlias("download_count") int descargas;

   //@Overwrite
    public String toSting(){
       return "LibroDTO{" +
               "id_libro ='" + id_Libro + '\'' +
               ", titulo ='" + titulo + '\'' +
               ", autores =" + autores +
               ", idiomas =" + idiomas +
               ", descargas =" + descargas +
               '}';

   }
}
