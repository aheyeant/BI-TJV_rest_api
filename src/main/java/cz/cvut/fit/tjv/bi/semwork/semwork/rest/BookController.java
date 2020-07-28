package cz.cvut.fit.tjv.bi.semwork.semwork.rest;


import cz.cvut.fit.tjv.bi.semwork.semwork.application.BookService;
import cz.cvut.fit.tjv.bi.semwork.semwork.data.model.BookEntity;
import cz.cvut.fit.tjv.bi.semwork.semwork.rest.dto.BookDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@RestController
@ExposesResourceFor(BookDto.class)
@RequestMapping(value = "/api/books", produces = {MediaTypes.HAL_JSON_UTF8_VALUE, MediaTypes.ALPS_JSON_VALUE})
public class BookController {

    static final ResourceAssemblerSupport<BookEntity, BookDto> ENTITY_TO_DTO
            = new ResourceAssemblerSupport<BookEntity, BookDto>(BookController.class, BookDto.class) {
        @Override
        public BookDto toResource(BookEntity e) {
            BookDto p = new BookDto(e.getName(), e.getPrice());
            p.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(BookController.class).readByName(p.getName())).withSelfRel());
            return p;
        }
    };

    @Autowired
    private BookService service;

    @Autowired
    private EntityLinks entityLinks;

    private Function<BookDto, BookEntity> dtoToEntity = d -> new BookEntity(d.getName(), d.getPrice());

    @PostMapping
    public HttpEntity<BookDto> create(@RequestBody BookDto dto) {
        BookEntity entitySaved = service.createOrUpdate(dtoToEntity.apply(dto));
        BookDto result = ENTITY_TO_DTO.toResource(entitySaved);
        return ResponseEntity.created(entityLinks.linkForSingleResource(BookDto.class, result.getName()).toUri()).body(result);
    }

    @GetMapping
    public List<BookDto> readAll() {
        return ENTITY_TO_DTO.toResources(service.readAll());
    }

    @GetMapping("/{name}")
    public HttpEntity<BookDto> readByName(@PathVariable String name) {
        Optional<BookDto> resultOpt = service.readByName(name).map(ENTITY_TO_DTO::toResource);
        Link linkToList = entityLinks.linkToCollectionResource(BookDto.class).withRel("list");
        if (resultOpt.isPresent()) {
            BookDto result = resultOpt.get();
            result.add(linkToList);
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.LINK, linkToList.toString())
                    .header(HttpHeaders.LINK, result.getLink("self").toString())
                    .body(result);
        } else
            return ResponseEntity.notFound().header(HttpHeaders.LINK, linkToList.toString()).build();
    }

    @PutMapping("/{name}")
    public HttpEntity<BookDto> updateOrCreate(@PathVariable String name, @RequestBody BookDto dto) {
        dto.setName(name);
        BookEntity entitySaved = service.createOrUpdate(dtoToEntity.apply(dto));
        BookDto result = ENTITY_TO_DTO.toResource(entitySaved);
        Link linkToList = entityLinks.linkToCollectionResource(BookDto.class).withRel("list");
        result.add(linkToList);
        return ResponseEntity
                .noContent()
                .header(HttpHeaders.LINK, result.getLink("self").toString())
                .header(HttpHeaders.LINK, linkToList.toString())
                .build();
    }

    @DeleteMapping("/{name}")
    public HttpEntity delete(@PathVariable String name) {
        Link linkToList = entityLinks.linkToCollectionResource(BookDto.class).withRel("list");
        try {
            service.deleteByName(name);
            return ResponseEntity.noContent().header(HttpHeaders.LINK, linkToList.toString()).build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().header(HttpHeaders.LINK, linkToList.toString()).build();
        }
    }
}
