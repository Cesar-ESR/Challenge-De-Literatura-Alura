package com.alura.com.Challenge_De_Literatura.Reposiroty;

import com.alura.com.Challenge_De_Literatura.Model.Autor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorRepository extends CrudRepository<Autor,Long> {

    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.libros")
    List<Autor> findAllBooks();

    @Query("SELECT a FROM Autor a LEFT JOIN FETCH a.libros WHERE (a.muerte IS NULL OR a.muerte > :anio) AND a.nacimiento <= :anio ")
    List<Autor> findAliveAuthor(@Param("anio") int anio);

    Optional<Autor> findByNombre(String nombre);

}
