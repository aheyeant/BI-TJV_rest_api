package cz.cvut.fit.tjv.bi.semwork.semwork.rest;


import cz.cvut.fit.tjv.bi.semwork.semwork.application.CustomerService;
import cz.cvut.fit.tjv.bi.semwork.semwork.data.model.CustomerEntity;
import cz.cvut.fit.tjv.bi.semwork.semwork.rest.dto.BookDto;
import cz.cvut.fit.tjv.bi.semwork.semwork.rest.dto.CustomerDto;
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
@ExposesResourceFor(CustomerDto.class)
@RequestMapping(value = "/api/customers", produces = {MediaTypes.HAL_JSON_UTF8_VALUE, MediaTypes.ALPS_JSON_VALUE})
public class CustomerController {

    static final ResourceAssemblerSupport<CustomerEntity, CustomerDto> ENTITY_TO_DTO
            = new ResourceAssemblerSupport<CustomerEntity, CustomerDto>(CustomerController.class, CustomerDto.class) {
        @Override
        public CustomerDto toResource(CustomerEntity e) {
            CustomerDto p = new CustomerDto(e.getLogin(), e.getName(), e.getSurname());
            p.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(CustomerController.class).readByLogin(p.getLogin())).withSelfRel());
            return p;
        }
    };

    @Autowired
    private CustomerService service;

    @Autowired
    private EntityLinks entityLinks;

    private Function<CustomerDto, CustomerEntity> dtoToEntity = d -> new CustomerEntity(d.getLogin(), d.getName(), d.getSurname());

    @PostMapping
    public HttpEntity<CustomerDto> create(@RequestBody CustomerDto dto) {
        CustomerEntity entitySaved = service.createOrUpdate(dtoToEntity.apply(dto));
        CustomerDto result = ENTITY_TO_DTO.toResource(entitySaved);
        return ResponseEntity.created(entityLinks.linkForSingleResource(CustomerDto.class, result.getName()).toUri()).body(result);
    }

    @GetMapping
    public List<CustomerDto> readAll() {
        return ENTITY_TO_DTO.toResources(service.readAll());
    }

    @GetMapping("/{login}")
    public HttpEntity<CustomerDto> readByLogin(@PathVariable String login) {
        Optional<CustomerDto> resultOpt = service.readByLogin(login).map(ENTITY_TO_DTO::toResource);
        Link linkToList = entityLinks.linkToCollectionResource(CustomerDto.class).withRel("list");
        if (resultOpt.isPresent()) {
            CustomerDto result = resultOpt.get();
            result.add(linkToList);
            return ResponseEntity
                    .ok()
                    .header(HttpHeaders.LINK, linkToList.toString())
                    .header(HttpHeaders.LINK, result.getLink("self").toString())
                    .body(result);
        } else
            return ResponseEntity.notFound().header(HttpHeaders.LINK, linkToList.toString()).build();
    }

    @PutMapping("/{login}")
    public HttpEntity<CustomerDto> updateOrCreate(@PathVariable String login, @RequestBody CustomerDto dto) {
        dto.setName(login);
        CustomerEntity entitySaved = service.createOrUpdate(dtoToEntity.apply(dto));
        CustomerDto result = ENTITY_TO_DTO.toResource(entitySaved);
        Link linkToList = entityLinks.linkToCollectionResource(CustomerDto.class).withRel("list");
        result.add(linkToList);
        return ResponseEntity
                .noContent()
                .header(HttpHeaders.LINK, result.getLink("self").toString())
                .header(HttpHeaders.LINK, linkToList.toString())
                .build();
    }

    @DeleteMapping("/{login}")
    public HttpEntity delete(@PathVariable String login) {
        Link linkToList = entityLinks.linkToCollectionResource(CustomerDto.class).withRel("list");
        try {
            service.deleteByLogin(login);
            return ResponseEntity.noContent().header(HttpHeaders.LINK, linkToList.toString()).build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().header(HttpHeaders.LINK, linkToList.toString()).build();
        }
    }
}
