package com.alura.com.Challenge_De_Literatura.Service;

import com.alura.com.Challenge_De_Literatura.Reposiroty.IConvertirDatos;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

@Component
public class ConvertirDatos implements IConvertirDatos {
    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public <T> T obtenerDatos(String json, Class<T> clase){
        try{

            return mapper.readValue(json, clase);

        }catch (JsonProcessingException e){

            System.err.println("Error al convertir JSON: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al procesar JSON", e);

        }
    }
}
