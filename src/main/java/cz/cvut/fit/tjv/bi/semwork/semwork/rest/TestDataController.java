package cz.cvut.fit.tjv.bi.semwork.semwork.rest;


import cz.cvut.fit.tjv.bi.semwork.semwork.application.BookService;
import cz.cvut.fit.tjv.bi.semwork.semwork.application.CustomerService;
import cz.cvut.fit.tjv.bi.semwork.semwork.application.StorageService;
import cz.cvut.fit.tjv.bi.semwork.semwork.data.model.BookEntity;
import cz.cvut.fit.tjv.bi.semwork.semwork.data.model.CustomerEntity;
import cz.cvut.fit.tjv.bi.semwork.semwork.data.model.StorageEntity;
import cz.cvut.fit.tjv.bi.semwork.semwork.rest.dto.BookDto;
import cz.cvut.fit.tjv.bi.semwork.semwork.rest.dto.CustomerDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedList;
import java.util.List;

@RestController
@ExposesResourceFor(BookDto.class)
@RequestMapping(value = "/api/test/data", produces = {MediaTypes.HAL_JSON_UTF8_VALUE, MediaTypes.ALPS_JSON_VALUE})
public class TestDataController {

    @Autowired
    BookService bookService;

    @Autowired
    CustomerService customerService;

    @Autowired
    StorageService storageService;

    @Autowired
    private EntityLinks entityLinks;

    @PostMapping("/create")
    public HttpEntity createTestData() {
        Link linkToList = entityLinks.linkToCollectionResource(CustomerDto.class).withRel("list");
        if (!customerService.readAll().isEmpty()) return ResponseEntity.badRequest().header(HttpHeaders.LINK, linkToList.toString()).build();
        if (!storageService.readAll().isEmpty()) return ResponseEntity.badRequest().header(HttpHeaders.LINK, linkToList.toString()).build();
        if (!bookService.readAll().isEmpty()) return ResponseEntity.badRequest().header(HttpHeaders.LINK, linkToList.toString()).build();
        CustomerEntity customer1 = new CustomerEntity("lw", "Lamar", "Walter");
        CustomerEntity customer2 = new CustomerEntity("ce", "Casey", "Estes");
        CustomerEntity customer3 = new CustomerEntity("hc", "Holly", "Curtis");
        CustomerEntity customer4 = new CustomerEntity("zs", "Zariah", "Salazar");

        BookEntity book1 = new BookEntity("Python", 10);
        BookEntity book2 = new BookEntity("Belorussian_cuisine", 10);
        BookEntity book3 = new BookEntity("African_technologies", 10);
        BookEntity book4 = new BookEntity("Who_to_survive_without_O2", 10);
        BookEntity book5 = new BookEntity("Best_design_ideas_for_WC", 10);

        StorageEntity storage1 = new StorageEntity("storage_1", "Belasrus");
        StorageEntity storage2 = new StorageEntity("storage_2", "USSR");

        customerService.createOrUpdate(customer1);
        customerService.createOrUpdate(customer2);
        customerService.createOrUpdate(customer3);
        customerService.createOrUpdate(customer4);

        bookService.createOrUpdate(book1);
        bookService.createOrUpdate(book2);
        bookService.createOrUpdate(book3);
        bookService.createOrUpdate(book4);
        bookService.createOrUpdate(book5);

        storageService.createOrUpdate(storage1);
        storageService.createOrUpdate(storage2);

        return ResponseEntity.ok().header(HttpHeaders.LINK, linkToList.toString()).build();
    }

    @DeleteMapping("/delete")
    public HttpEntity deleteAllData() {
        Link linkToList = entityLinks.linkToCollectionResource(CustomerDto.class).withRel("list");
        try {
            List<CustomerEntity> customerEntities = customerService.readAll();
            for (CustomerEntity customerEntity: customerEntities) {
                customerService.deleteByLogin(customerEntity.getLogin());
            }
            List<StorageEntity> storageEntities = storageService.readAll();
            for (StorageEntity storageEntity: storageEntities) {
                storageService.deleteByName(storageEntity.getName());
            }
            List<BookEntity> bookEntities = bookService.readAll();
            for (BookEntity bookEntity: bookEntities) {
                bookService.deleteByName(bookEntity.getName());
            }
            return ResponseEntity.noContent().header(HttpHeaders.LINK, linkToList.toString()).build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().header(HttpHeaders.LINK, linkToList.toString()).build();
        }
    }
}
