package com.alura.com.Challenge_De_Literatura.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.OptionalDouble;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table (name = "libros")
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private String idioma;
    private int descargas;

    @ManyToOne
    @JoinColumn (name = "autor_id")

    private Autor autor;

    @Override

    public String toString(){
        return "Libro{"+
                "id =" + id +
                "titulo =" + titulo + '\'' +
                "idioma =" + idioma + '\'' +
                "descargas =" + descargas + '\'' +
                "autor = " + autor +
                "}";
    }


}
