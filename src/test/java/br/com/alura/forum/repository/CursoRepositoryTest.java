package br.com.alura.forum.repository;

import br.com.alura.forum.models.Curso;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
//Anotação que vai indicar que o banco não é em memória
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CursoRepositoryTest {

    @Autowired
    private CursoRepository repository;

    @Test
    public void deveriaCarregarUmCursoAoBuscarPeloSeuNome() {
        String nomeCurso = "HTML 5";
        Curso curso = repository.findByNome(nomeCurso);
        Assertions.assertNotNull(curso);
        Assertions.assertEquals(nomeCurso, curso.getNome());
    }

    @Test
    public void deveriaCarregarUmCursoAoBuscarPeloSeuNomeDois() {

        Curso curso1 = new Curso("Jk Senpai", "Como corrigir erro no teste");
        repository.save(curso1);

        String nomeCurso = "Jk Senpai";

        Curso curso2 = repository.findByNome(nomeCurso);
        Assertions.assertNotNull(curso2);
        Assertions.assertEquals(nomeCurso, curso2.getNome());
    }
}