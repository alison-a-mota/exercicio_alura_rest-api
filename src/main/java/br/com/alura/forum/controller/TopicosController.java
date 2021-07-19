package br.com.alura.forum.controller;

import br.com.alura.forum.DTO.TopicoDTO;
import br.com.alura.forum.models.Curso;
import br.com.alura.forum.models.Topico;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController

public class TopicosController {

    @RequestMapping("/topicos")
    public List<TopicoDTO> lista() {
        Topico topico = new Topico("Dúvida 2", "Dúvida com Spring", new Curso("Spring", "Programação"));
        return TopicoDTO.converter(Arrays.asList(topico, topico, topico));
    }
}