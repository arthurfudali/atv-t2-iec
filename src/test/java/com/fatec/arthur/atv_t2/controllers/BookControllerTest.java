package com.fatec.arthur.atv_t2.controllers;

import com.fatec.arthur.atv_t2.models.Book;
import com.fatec.arthur.atv_t2.repositories.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository repo;

    @BeforeEach
    void setup() {
        repo.deleteAll();
        repo.save(new Book("Clean Code", "Robert C. Martin"));
        repo.save(new Book("Domain-Driven Design", "Eric Evans"));
    }

    @Test
    void shouldReturnAllBooks() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldReturnBookById() throws Exception {
        Book b = repo.findAll().getFirst();
        mockMvc.perform(get("/books/" + b.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(b.getTitle()));
    }

    @Test
    void shouldCreateBook() throws Exception {
        String json = """
                {
                "title": "Refactoring",
                "author": "Martin Fowler"
                }
                """;

        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON).content(json))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.title").value("Refactoring"));
    }
}
