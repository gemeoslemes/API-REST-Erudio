package br.com.erudio.mapper.custom;

import br.com.erudio.data.vo.v2.PersonVOV2;
import br.com.erudio.model.person.Person;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PersonMapper {

    public PersonVOV2 convertEntityToVO(Person person) {
        PersonVOV2 vo = new PersonVOV2();
        vo.setId(person.getId());
        vo.setFirstName(person.getFirstName());
        vo.setAddress(person.getAddress());
        vo.setBirthDay(new Date());
        vo.setGender(person.getGender());
        vo.setLastName(person.getLastName());
        return vo;
    }

    public Person convertVOToEntity(PersonVOV2 vo) {
        Person entity = new Person();
        entity.setId(vo.getId());
        entity.setFirstName(vo.getFirstName());
        entity.setAddress(vo.getAddress());
        //entity.setBirthDay(new Date());
        entity.setGender(vo.getGender());
        entity.setLastName(vo.getLastName());
        return entity;
    }
}
