package com.alura.com.Challenge_De_Literatura.DTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DatosRespuestaDTO {

    private List<LibroDTO> results;

    public List<LibroDTO> getResults() { return results;}

    public void setResults(List<LibroDTO> results){this.results = results;}
}
