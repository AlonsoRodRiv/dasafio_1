package org.example.dasafio.controller;

import org.example.dasafio.converter.ConvierteDatosImpl;
import org.example.dasafio.entity.DatosLibros;
import org.example.dasafio.entity.LibroDTO;
import org.example.dasafio.service.ConsumoAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/api")
public class LibrosController {

    @Autowired
    private ConsumoAPI consumoAPI;

    @GetMapping("/libros")
    public String obtenerLibros(Model model) {
        String url = "https://gutendex.com/books/";
        String respuesta = consumoAPI.obtenerDatos(url);

        // Convertir la respuesta JSON a un objeto utilizando la clase ConvierteDatosImpl
        ConvierteDatosImpl conversor = new ConvierteDatosImpl();
        // Aquí debes definir la clase que representa los datos de la respuesta
        LibroDTO libros = conversor.obtenerDatos(respuesta, LibroDTO.class);

        model.addAttribute("libros", libros.resultados());
        return "viewLibros";
    }

    @GetMapping(value = "/libros/top10", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<DatosLibros> getTop10Libros() {
        String url = "https://gutendex.com/books/";
        String respuesta = consumoAPI.obtenerDatos(url);

        ConvierteDatosImpl conversor = new ConvierteDatosImpl();
        LibroDTO datos = conversor.obtenerDatos(respuesta, LibroDTO.class);

        return datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
    @GetMapping(value = "/libros/titulo", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<DatosLibros> getTituloLibros() {
        String url = "https://gutendex.com/books/";
        String respuesta = consumoAPI.obtenerDatos(url);

        ConvierteDatosImpl conversor = new ConvierteDatosImpl();
        LibroDTO datos = conversor.obtenerDatos(respuesta, LibroDTO.class);

        return datos.resultados().stream()
                .sorted(Comparator.comparing(DatosLibros::titulo))
                .collect(Collectors.toList());
    }
    @GetMapping(value = "/libros/{nombre}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<DatosLibros> getLibro(@PathVariable String nombre) {
        String url = "https://gutendex.com/books/?search=";
        String respuesta = consumoAPI.obtenerDatos(url + nombre.replace(" ", "+"));
        ConvierteDatosImpl conversor = new ConvierteDatosImpl();
        LibroDTO datos = conversor.obtenerDatos(respuesta, LibroDTO.class);

        return datos.resultados().stream()
                .filter(l -> l.titulo().toUpperCase().contains(nombre.toUpperCase()))
                .collect(Collectors.toList());
    }

}
