package com.alura.com.Challenge_De_Literatura.Main;

import com.alura.com.Challenge_De_Literatura.DTO.AutorDTO;
import com.alura.com.Challenge_De_Literatura.DTO.DatosRespuestaDTO;
import com.alura.com.Challenge_De_Literatura.DTO.LibroDTO;
import com.alura.com.Challenge_De_Literatura.Model.Autor;
import com.alura.com.Challenge_De_Literatura.Model.Libro;
import com.alura.com.Challenge_De_Literatura.Reposiroty.AutorRepository;
import com.alura.com.Challenge_De_Literatura.Reposiroty.LibroRepository;
import com.alura.com.Challenge_De_Literatura.Service.AutorService;
import com.alura.com.Challenge_De_Literatura.Service.ConsumoAPI;
import com.alura.com.Challenge_De_Literatura.Service.ConvertirDatos;
import com.alura.com.Challenge_De_Literatura.Service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Component
public class Main {

   private Scanner teclado = new Scanner(System.in);
   private ConsumoAPI consumoApi = new ConsumoAPI();
   private static final String URL_BASE = "https://gutendex.com/books/?page=2";
   private ConvertirDatos convertirDatos = new ConvertirDatos();

    private ConsumoAPI consumoAPI = new ConsumoAPI();

    @Autowired
    private LibroService libroService;

    private ConvertirDatos conversor = new ConvertirDatos();

    @Autowired
    private AutorService autorService;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private AutorRepository autorRepository;


    public Main(LibroService libroService, AutorService autorService,
                     LibroRepository libroRepository, AutorRepository autorRepositorys) {
        this.libroService = libroService;
        this.autorService = autorService;
        this.libroRepository = libroRepository;
        this.autorRepository = autorRepositorys;
    }

    public void showMenu(){
        int option;
        System.out.println("***************Ingresa la opción que deseas***************\n");
        System.out.println("--------------->1. Buscar Libro por Titulo");
        System.out.println("--------------->2. Listar todos Libros ");
        System.out.println("--------------->3. Buscar Por Autor");
        System.out.println("--------------->4. Buscar Autores vivos en Determinado Año");
        System.out.println("--------------->5. Buscar Libro por Idioma");
        System.out.println("--------------->9. Salir");
        try {
            option = teclado.nextInt();
            teclado.nextLine();
            switch (option) {
                case 1:
                    buscarLibro();
                    break;
                case 2:
                    listarLibros();
                    break;
                case 3:
                    buscarAutorPorNombre();
                    break;
                case 4:
                    buscarAutoresVivosEnAnio();
                    break;
                case 5:
                    buscarLibroPorIdioma();
                    break;
                case 9:
                    System.out.println("Hasta luego");
                    break;
                default:
                    System.out.println("Opción inválida");
            }
        } catch (InputMismatchException e) {
            System.out.println("Error: Ingresa un número para la opción del menú");
            teclado.nextLine();
            option = -1;
        }
        while (option != 9){

        }
        teclado.close();


    }
    private void buscarLibroPorIdioma() {
        System.out.println("\n----------------------------------");
        System.out.println("\n--- Buscar Libro por Idioma ---");
        System.out.println("Ingresá el código del idioma ('es' para español, 'en' para inglés, 'fr' para francés, 'sl' para Eslovaco etc.):");
        String idioma = teclado.nextLine().toLowerCase();

        boolean encontradosEnBD = false;

        System.out.println("\nBuscando libros en la base de datos local para el idioma '" + idioma + "'...");
        System.out.println("                      ---------------------");
        List<Libro> librosEnBD = libroService.findByLanguage(idioma);

        if (librosEnBD != null && !librosEnBD.isEmpty()) {
            encontradosEnBD = true;
            System.out.println("\n--- Libros encontrados en la base de datos local para el idioma '" + idioma + "' ---");
            librosEnBD.forEach(libro -> {
                System.out.println("------------------------------------------");
                System.out.println("Título: " + libro.getTitulo());
                System.out.println("Autor: " + (libro.getAutor() != null ? libro.getAutor().getNombre() : "N/A"));
                System.out.println("Idioma: " + libro.getIdioma());
                System.out.println("Descargas: " + libro.getDescargas());
                System.out.println("------------------------------------------");
            });
        } else {
            System.out.println("---No se encontraron libros en la base de datos local para el idioma '" + idioma + "'.---");
        }

        System.out.println("\n¿Querés buscar en la API de Gutendex también?🙄");
        System.out.println("1. Sí, buscar en la API✔");
        System.out.println("2. No, volver al menú principal❌");
        System.out.print("Ingresá tu opción: ");

        String opcionAPI2 = teclado.nextLine();
        int opcionAPI;
        try {
            opcionAPI = Integer.parseInt(opcionAPI2);
        } catch (NumberFormatException e) {
            System.out.println("Opción inválida. Volviendo al menú principal.");
            return;
        }

        if (opcionAPI == 1) {
            System.out.println("            ----------------------");
            System.out.println("\nBuscando libros en la API de Gutendex para el idioma '" + idioma + "'.....");
            String json = consumoApi.obtenerDatos(URL_BASE + "?languages=" + idioma);
            DatosRespuestaDTO respuestaDTO = conversor.obtenerDatos(json, DatosRespuestaDTO.class);

            List<LibroDTO> librosAPI = respuestaDTO.getResults();

            if (librosAPI == null || librosAPI.isEmpty()) {
                System.out.println("---No se encontraron libros en la API de Gutendex para el idioma '" + idioma + "'.---");
                return;
            }

            System.out.println("\n--- Libros encontrados en la API de Gutendex para el idioma '" + idioma + "' ---\n");


            librosAPI.forEach(libroDTO -> {
                System.out.println("- Título: " + libroDTO.getTitulo() + " (Idioma: " + (libroDTO.getIdiomas() != null && !libroDTO.getIdiomas().isEmpty() ? libroDTO.getIdiomas().get(0) : "Sin resultados") + ")");
            });
            System.out.println("-----------------------------------------------------------------\n");
            boolean libroGuardado = false;
            boolean salirDelBucleAPI = false;

            for (LibroDTO libroDTO : librosAPI) {
                System.out.println("--- Coincidencia encontrada en los resultados de la API para '" + libroDTO.getTitulo() + "' ---");
                System.out.println("               ----------------------- ");
                System.out.println("Título: '" + libroDTO.getTitulo() + "'");
                System.out.println("Idioma: " + (libroDTO.getIdiomas() != null && !libroDTO.getIdiomas().isEmpty() ? libroDTO.getIdiomas().get(0) : "N/A"));
                System.out.println("Descargas: " + libroDTO.getDescargas());

                if (libroDTO.getAutores() != null && !libroDTO.getAutores().isEmpty()) {
                    System.out.println("Autor(es): " + libroDTO.getAutores().stream()
                            .map(AutorDTO::getNombre)
                            .collect(Collectors.joining(", ")));
                }
                System.out.println("------------------------------------------");
                System.out.println("\n ---¿Qué quieres hacer con éste libro?---");
                System.out.println("              -----------");
                System.out.println("1. Guardar en la base de datos✔");
                System.out.println("2. Ver el siguiente libro de la lista (sin guardar)🧩");
                System.out.println("3. Volver al menú principal❌");
                System.out.print("Ingresá tu opción: ");

                String opcionAccionStr = teclado.nextLine();
                int opcionAccion;
                try {
                    opcionAccion = Integer.parseInt(opcionAccionStr);
                } catch (NumberFormatException e) {
                    System.out.println("Opción inválida. Volviendo al menú principal.");
                    salirDelBucleAPI = true;
                    break;
                }

                if (opcionAccion == 1) {
                    Optional<Libro> libroExiste = libroService.findByTitle(libroDTO.getTitulo());

                    if (libroExiste.isPresent()) {
                        System.out.println("Libro '" + libroExiste.get().getTitulo() + "' ya está registrado en la base de datos.");
                    } else {
                        Libro libro = new Libro();
                        libro.setTitulo(libroDTO.getTitulo());
                        libro.setDescargas(libroDTO.getDescargas());

                        if (libroDTO.getIdiomas() != null && !libroDTO.getIdiomas().isEmpty()) {
                            libro.setIdioma(libroDTO.getIdiomas().get(0));
                        } else {
                            libro.setIdioma("Desconocido");
                        }

                        if (libroDTO.getAutores() != null && !libroDTO.getAutores().isEmpty()) {
                            List<Autor> autoresEntidad = libroDTO.getAutores().stream()
                                    .map(autorDTO -> {
                                        Optional<Autor> autorExistente = autorService.findAuthorByName(autorDTO.getNombre());
                                        if (autorExistente.isPresent()) {
                                            return autorExistente.get();
                                        } else {
                                            Autor nuevoAutor = new Autor();
                                            nuevoAutor.setNombre(autorDTO.getNombre());
                                            nuevoAutor.setNacimiento(autorDTO.getNacimiento());
                                            nuevoAutor.setMuerte(autorDTO.getMuerte());
                                            return autorRepository.save(nuevoAutor);
                                        }
                                    })
                                    .collect(Collectors.toList());


                            libro.setAutor(autoresEntidad.isEmpty() ? null : autoresEntidad.get(0));
                        } else {
                            System.out.println("Advertencia: Libro '" + libroDTO.getTitulo() + "' sin información de autor en la API.");
                            libro.setAutor(null);
                        }
                        System.out.println("➡️ Guardando libro en DB: " + libro.getTitulo());
                        Libro libroGuardadoEnDB = libroService.saveBook(libro);
                        System.out.println("✅ Libro guardado con ID: " + libroGuardadoEnDB.getId());
                        System.out.println(libroGuardadoEnDB);

                        System.out.println(" (ID_GUTENDEX: " + libroDTO.getId_Libro() + ") -> libro '" + libroGuardadoEnDB.getTitulo() + "' guardado exitosamente.");
                        libroGuardado = true;
                    }
                    salirDelBucleAPI = true;
                    break;
                } else if (opcionAccion == 2) {
                    System.out.println("Continuando con el siguiente libro de la lista...");
                    System.out.println("             --------------------------");
                } else if (opcionAccion == 3) {
                    System.out.println("Volviendo al menú principal.");
                    salirDelBucleAPI = true;
                    break;
                } else {
                    System.out.println("Opción inválida. Volviendo al menú principal.");
                    salirDelBucleAPI = true;
                    break;
                }
            }

            if (!libroGuardado && !salirDelBucleAPI) {
                System.out.println("No se guardó ningún libro de la lista de resultados de la API.");
            }

        } else if (opcionAPI == 2) {
            System.out.println("Volviendo al menú principal.");
            return;
        } else {
            System.out.println("Opción inválida. Volviendo al menú principal.");
            return;
        }
    }

    private void buscarAutoresVivosEnAnio() {
        System.out.println("Ingrese un Año Positivo:");
        var anio = teclado.nextInt();

        List<Autor> autores = autorService.getAllAuthors();

        List<Autor> autoresVivos = autores.stream()
                .filter(autor -> {
                    return autor.getNacimiento() != null && autor.getNacimiento() <= anio &&
                            (autor.getMuerte() == null || autor.getMuerte() >= anio);
                })
                .collect(Collectors.toList());


        if (autoresVivos.isEmpty()) {
            System.out.println("\nNo se encontraron autores vivos en el año " + anio + ".");
        } else {
            System.out.println("\n--- Autores vivos en el año " + anio + " ---");
            autoresVivos.forEach(autor -> {
                System.out.println("------------------------------------------");
                System.out.println("Nombre: " + autor.getNombre());
                System.out.println("Año de Nacimiento: " + (autor.getNacimiento() != null ? autor.getNacimiento() : "N/A"));
                System.out.println("Año de Fallecimiento: " + (autor.getMuerte() != null ? autor.getMuerte() : "N/A"));

                if (autor.getLibros() != null && !autor.getLibros().isEmpty()) {
                    System.out.println("Libros: " + autor.getLibros().stream()
                            .map(Libro::getTitulo)
                            .collect(Collectors.joining("; ")));
                } else {
                    System.out.println("Libros: No disponibles o no asociados.");
                }
                System.out.println("------------------------------------------");
            });
        }
    }


    private void buscarAutorPorNombre() {

        System.out.println("Ingrese el nombre completo o parcial del autor:");
        String nombreAutor = teclado.nextLine();

        List<Autor> autores = autorService.getAllAuthors().stream()
                .filter(autor -> autor.getNombre().toLowerCase().contains(nombreAutor.toLowerCase()))
                .collect(Collectors.toList());

        Pattern pattern = Pattern.compile(".*[a-zA-ZáéíóúÁÉÍÓÚüÜñÑ].*");
        Matcher matcher = pattern.matcher(nombreAutor);

        if (autores.isEmpty() || !matcher.matches()) {
            System.out.println("No se encontró ningún autor con esa palabra clave o fué mal ingresado.");
        } else {
            System.out.println("\n--- Autores encontrados ---");
            autores.forEach(autor -> {
                System.out.println("ID: " + autor.getId());
                System.out.println("Nombre: " + autor.getNombre());
                System.out.println("Nacimiento: " + (autor.getNacimiento() != null ? autor.getNacimiento() : "N/A"));
                System.out.println("Fallecimiento: " + (autor.getMuerte() != null ? autor.getMuerte() : "N/A"));

                if (autor.getLibros() != null && !autor.getLibros().isEmpty()) {
                    System.out.println("  Libros: " + autor.getLibros().stream().map(Libro::getTitulo).collect(Collectors.joining(", ")));
                }
                System.out.println("----------------------------------------");
            });
        }

    }

    private void listarLibros() {
        System.out.println("\n--- Libros registrados en la base de datos ---");
        System.out.println("                 ---------------");
        List<Libro> libros = libroService.findAll();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados en la base de datos.");
        } else {
            libros.forEach(libro -> {
                System.out.println("        --------------------");
                System.out.println("ID: " + libro.getId());
                System.out.println("Título: " + libro.getTitulo());
                System.out.println("Autor: " + libro.getAutor());
                System.out.println("descargas: " + libro.getDescargas());

            });
        }

    }

    private void buscarLibro() {
        System.out.println("Ingrese al menos una Palabra Clave del Titulo");
        var tituloBuscado = teclado.nextLine();
        System.out.println("\nBuscando libros........(Esto puede demorar algún tiempo)");
        String json = consumoApi.obtenerDatos(URL_BASE + "?search=" + tituloBuscado.replace(" ", "+"));
        DatosRespuestaDTO respuestaDTO = conversor.obtenerDatos(json, DatosRespuestaDTO.class);

        List<LibroDTO> librosDTOS = respuestaDTO.getResults();

        if (librosDTOS == null || librosDTOS.isEmpty()) {
            System.out.println("---No se encontraron libros en la API para la búsqueda: '" + tituloBuscado + "'.---");
            return;
        }
        System.out.println("\n          ----------------------------------");
        System.out.println("\n--- Libros encontrados por la API Gutendex para '" + tituloBuscado + "' ---\n");

        librosDTOS.forEach(libroDTO -> {
            System.out.println("- Título: " + libroDTO.getTitulo()
                    + libroDTO.getDescargas()
                    + " (ID: " + libroDTO.getId_Libro() + ")");
        });
        System.out.println("-----------------------------------------------------------------\n");
        boolean libroGuardado = false;

        for (LibroDTO libroDTO : librosDTOS) {
            if (libroDTO.getTitulo().toLowerCase().contains(tituloBuscado.toLowerCase())) {
                System.out.println("Primera Coincidencia encontrada en los resultados de la API!");
                System.out.println("            ----------------------- ");
                System.out.println("Libro encontrado: '" + libroDTO.getTitulo() + "'");
                System.out.println("Idioma: " + (libroDTO.getIdiomas() != null && !libroDTO.getIdiomas().isEmpty() ? libroDTO.getIdiomas().get(0) : "Sin resultados"));
                System.out.println("Descargas: " + libroDTO.getDescargas());

                if (libroDTO.getAutores() != null && !libroDTO.getAutores().isEmpty()) {
                    System.out.println("Autor(es): " + libroDTO.getAutores().stream()
                            .map(AutorDTO::getNombre)
                            .collect(Collectors.joining(", ")));
                    System.out.println("--------------------------------");
                    System.out.println("❗ Si No es el libro que buscas y se encuentra en la Lista de arriba");
                    System.out.println("          has una busqueda mas específica -> OPCIÖN 2");
                }
                System.out.println("------------------------------------------");
                System.out.println("\n ---¿Qué quieres hacer con éste libro?---");
                System.out.println("              -----------");
                System.out.println("     1. Guardar en la base de datos✔");
                System.out.println("     2. Busqueda más específica❌");
                System.out.print("      Que quieres hacer con el Libro: ");

                String opcionAccion = teclado.nextLine();
                int opcionAccion1;
                try {
                    opcionAccion1 = Integer.parseInt(opcionAccion);
                } catch (NumberFormatException e) {
                    System.out.println("Opción inválida. Volviendo al menú principal.");
                    return;
                }

                if (opcionAccion1 == 1) {
                    Optional<Libro> libroExiste = libroService.findByTitle(libroDTO.getTitulo());
                    System.out.println("                 -------------");
                    if (libroExiste.isPresent()) {
                        System.out.println("Libro '" + libroExiste.get().getTitulo() + "' ya está registrado en la base de datos.");
                        libroGuardado = true;
                        break;
                    } else {
                        Libro libro = new Libro();
                        libro.setTitulo(libroDTO.getTitulo());
                        libro.setDescargas(libroDTO.getDescargas());

                        if (libroDTO.getIdiomas() != null && !libroDTO.getIdiomas().isEmpty()) {
                            libro.setIdioma(libroDTO.getIdiomas().get(0));
                        } else {
                            libro.setIdioma("Desconocido");
                        }
                        libro.setDescargas(libroDTO.getDescargas());

                        if (libroDTO.getAutores() != null && !libroDTO.getAutores().isEmpty()) {
                            List<Autor> autoresEntidad = libroDTO.getAutores().stream()
                                    .map(autorDTO -> {
                                        Optional<Autor> autorExistente = autorService.findAuthorByName(autorDTO.getNombre());
                                        if (autorExistente.isPresent()) {
                                            return autorExistente.get();
                                        } else {
                                            Autor nuevoAutor = new Autor();
                                            nuevoAutor.setNombre(autorDTO.getNombre());
                                            nuevoAutor.setNacimiento(autorDTO.getNacimiento());
                                            nuevoAutor.setMuerte(autorDTO.getMuerte());
                                            return autorRepository.save(nuevoAutor);
                                        }
                                    })
                                    .collect(Collectors.toList());

                            libro.setAutor(autoresEntidad.isEmpty() ? null : autoresEntidad.get(0));
                        } else {
                            System.out.println("Advertencia: Libro '" + libroDTO.getTitulo() + "' sin información de autor en la API.");
                            libro.setAutor(new Autor());
                        }
                        Libro libroGuardadoEnDB = libroService.saveBook(libro);

                        System.out.println(libroGuardadoEnDB);

                        System.out.println(" (ID_GUTENDEX: " + libroDTO.getId_Libro() + ") -> ' " + libro.getTitulo() + "' guardado en la base de datos.");
                        libroGuardado = true;
                        break;
                    }
                } else if (opcionAccion1 == 2) {
                    System.out.println("               ----------");
                    System.out.println("Volviendo al menú principal......");
                    return;
                } else {
                    System.out.println("-----------------");
                    System.out.println("Opción inválida. Volviendo al menú principal.");
                    return;
                }
            }
        }

    }
}
