package cz.cvut.fit.tjv.bi.semwork.semwork.application;


import cz.cvut.fit.tjv.bi.semwork.semwork.data.dao.CustomerRepository;
import cz.cvut.fit.tjv.bi.semwork.semwork.data.model.BookEntity;
import cz.cvut.fit.tjv.bi.semwork.semwork.data.model.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    @Autowired
    private CustomerRepository repository;

    @Transactional
    public CustomerEntity createOrUpdate(CustomerEntity customerEntity) {
        return repository.save(customerEntity);
    }

    @Transactional(readOnly = true)
    public List<CustomerEntity> readAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<CustomerEntity> readByLogin(String login) {
        return repository.findById(login);
    }

    @Transactional
    public void deleteByLogin(String login) {
        repository.deleteById(login);
    }

    /*@Transactional(readOnly = true)
    public Collection<BookEntity> readAllBooksById(int id) {
        return repository.findAllBooksByCustomerId(id);
    }*/
}
