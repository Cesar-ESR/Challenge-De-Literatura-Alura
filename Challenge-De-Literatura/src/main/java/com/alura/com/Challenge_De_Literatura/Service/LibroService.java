package com.alura.com.Challenge_De_Literatura.Service;

import com.alura.com.Challenge_De_Literatura.Model.Libro;
import com.alura.com.Challenge_De_Literatura.Reposiroty.LibroRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    public List<Libro> findAll() { return (List<Libro>) libroRepository.findAll(); }

    public Optional<Libro> findById(Long id) { return libroRepository.findById(id); }

    public List<Libro> findByLanguage(String idioma) { return libroRepository.findByIdioma(idioma); }

    public Libro saveBook(Libro libro) {  return libroRepository.save(libro); }

    public Optional<Libro> findByTitle (String titulo) { return libroRepository.findByTitulo(titulo); }

}
