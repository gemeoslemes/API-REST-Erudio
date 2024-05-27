package br.com.erudio.unittests.mapper.mocks;

import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.model.book.Book;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BookMock {
    public Book mockEntity() {
        return mockEntity(0);
    }

    public BookVO mockVO() {
        return mockVO(0);
    }

    public List<BookVO> mockListVO() {
        List<BookVO> bookVOList = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            bookVOList.add(mockVO(i));
        }
        return bookVOList;
    }

    public List<Book> mockListEntity() {
        List<Book> bookList = new ArrayList<>();
        for (int i = 0; i < 14; i++) {
            bookList.add(mockEntity(i));
        }
        return bookList;
    }

    public Book mockEntity(Integer number) {
        Book book = new Book();
        book.setId(number.longValue());
        book.setTitle("Title : " + number);
        LocalDate localDate = LocalDate.of(2022, 11, 11);
        Date date = Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));
        book.setLaunchDate(date);
        book.setAuthor("Name Author: " + number);
        book.setPrice(number.doubleValue());
        return book;
    }

    public BookVO mockVO(Integer number) {
        BookVO book = new BookVO();
        book.setKey(number.longValue());
        book.setTitle("Title : " + number);
        LocalDate localDate = LocalDate.of(2022, 11, 11);
        Date date = Date.from(localDate.atStartOfDay().toInstant(ZoneOffset.UTC));
        book.setLaunchDate(date);
        book.setAuthor("Name Author: " + number);
        book.setPrice(number.doubleValue());
        return book;
    }

}
