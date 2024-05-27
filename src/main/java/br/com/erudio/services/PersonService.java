package br.com.erudio.services;

import br.com.erudio.controllers.PersonController;
import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.data.vo.v2.PersonVOV2;
import br.com.erudio.execeptions.RequiredObjectIsNotNullException;
import br.com.erudio.execeptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.mapper.custom.PersonMapper;
import br.com.erudio.model.person.Person;
import br.com.erudio.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import java.util.List;
import java.util.logging.Logger;

@Service
public class PersonService {
    @Autowired
    private PersonRepository repository;

    @Autowired
    private PersonMapper mapper;

    private Logger logger = Logger.getLogger(PersonService.class.getName());

    public List<PersonVO>   findAll() {
        logger.info("Finding all people!");
        var persons = DozerMapper.parseListObjects(repository.findAll(), PersonVO.class);
        persons
                .stream()
                .forEach(p -> p.add(linkTo(methodOn(PersonController.class).findById(p.getKey())).withSelfRel()));
        return persons;
    }

    public PersonVO findById(Long id) {
        logger.info("Finding one person!");
        var entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this ID!"));
        var vo =  DozerMapper.parseObject(entity, PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(id)).withSelfRel());
        return vo;
    }

    public PersonVO create(PersonVO person) {
        if (person == null) throw new RequiredObjectIsNotNullException();

        logger.info("Creating one person");

        var entity = DozerMapper.parseObject(person, Person.class);
        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }
    public PersonVOV2 createV2(PersonVOV2 personVOV2) {
        logger.info("Creating one person with V2");

        var entity = mapper.convertVOToEntity(personVOV2);
        var vo = mapper.convertEntityToVO(repository.save(entity));
        return vo;
    }

    public PersonVO update(PersonVO person) {
        if (person == null) throw new RequiredObjectIsNotNullException();

        logger.info("Updating one person");

        var entity = repository.findById(person.getKey()).orElseThrow(
                () -> new ResourceNotFoundException("No records found for this ID!"));

        entity.setFirstName(person.getFirstName());
        entity.setLastName(person.getLastName());
        entity.setAddress(person.getAddress());
        entity.setGender(person.getGender());

        var vo = DozerMapper.parseObject(repository.save(entity), PersonVO.class);
        vo.add(linkTo(methodOn(PersonController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        logger.info("Deleting one person");

        var entity = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("No records for this ID!"));
        repository.delete(entity);
    }

}
