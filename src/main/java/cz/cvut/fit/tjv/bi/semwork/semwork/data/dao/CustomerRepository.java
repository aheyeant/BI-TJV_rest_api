package cz.cvut.fit.tjv.bi.semwork.semwork.data.dao;

import cz.cvut.fit.tjv.bi.semwork.semwork.data.model.BookEntity;
import cz.cvut.fit.tjv.bi.semwork.semwork.data.model.CustomerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerEntity, String> {
    //Collection<BookEntity> findAllBooksByCustomerId(int id);
}
