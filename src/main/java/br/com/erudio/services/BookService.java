package br.com.erudio.services;

import br.com.erudio.controllers.BookController;
import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.execeptions.RequiredObjectIsNotNullException;
import br.com.erudio.execeptions.ResourceNotFoundException;
import br.com.erudio.mapper.DozerMapper;
import br.com.erudio.model.book.Book;
import br.com.erudio.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    public BookVO create(BookVO bookVO) {
        var entity = DozerMapper.parseObject(bookVO, Book.class);
        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public BookVO findById(Long id) {
        var entity = repository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("No records found for this ID!"));
        var vo = DozerMapper.parseObject(entity, BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(id)).withSelfRel());
        return vo;
    }

    public List<BookVO> findAll() {
        var books = DozerMapper.parseListObjects(repository.findAll(), BookVO.class);
        books
                .stream()
                .forEach(b -> b.add(linkTo(methodOn(BookController.class).findById(b.getKey())).withSelfRel()));
        return books;
    }

    public BookVO update(BookVO bookVO) {
        if (bookVO == null) throw new RequiredObjectIsNotNullException();

        var entity = repository.findById(bookVO.getKey()).orElseThrow(() ->
                new ResourceNotFoundException("No records found for this ID!"));

        entity.setAuthor(bookVO.getAuthor());
        entity.setPrice(bookVO.getPrice());
        entity.setLaunchDate(bookVO.getLaunchDate());
        entity.setTitle(bookVO.getTitle());


        var vo = DozerMapper.parseObject(repository.save(entity), BookVO.class);
        vo.add(linkTo(methodOn(BookController.class).findById(vo.getKey())).withSelfRel());
        return vo;
    }

    public void delete(Long id) {
        var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID!"));
        repository.delete(entity);
    }
}
