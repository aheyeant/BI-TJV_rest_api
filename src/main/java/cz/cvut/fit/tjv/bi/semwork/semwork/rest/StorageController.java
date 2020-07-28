package cz.cvut.fit.tjv.bi.semwork.semwork.rest;

import cz.cvut.fit.tjv.bi.semwork.semwork.application.StorageService;
import cz.cvut.fit.tjv.bi.semwork.semwork.data.model.StorageEntity;
import cz.cvut.fit.tjv.bi.semwork.semwork.rest.dto.StorageDto;
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
@ExposesResourceFor(StorageDto.class)
@RequestMapping(value = "/api/storages", produces = {MediaTypes.HAL_JSON_UTF8_VALUE, MediaTypes.ALPS_JSON_VALUE})
public class StorageController {

    static final ResourceAssemblerSupport<StorageEntity, StorageDto> ENTITY_TO_DTO
            = new ResourceAssemblerSupport<StorageEntity, StorageDto>(StorageController.class, StorageDto.class) {
        @Override
        public StorageDto toResource(StorageEntity e) {
            StorageDto p = new StorageDto(e.getName(), e.getLocation());
            p.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(StorageController.class).readByName(p.getName())).withSelfRel());
            return p;
        }
    };

    @Autowired
    private StorageService service;

    @Autowired
    private EntityLinks entityLinks;

    private Function<StorageDto, StorageEntity> dtoToEntity = d -> new StorageEntity(d.getName(), d.getLocation());

    @PostMapping
    public HttpEntity<StorageDto> create(@RequestBody StorageDto dto) {
        StorageEntity entitySaved = service.createOrUpdate(dtoToEntity.apply(dto));
        StorageDto result = ENTITY_TO_DTO.toResource(entitySaved);
        return ResponseEntity.created(entityLinks.linkForSingleResource(StorageDto.class, result.getName()).toUri()).body(result);
    }

    @GetMapping
    public List<StorageDto> readAll() {
        return ENTITY_TO_DTO.toResources(service.readAll());
    }

    @GetMapping("/{name}")
    public HttpEntity<StorageDto> readByName(@PathVariable String name) {
        Optional<StorageDto> resultOpt = service.readByName(name).map(ENTITY_TO_DTO::toResource);
        Link linkToList = entityLinks.linkToCollectionResource(StorageDto.class).withRel("list");
        if (resultOpt.isPresent()) {
            StorageDto result = resultOpt.get();
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
    public HttpEntity<StorageDto> updateOrCreate(@PathVariable String name, @RequestBody StorageDto dto) {
        dto.setName(name);
        StorageEntity entitySaved = service.createOrUpdate(dtoToEntity.apply(dto));
        StorageDto result = ENTITY_TO_DTO.toResource(entitySaved);
        Link linkToList = entityLinks.linkToCollectionResource(StorageDto.class).withRel("list");
        result.add(linkToList);
        return ResponseEntity
                .noContent()
                .header(HttpHeaders.LINK, result.getLink("self").toString())
                .header(HttpHeaders.LINK, linkToList.toString())
                .build();
    }

    @DeleteMapping("/{name}")
    public HttpEntity delete(@PathVariable String name) {
        Link linkToList = entityLinks.linkToCollectionResource(StorageDto.class).withRel("list");
        try {
            service.deleteByName(name);
            return ResponseEntity.noContent().header(HttpHeaders.LINK, linkToList.toString()).build();
        } catch (EmptyResultDataAccessException e) {
            return ResponseEntity.notFound().header(HttpHeaders.LINK, linkToList.toString()).build();
        }
    }
}
