package br.com.erudio.junittest.mockito.services;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.execeptions.RequiredObjectIsNotNullException;
import br.com.erudio.model.person.Person;
import br.com.erudio.repositories.PersonRepository;
import br.com.erudio.services.PersonService;
import br.com.erudio.unittests.mapper.mocks.PersonMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class PersonServiceTest {
    PersonMock input;

    @InjectMocks
    private PersonService service;

    @Mock
    PersonRepository repository;

    @BeforeEach
    void setUpMock() throws Exception {
        input = new PersonMock();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Testing the search for all people in the list")
    void findAll() {
        List<Person> list = input.mockEntityList();

        when(repository.findAll()).thenReturn(list);

        var people = service.findAll();

        assertNotNull(people);
        assertEquals(14, people.size());

        var personOne = people.get(1);
        assertNotNull(personOne);
        assertNotNull(personOne.getKey());
        assertNotNull(personOne.getLinks());
        assertTrue(personOne.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Address: 1", personOne.getAddress());
        assertEquals("Last Name: 1", personOne.getLastName());
        assertEquals("First Name: 1", personOne.getFirstName());
        assertEquals("Female", personOne.getGender());

        var personFour = people.get(4);
        assertNotNull(personFour);
        assertNotNull(personFour.getKey());
        assertNotNull(personFour.getLinks());
        assertTrue(personFour.toString().contains("links: [</api/person/v1/4>;rel=\"self\"]"));
        assertEquals("Address: 4", personFour.getAddress());
        assertEquals("Last Name: 4", personFour.getLastName());
        assertEquals("First Name: 4", personFour.getFirstName());
        assertEquals("Male", personFour.getGender());

        var personSeven = people.get(7);
        assertNotNull(personSeven);
        assertNotNull(personSeven.getKey());
        assertNotNull(personSeven.getLinks());
        assertTrue(personSeven.toString().contains("links: [</api/person/v1/7>;rel=\"self\"]"));
        assertEquals("Address: 7", personSeven.getAddress());
        assertEquals("Last Name: 7", personSeven.getLastName());
        assertEquals("First Name: 7", personSeven.getFirstName());
        assertEquals("Female", personSeven.getGender());
    }

    @Test
    @DisplayName("Checks that the link is not null and is correct")
    void testFindById() {
        Person person = input.mockEntity(1);
        person.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(person));

        var result = service.findById(1l);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Address: 1", result.getAddress());
        assertEquals("Last Name: 1", result.getLastName());
        assertEquals("First Name: 1", result.getFirstName());
        assertEquals("Female", result.getGender());
    }

    @Test
    @DisplayName("Checks the person creation and wheter it contains all the correct informaction")
    void testCreate() {
        Person entity = input.mockEntity(1);
        Person persisted = entity;
        persisted.setId(1L);

        PersonVO vo = input.mockVO(1);
        vo.setKey(1L);

        when(repository.save(entity)).thenReturn(persisted);

        var result = service.create(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Address: 1", result.getAddress());
        assertEquals("Last Name: 1", result.getLastName());
        assertEquals("First Name: 1", result.getFirstName());
        assertEquals("Female", result.getGender());
    }
    @Test
    @DisplayName("Testing method create when data is null if exception is called")
    void testCreateCase2() {
        Exception exception = assertThrows(RequiredObjectIsNotNullException.class, () -> {
            service.create(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Checks if the update method has all the correct information")
    void testUpdate() {
        Person entity = input.mockEntity(1);
        Person persisted = entity;
        persisted.setId(1L);

        PersonVO vo = input.mockVO(1);
        vo.setKey(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(persisted);

        var result = service.update(vo);
        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getLinks());
        assertTrue(result.toString().contains("links: [</api/person/v1/1>;rel=\"self\"]"));
        assertEquals("Address: 1", result.getAddress());
        assertEquals("Last Name: 1", result.getLastName());
        assertEquals("First Name: 1", result.getFirstName());
        assertEquals("Female", result.getGender());
    }

    @Test
    @DisplayName("Testing method update when data is null if exception is called")
    void testUpdateCase2() {
        Exception exception = assertThrows(RequiredObjectIsNotNullException.class, () -> {
            service.update(null);
        });

        String expectedMessage = "It is not allowed to persist a null object!";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Checks if the person was deleted successfully")
    void TestDelete() {
        Person entity = input.mockEntity(1);
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);
    }
}