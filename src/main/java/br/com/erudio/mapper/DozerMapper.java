package br.com.erudio.mapper;

//import com.github.dozermapper.core.DozerBeanMapperBuilder;
//import com.github.dozermapper.core.Mapper;
import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.model.book.Book;
import br.com.erudio.model.person.Person;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class DozerMapper {

    // private static Mapper mapper = DozerBeanMapperBuilder.buildDefault();
    private static ModelMapper mapper = new ModelMapper();

    static {
        mapper.createTypeMap(
                Person.class,
                PersonVO.class
        ).addMapping(Person::getId, PersonVO::setKey);
        mapper.createTypeMap(
                PersonVO.class,
                Person.class
        ).addMapping(PersonVO::getKey, Person::setId);
        mapper.createTypeMap(
                Book.class,
                BookVO.class
        ).addMapping(Book::getId, BookVO::setKey);
        mapper.createTypeMap(
                BookVO.class,
                Book.class
        ).addMapping(BookVO::getKey, Book::setId);
    }

    public static <O, D> D parseObject(O origin, Class<D> destination) {
        return mapper.map(origin, destination);
    }

    public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
        List<D> destinationObjects = new ArrayList<>();
        for (O o: origin) {
            destinationObjects.add(mapper.map(o, destination));
        }
        return destinationObjects;
    }

}
