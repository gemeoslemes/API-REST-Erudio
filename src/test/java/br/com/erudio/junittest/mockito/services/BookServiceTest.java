package br.com.erudio.junittest.mockito.services;

import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.model.book.Book;
import br.com.erudio.repositories.BookRepository;
import br.com.erudio.services.BookService;
import br.com.erudio.unittests.mapper.mocks.BookMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    BookMock bookMock;

    @InjectMocks
    private BookService service;

    @Mock
    private BookRepository repository;

    @BeforeEach
    void setUpMock() throws Exception {
        bookMock = new BookMock();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Test creating the book successfully")
    void create() {
        Book entity = bookMock.mockEntity(1);
        Book pesisted = entity;
        pesisted.setId(1L);

        BookVO vo = bookMock.mockVO(1);
        vo.setKey(1L);

        when(repository.save(entity)).thenReturn(pesisted);

        var result = service.create(vo);
        LocalDate localDate = LocalDate.of(2022, 11, 11);
        Date date = Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getPrice());
        assertNotNull(result.getLaunchDate());
        assertNotNull(result.getAuthor());
        assertNotNull(result.getTitle());
        assertTrue(result.toString().contains("links: [</api/books/v1/1>;rel=\"self\"]"));
        assertEquals("Name Author: 1", result.getAuthor());
        assertEquals("Title : 1", result.getTitle());
        assertEquals(1.0, result.getPrice());
        assertEquals(date, result.getLaunchDate());

    }

    @Test
    @DisplayName("Test find book by id successfully ")
    void findById() {
        Book book = bookMock.mockEntity(1);
        book.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(book));

        var result = service.findById(1L);
        LocalDate localDate = LocalDate.of(2022, 11, 11);
        Date date = Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getPrice());
        assertNotNull(result.getLaunchDate());
        assertNotNull(result.getAuthor());
        assertNotNull(result.getTitle());
        assertTrue(result.toString().contains("links: [</api/books/v1/1>;rel=\"self\"]"));
        assertEquals("Name Author: 1", result.getAuthor());
        assertEquals("Title : 1", result.getTitle());
        assertEquals(1.0, result.getPrice());
        assertEquals(date, result.getLaunchDate());
    }

    @Test
    @DisplayName("Test by reading all books successfully")
    void findAll() {
        List<Book> list = bookMock.mockListEntity();

        when(repository.findAll()).thenReturn(list);

        var book = service.findAll();

        assertNotNull(book);
        assertEquals(14, book.size());

        var bookOne = book.get(1);

        LocalDate localDate = LocalDate.of(2022, 11, 11);
        Date date = Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));

        assertNotNull(bookOne);
        assertNotNull(bookOne.getKey());
        assertNotNull(bookOne.getPrice());
        assertNotNull(bookOne.getLaunchDate());
        assertNotNull(bookOne.getAuthor());
        assertNotNull(bookOne.getTitle());
        assertTrue(bookOne.toString().contains("links: [</api/books/v1/1>;rel=\"self\"]"));
        assertEquals("Name Author: 1", bookOne.getAuthor());
        assertEquals("Title : 1", bookOne.getTitle());
        assertEquals(1.0, bookOne.getPrice());
        assertEquals(date, bookOne.getLaunchDate());

        var bookFour = book.get(4);

        assertNotNull(bookFour);
        assertNotNull(bookFour.getKey());
        assertNotNull(bookFour.getPrice());
        assertNotNull(bookFour.getLaunchDate());
        assertNotNull(bookFour.getAuthor());
        assertNotNull(bookFour.getTitle());
        assertTrue(bookFour.toString().contains("links: [</api/books/v1/4>;rel=\"self\"]"));
        assertEquals("Name Author: 4", bookFour.getAuthor());
        assertEquals("Title : 4", bookFour.getTitle());
        assertEquals(4.0, bookFour.getPrice());
        assertEquals(date, bookFour.getLaunchDate());
    }

    @Test
    @DisplayName("Test updating book successfully")
    void update() {
        Book entity = bookMock.mockEntity(1);
        Book pesisted = entity;
        pesisted.setId(1L);

        BookVO vo = bookMock.mockVO(1);
        vo.setKey(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));
        when(repository.save(entity)).thenReturn(pesisted);


        var result = service.update(vo);
        LocalDate localDate = LocalDate.of(2022, 11, 11);
        Date date = Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));

        assertNotNull(result);
        assertNotNull(result.getKey());
        assertNotNull(result.getPrice());
        assertNotNull(result.getLaunchDate());
        assertNotNull(result.getAuthor());
        assertNotNull(result.getTitle());
        assertTrue(result.toString().contains("links: [</api/books/v1/1>;rel=\"self\"]"));
        assertEquals("Name Author: 1", result.getAuthor());
        assertEquals("Title : 1", result.getTitle());
        assertEquals(1.0, result.getPrice());
        assertEquals(date, result.getLaunchDate());
    }

    @Test
    @DisplayName("Test deleting Book successfully")
    void delete() {
        Book entity = bookMock.mockEntity(1);
        entity.setId(1L);

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        service.delete(1L);
    }
}