package com.fatec.arthur.atv_t2.repositories;

import com.fatec.arthur.atv_t2.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
}
