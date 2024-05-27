package br.com.erudio.controllers;

import br.com.erudio.data.vo.v1.BookVO;
import br.com.erudio.services.BookService;
import br.com.erudio.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/books/v1")
public class BookController {

    @Autowired
    private BookService service;
    @PostMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML, MediaType.APPLICATION_XML},
                 consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Adds a new Book by passing in a JSON, XML or YML representation of the book!",
            tags = {"Books"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "201", content =
                    @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = BookVO.class)
                    )),
                    @ApiResponse(description = "Not Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<BookVO> createBook(@RequestBody BookVO bookVO) {
        BookVO bookVOCreated = service.create(bookVO);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookVOCreated);
    }

    @GetMapping(value = "/{id}",
                produces = {MediaType.APPLICATION_YML, MediaType.APPLICATION_JSON, MediaType.APPLICATION_JSON})
    @Operation(summary = "Find a Book", description = "Find a Book",
        tags = {"Books"},
        responses = {
            @ApiResponse(description = "Success", responseCode = "200", content =
            @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = BookVO.class)
            )),
            @ApiResponse(description = "Not Unauthorized", responseCode = "401", content = @Content),
            @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
            @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
            @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
        }
    )
    public BookVO findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML, MediaType.APPLICATION_XML})
    @Operation(summary = "Finds all books", description = "Finds all Books",
            tags = {"Books"},
            responses = {
                @ApiResponse(description = "Success", responseCode = "200", content =
                @Content(
                        mediaType = "application/json",
                        schema = @Schema(implementation = BookVO.class)
                )),
                @ApiResponse(description = "Not Unauthorized", responseCode = "401", content = @Content),
                @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public List<BookVO> findAll(){
        return service.findAll();
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
                consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_YML, MediaType.APPLICATION_XML})
    @Operation(summary = "Updates  a new Book by passing in a JSON, XML or YML representation of the book!",
                tags = {"Books"},
                responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BookVO.class)
                    )),
                    @ApiResponse(description = "Not Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
                }
    )
    public BookVO update(@RequestBody BookVO bookVO) {
        return service.update(bookVO);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a Book", description = "Delete a Book",
        tags = {"Books"},
         responses = {
                    @ApiResponse(description = "Success", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
         }
    )
    public ResponseEntity<?> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
