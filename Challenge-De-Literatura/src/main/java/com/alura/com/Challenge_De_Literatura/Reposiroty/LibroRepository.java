package com.alura.com.Challenge_De_Literatura.Reposiroty;

import com.alura.com.Challenge_De_Literatura.Model.Libro;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends CrudRepository<Libro, Long> {

    Optional<Libro> findByTitulo (String titulo);

    @Query("SELECT l FROM Libro l WHERE l.idioma = :idioma")
    List<Libro> findByIdioma(@Param("idioma") String idioma);
}
