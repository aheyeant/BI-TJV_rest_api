package cz.cvut.fit.tjv.bi.semwork.semwork.application;


import cz.cvut.fit.tjv.bi.semwork.semwork.data.dao.StorageRepository;
import cz.cvut.fit.tjv.bi.semwork.semwork.data.model.BookEntity;
import cz.cvut.fit.tjv.bi.semwork.semwork.data.model.StorageEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
public class StorageService {
    @Autowired
    private StorageRepository repository;

    @Transactional
    public StorageEntity createOrUpdate(StorageEntity storageEntity) {
        return repository.save(storageEntity);
    }

    @Transactional(readOnly = true)
    public List<StorageEntity> readAll() {
        return repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<StorageEntity> readByName(String name) {
        return repository.findById(name);
    }

    @Transactional
    public void deleteByName(String name) {
        repository.deleteById(name);
    }

/*    @Transactional(readOnly = true)
    public Collection<BookEntity> readAllBooksById(int id) {
        return repository.findAllBooksByStorageId(id);
    }*/
}
