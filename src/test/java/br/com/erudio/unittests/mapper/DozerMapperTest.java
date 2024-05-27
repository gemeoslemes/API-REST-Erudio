package br.com.erudio.unittests.mapper;

import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.unittests.mapper.mocks.BookMock;
import br.com.erudio.unittests.mapper.mocks.PersonMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DozerMapperTest {
    PersonMock inputObject;

    BookMock inputBookObject;

    @BeforeEach
    public void setUp() {

        inputObject = new PersonMock();
        inputBookObject = new BookMock();
    }

    @Test
    public void parseEntityToVOTest() {
        PersonVO output = DozerMapper.parseObject(inputObject.mockEntity(), PersonVO.class);
        assertEquals(Long.valueOf(0L), output.getKey());
        assertEquals("First Name: 0", output.getFirstName());
        assertEquals("Last Name: 0", output.getLastName());
        assertEquals("Address: 0", output.getAddress());
        assertEquals("Male", output.getGender());
    }

    @Test
    public void parseEntityToVOBookTest() {
        LocalDate localDate = LocalDate.of(2022, 11, 11);
        Date date = Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));
        BookVO output = DozerMapper.parseObject(inputBookObject.mockEntity(), BookVO.class);
        assertEquals(Long.valueOf(0L), output.getKey());
        assertEquals("Title : 0", output.getTitle());
        assertEquals("Name Author: 0", output.getAuthor());
        assertEquals(date, output.getLaunchDate());
        assertEquals(0.0, output.getPrice());
    }

    @Test
    public void parseEntityListTOVOListBookTest() {
        List<BookVO> outputList = DozerMapper.parseListObjects(inputBookObject.mockListEntity(), BookVO.class);

        LocalDate localDate = LocalDate.of(2022, 11, 11);
        Date date = Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));

        BookVO personVOZero = outputList.get(0);
        BookVO output = DozerMapper.parseObject(inputBookObject.mockEntity(), BookVO.class);
        assertEquals(Long.valueOf(0L), personVOZero.getKey());
        assertEquals("Title : 0", personVOZero.getTitle());
        assertEquals("Name Author: 0", personVOZero.getAuthor());
        assertEquals(date, personVOZero.getLaunchDate());
        assertEquals(0.0, personVOZero.getPrice());

        BookVO personVOSeven = outputList.get(7);

        assertEquals(Long.valueOf(7L), personVOSeven.getKey());
        assertEquals("Title : 7", personVOSeven.getTitle());
        assertEquals("Name Author: 7", personVOSeven.getAuthor());
        assertEquals(date, personVOSeven.getLaunchDate());
        assertEquals(7.0, personVOSeven.getPrice());

        BookVO personVOFour = outputList.get(4);

        assertEquals(Long.valueOf(4L), personVOFour.getKey());
        assertEquals("Title : 4", personVOFour.getTitle());
        assertEquals("Name Author: 4", personVOFour.getAuthor());
        assertEquals(date, personVOFour.getLaunchDate());
        assertEquals(4.0, personVOFour.getPrice());
    }

    @Test
    public void parseEntityListToVOListTest() {
        List<PersonVO> outputList = DozerMapper.parseListObjects(inputObject.mockEntityList(), PersonVO.class);
        PersonVO personVOZero = outputList.get(0);

        assertEquals(Long.valueOf(0L), personVOZero.getKey());
        assertEquals("First Name: 0", personVOZero.getFirstName());
        assertEquals("Last Name: 0", personVOZero.getLastName());
        assertEquals("Address: 0", personVOZero.getAddress());
        assertEquals("Male", personVOZero.getGender());

        PersonVO personVOSeven = outputList.get(7);

        assertEquals(Long.valueOf(7L), personVOSeven.getKey());
        assertEquals("First Name: 7", personVOSeven.getFirstName());
        assertEquals("Last Name: 7", personVOSeven.getLastName());
        assertEquals("Address: 7", personVOSeven.getAddress());
        assertEquals("Female", personVOSeven.getGender());

        PersonVO personVOTwelve = outputList.get(12);

        assertEquals(Long.valueOf(12L), personVOTwelve.getKey());
        assertEquals("First Name: 12", personVOTwelve.getFirstName());
        assertEquals("Last Name: 12", personVOTwelve.getLastName());
        assertEquals("Address: 12", personVOTwelve.getAddress());
        assertEquals("Male", personVOTwelve.getGender());
    }


}