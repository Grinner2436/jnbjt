package com.grinner.game.jnbjt.dao.jpa;

import com.grinner.game.jnbjt.domain.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book,Integer> {
    Book findByName(String name);
}
