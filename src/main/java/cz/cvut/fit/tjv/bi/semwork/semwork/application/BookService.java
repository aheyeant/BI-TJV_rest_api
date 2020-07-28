package cz.cvut.fit.tjv.bi.semwork.semwork.application;


import cz.cvut.fit.tjv.bi.semwork.semwork.data.dao.BookRepository;
import cz.cvut.fit.tjv.bi.semwork.semwork.data.model.BookEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {
    @Autowired
    private BookRepository repository;

    @Transactional
    public BookEntity createOrUpdate(BookEntity bookEntity) {
        return repository.save(bookEntity);
    }

    @Transactional(readOnly = true)
    public List<BookEntity> readAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<BookEntity> readByName(String name) {
        return repository.findById(name);
    }

    @Transactional
    public void deleteByName(String name) {
        repository.deleteById(name);
    }
}
