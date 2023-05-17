package br.com.uniamerica.estacionamento.controller;
import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import br.com.uniamerica.estacionamento.service.MovimentacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping(value = "/api/movimentacao")
public class MovimentacaoController {

    @Autowired
    private MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private MovimentacaoService movimentacaoService;

    @GetMapping("/{id}")
    public ResponseEntity<?> findByIdRequest(@PathVariable("id") final Long id){

        final Movimentacao movimentacao = this.movimentacaoRepository.findById(id).orElse(null);
        return movimentacao == null
                ? ResponseEntity.badRequest().body("Nenhum valor Encontrado")
                : ResponseEntity.ok(movimentacao);
    }

    @GetMapping("/lista")
    public ResponseEntity<?> listaCompleta(){
        return ResponseEntity.ok(this.movimentacaoRepository.findAll());
    }

    @GetMapping("/ativo")
    public ResponseEntity<?> ListaAberto(){
        return  ResponseEntity.ok(this.movimentacaoRepository.findAberto());
    }

    @PostMapping
    public ResponseEntity<?> cadastrar(@RequestBody final Movimentacao movimentacao){
        try{
            this.movimentacaoRepository.save(movimentacao);
            return ResponseEntity.ok("REGISTRO CADASTRADO COM SUCESSO");
        }catch (Exception e){
            return ResponseEntity.badRequest().body("erro" +e.getStackTrace());
        }
    }

    @PutMapping
    public ResponseEntity<?> editar(@RequestParam("id") final Long id, @RequestBody final Movimentacao movimentacao) {
        try {
            final Movimentacao movimentacaobanco = this.movimentacaoRepository.findById(id).orElse(null);
            if(movimentacaobanco == null || !movimentacaobanco.getId().equals(movimentacao.getId())){
                throw new RuntimeException("O registro nao foi encontrado");
            }
            this.movimentacaoRepository.save(movimentacao);
            return ResponseEntity.ok("registro cadastrado");

        }catch (DataIntegrityViolationException e){
            return ResponseEntity.internalServerError().body("erro" + e.getCause().getCause().getMessage());
        }catch (RuntimeException e){
            return ResponseEntity.internalServerError().body("erro" + e.getMessage());
        }
    }

    @PutMapping("/hora")
    public ResponseEntity<?> horaFinal(@RequestParam("id") final Long id){
        try {
            this.movimentacaoService.horaFinal(id);
            return ResponseEntity.ok("Registro alterado");

        }catch (RuntimeException e){
            return ResponseEntity.internalServerError().body("erro" + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<?>deleta(@RequestParam("id") final Long id){
        final Movimentacao movimentacaoBanco = this.movimentacaoRepository.findById(id).orElse(null);
            movimentacaoBanco.setAtivo(false);
            this.movimentacaoService.deleta(movimentacaoBanco);
        return ResponseEntity.ok("Movimentacao deletado");
    }
}
