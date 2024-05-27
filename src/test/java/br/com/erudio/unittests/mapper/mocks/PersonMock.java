package br.com.erudio.unittests.mapper.mocks;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.model.person.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonMock {

    public Person mockEntity() {
        return mockEntity(0);
    }

    public PersonVO mockVO() {
        return mockVO(0);
    }

    public List<Person> mockEntityList() {
        List<Person> persons = new  ArrayList<>();
        for (int i = 0; i < 14; i++) {
            persons.add(mockEntity(i));
        }
        return persons;
    }

    public List<PersonVO> mockVoList() {
        List<PersonVO> personsVO = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            personsVO.add(mockVO(i));
        }
        return personsVO;
    }

    public Person mockEntity(Integer number) {
        Person person = new Person();
        person.setFirstName("First Name: " + number);
        person.setLastName("Last Name: " + number);
        person.setAddress("Address: " + number);
        person.setGender(((number % 2) == 0) ? "Male" : "Female");
        person.setId(number.longValue());
        return person;
    }
    public PersonVO mockVO (Integer number) {
        PersonVO person = new PersonVO();
        person.setFirstName("First Name: " + number);
        person.setLastName("Last Name: " + number);
        person.setAddress("Address: " + number);
        person.setGender(((number % 2) == 0) ? "Male" : "Female");
        person.setKey(number.longValue());
        return person;
    }


}
