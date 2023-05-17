package br.com.uniamerica.estacionamento.controller;


import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;
import br.com.uniamerica.estacionamento.service.ModeloService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepositoryExtensionsKt;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/api/modelo")
public class ModeloController {

    @Autowired
    private ModeloRepository modeloRepository;

    @Autowired
    private ModeloService modeloService;


    @GetMapping("/{id}")
    public ResponseEntity<?> findByIdRequest(@PathVariable("id") final Long id){

        final Modelo modelo = this.modeloRepository.findById(id).orElse(null);
        return modelo == null
                ? ResponseEntity.badRequest().body("Nenhum valor Encontrado")
                : ResponseEntity.ok(modelo);
    }

    @GetMapping("/lista")
    public ResponseEntity<?> listaCompleta(){
        return ResponseEntity.ok(this.modeloRepository.findAll());
    }

    @GetMapping("/ativo")
    public ResponseEntity<?> ListaAtivo(){
        return  ResponseEntity.ok(this.modeloRepository.findAtivo());
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody final Modelo modelo){
        try{
            this.modeloRepository.save(modelo);
            return ResponseEntity.ok("REGISTRO CADASTRADO COM SUCESSO");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("erro" +e.getMessage());
        }
    }

    @PutMapping
    public ResponseEntity<?> editar(@RequestParam("id") final Long id, @RequestBody final Modelo modelo) {
        try {
            final Modelo modeloBanco = this.modeloRepository.findById(id).orElse(null);
            if(modeloBanco == null || !modeloBanco.getId().equals(modelo.getId())){
                throw new RuntimeException("O registro nao foi encontrado");
            }
            this.modeloRepository.save(modelo);
            return ResponseEntity.ok("registro cadastrado");

        }catch (DataIntegrityViolationException e){
            return ResponseEntity.internalServerError().body("erro" + e.getCause().getCause().getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.internalServerError().body("erro" + e.getMessage());
        }
    }
    @DeleteMapping
    public ResponseEntity<?>deleta(@RequestParam("id") final Long id){
        final Modelo modeloBanco = this.modeloRepository.findById(id).orElse(null);
        List<Veiculo> veiculos = this.modeloRepository.findVeiculoByModelo(modeloBanco);
        if(veiculos == null){
            this.modeloRepository.delete(modeloBanco);
        }else{
            modeloBanco.setAtivo(false);
            this.modeloRepository.save(modeloBanco);
        }
        return ResponseEntity.ok("Modelo deletado");
    }


}
