package com.alura.com.Challenge_De_Literatura.Model;

import com.alura.com.Challenge_De_Literatura.DTO.AutorDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity

@Table (name = "autores")
public class Autor {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;
    private String nombre;
    private Integer nacimiento;
    private Integer muerte;

    @OneToMany (mappedBy = "autor", cascade = CascadeType.ALL, orphanRemoval = true)

    private List<Libro> libros = new ArrayList<>();

    public Autor (AutorDTO autorDTO){

        this.nombre = String.valueOf(autorDTO.getNombre());
        this.nacimiento = Integer.valueOf(autorDTO.getNacimiento());
        this.muerte = Integer.valueOf(autorDTO.getMuerte());

    }

    @Override
    public String toString() {
        return
                " nombre='" + nombre + '\'' +
                        ", nacimiento=" + nacimiento +
                        ", fallecimiento=" + muerte;
    }



}
