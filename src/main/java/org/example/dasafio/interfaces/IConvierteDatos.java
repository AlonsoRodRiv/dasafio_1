package org.example.dasafio.interfaces;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase);
}
