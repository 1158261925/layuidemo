package com.example.demo.jpa;

import com.example.demo.pojo.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BookInfoJpa extends JpaRepository<Book,Long> {
    List<Book> findByBookAuthorLikeOrBookNameLike(String bookAuthor,String bookName);
}
