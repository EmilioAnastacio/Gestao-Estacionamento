package br.com.uniamerica.estacionamento.controller;
import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import br.com.uniamerica.estacionamento.repository.MarcaRepository;
import br.com.uniamerica.estacionamento.service.CondutorService;
import br.com.uniamerica.estacionamento.service.MarcaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/api/marca")
public class MarcaController {

    @Autowired
    private MarcaRepository marcaRepository;
    @Autowired
    private MarcaService marcaService;



    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable("id") final Long id){

        final Marca marca = this.marcaRepository.findById(id).orElse(null);
        return marca == null
                ? ResponseEntity.badRequest().body("Nenhum valor Encontrado")
                : ResponseEntity.ok(marca);
    }

    @GetMapping("/lista")
    public ResponseEntity<?> listaCompleta(){
        return ResponseEntity.ok(this.marcaRepository.findAll());
    }

    @GetMapping("/ativo")
    public ResponseEntity<?> ListaAtivo(){
        return  ResponseEntity.ok(this.marcaRepository.findAtivo());
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody final Marca marca){
        try{
            this.marcaService.cadastrar(marca);
            return ResponseEntity.ok("REGISTRO CADASTRADO COM SUCESSO");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("erro" +e.getStackTrace());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(
            @PathVariable("id") final Long id,
            @RequestBody final Marca marca) {
        try {
            this.marcaService.editar(id, marca);
            return ResponseEntity.ok("Registro atualizado com sucesso. ");
        }catch (DataIntegrityViolationException e){
            return ResponseEntity.internalServerError().body("erro" + e.getCause().getCause().getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.internalServerError().body("erro" + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?>deleta(@PathVariable("id") final Long id){
        try {
            this.marcaService.excluir(id);
            return ResponseEntity.ok("Registro excluido com sucesso.");
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body("Error: " + e.getMessage());
        }
    }

}
