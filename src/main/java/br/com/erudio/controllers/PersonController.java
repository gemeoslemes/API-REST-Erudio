package br.com.erudio.controllers;

import br.com.erudio.data.vo.v1.PersonVO;
import br.com.erudio.data.vo.v2.PersonVOV2;
import br.com.erudio.services.PersonService;
import br.com.erudio.util.MediaType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/person/v1")
@Tag(name = "People", description = "Endpoints for Managing People")
public class PersonController {

    @Autowired
    private PersonService personService;
    @CrossOrigin(origins = "http://localhost:8080")
    @GetMapping(value = "/{id}",
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Find a People", description = "Find a People",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PersonVO.class)
                    )),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not UanuThorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public PersonVO findById(@PathVariable(value = "id") Long id) {
        return personService.findById(id);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Finds all People", description = "Finds all People",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", content =
                    @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = PersonVO.class))
                    )),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Not Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content),
            }
    )
    public List<PersonVO> findAll() {
        return personService.findAll();
    }

    @CrossOrigin({"http://localhost:8080", "https://erudio.com.br"})
    @PostMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Adds a new Person by passing in a JSON, XML or YML representation of the person!",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "201", content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PersonVO.class)
                    )),
                    @ApiResponse(description = "Not Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<PersonVO> create(@RequestBody PersonVO person) {
        PersonVO personCreated = personService.create(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(personCreated);
    }

    @PostMapping(value = "v2",
            produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    public ResponseEntity<PersonVOV2> createV2(@RequestBody PersonVOV2 person) {
        PersonVOV2 personCreated = personService.createV2(person);
        return ResponseEntity.status(HttpStatus.CREATED).body(personCreated);
    }

    @PutMapping(produces = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML},
            consumes = {MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.APPLICATION_YML})
    @Operation(summary = "Updates a Person",
            description = "Updates a Person by passing in a JSON, XML or YML representation of the person!",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Sucess", responseCode = "200", content =
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PersonVO.class)
                    )),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Not Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public PersonVO update(@RequestBody PersonVO person) {
        return personService.update(person);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete a People", description = "Delete a People",
            tags = {"People"},
            responses = {
                    @ApiResponse(description = "Success", responseCode = "204", content = @Content),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Not Unauthorized", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<?> delete(@PathVariable Long id) {
        personService.delete(id);
        return ResponseEntity.noContent().build();
    }
}