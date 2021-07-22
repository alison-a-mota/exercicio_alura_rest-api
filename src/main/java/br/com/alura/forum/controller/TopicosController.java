package br.com.alura.forum.controller;

import br.com.alura.forum.DTO.AtualizacaoTopicoForm;
import br.com.alura.forum.DTO.DetalhesDoTopicoDTO;
import br.com.alura.forum.DTO.TopicoDTO;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.models.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

    @Autowired
    private TopicoRepository topicoRepository;
    @Autowired
    private CursoRepository cursoRepository;

    @GetMapping
    @Cacheable("listaDeTopicos")
    public Page<TopicoDTO> lista(@RequestParam(required = false) String nomeCurso, Pageable paginacao) {

        if (nomeCurso == null) {
            Page<Topico> topicos = topicoRepository.findAll(paginacao);
            return TopicoDTO.converter(topicos);
        }
        //?cursoNome=Spring+Boot
        Page<Topico> topicos = topicoRepository.findByCursoNome(nomeCurso, paginacao);
        return TopicoDTO.converter(topicos);
    }

    @GetMapping("/{id}")
    public DetalhesDoTopicoDTO detalhar(@PathVariable Long id) {
        Topico topico = topicoRepository.getById(id);
        return new DetalhesDoTopicoDTO(topico);

    }

    @PostMapping
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoDTO> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
        Topico topico = form.toModel(cursoRepository);
        topicoRepository.save(topico);

        URI uri1 = UriComponentsBuilder.fromPath("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        return ResponseEntity.created(uri1).body(new TopicoDTO(topico));

        // Esse código foi o que o instrutor fez e não compila
        // URI uri = UriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();
        // return ResponseEntity.created(uri).body(new TopicoDTO(topico));

    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<TopicoDTO> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
        Topico topico = form.atualizar(id, topicoRepository);

        return ResponseEntity.ok(new TopicoDTO(topico));
    }

    @DeleteMapping("/{id}")
    @CacheEvict(value = "listaDeTopicos", allEntries = true)
    public ResponseEntity<String> deletar(@PathVariable Long id) {
        topicoRepository.deleteAllById(Collections.singleton(id));
        return ResponseEntity.ok().body("Tópico deletado.");
    }
}
