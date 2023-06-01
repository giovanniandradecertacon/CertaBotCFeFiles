package br.com.certacon.certabotcfefiles.controller;

import br.com.certacon.certabotcfefiles.exception.MessageExceptionHandler;
import br.com.certacon.certabotcfefiles.models.CFeFileModel;
import br.com.certacon.certabotcfefiles.repositories.CFeFileRepository;
import br.com.certacon.certabotcfefiles.services.CFeFileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("cfeFile")
public class CFeFileController {
    private final CFeFileService fileService;
    private final CFeFileRepository cfeFileRepository;

    public CFeFileController(CFeFileService fileService, CFeFileRepository cfeFileRepository) {
        this.fileService = fileService;
        this.cfeFileRepository = cfeFileRepository;
    }

    @PostMapping
    @Operation(description = "Cria a entidade do Arquivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arquivo criado!", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CFeFileModel.class))}),
            @ApiResponse(responseCode = "400", description = "Informação inserida esta errada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "500", description = "Erro no servidor", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageExceptionHandler.class))})
    })
    public ResponseEntity<CFeFileModel> create(@RequestBody CFeFileModel entity) {
        CFeFileModel bot = fileService.saveOrUpdate(entity);
        return ResponseEntity.status(HttpStatus.OK).body(bot);
    }


    @GetMapping
    @Operation(description = "Busca todos Arquivos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arquivos(s) encontrado(s)!", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CFeFileModel.class))}),
            @ApiResponse(responseCode = "400", description = "Informação inserida está errada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "404", description = "Arquivos(s) não encontrado(s)", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "500", description = "Erro no servidor", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageExceptionHandler.class))})
    })
    public ResponseEntity<List<CFeFileModel>> getAll() {
        try {
            List<CFeFileModel> botList = cfeFileRepository.findAll();
            return ResponseEntity.status(HttpStatus.OK).body(botList);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @PutMapping
    @Operation(description = "Atualiza um Arquivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arquivo atualizado!", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = CFeFileModel.class))}),
            @ApiResponse(responseCode = "400", description = "Informação inserida esta errada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "404", description = "Arquivo não foi encontrado", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "500", description = "Erro no servidor", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageExceptionHandler.class))})
    })
    public ResponseEntity<CFeFileModel> update(@RequestBody CFeFileModel entity) {
        CFeFileModel response = fileService.saveOrUpdate(entity);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Deleta um Arquivo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Arquivo deletado!", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Informação inserida esta errada",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "404", description = "Arquivo não foi encontrado", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageExceptionHandler.class))}),
            @ApiResponse(responseCode = "500", description = "Erro no servidor", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = MessageExceptionHandler.class))})
    })
    public ResponseEntity delete(@PathVariable(value = "id") UUID id) {
        boolean entity = fileService.deleteFile(id);
        if (entity == Boolean.FALSE) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Arquivo não foi encontrado!");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Deletado com Sucesso");
    }

}
