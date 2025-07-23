package com.alura.com.Challenge_De_Literatura.Reposiroty;

public interface IConvertirDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
