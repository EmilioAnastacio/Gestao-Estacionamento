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
    public ResponseEntity<?> findByIdRequest(@PathVariable("id") final Long id){

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

    @PutMapping
    public ResponseEntity<?> editar(@RequestParam("id") final Long id, @RequestBody final Marca marca) {
        try {
            final Marca marcaBanco = this.marcaRepository.findById(id).orElse(null);
            if(marcaBanco == null || !marcaBanco.getId().equals(marca.getId())){
                throw new RuntimeException("O registro nao foi encontrado");
            }
            this.marcaRepository.save(marca);
            return ResponseEntity.ok("registro cadastrado");

        }catch (DataIntegrityViolationException e){
            return ResponseEntity.internalServerError().body("erro" + e.getCause().getCause().getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.internalServerError().body("erro" + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?>deleta(@RequestParam("id") final Long id){
        final Marca marcaBanco = this.marcaRepository.findById(id).orElse(null);

        List<Modelo> modelos = this.marcaRepository.findModeloByMarca(marcaBanco);

        if(modelos.isEmpty()){
            this.marcaService.deleta(marcaBanco);
        }else{
            marcaBanco.setAtivo(false);
            this.marcaRepository.save(marcaBanco);
        }
        return ResponseEntity.ok("Marca deletado");
    }

}
