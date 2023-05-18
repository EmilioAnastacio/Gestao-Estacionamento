package br.com.uniamerica.estacionamento;

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;


public class Relatorio {
    
    @Getter
    @Setter
    private LocalDateTime entrada;
    @Getter
    @Setter
    private LocalDateTime saida;

    @Getter
    @Setter
    private Condutor condutor;
    @Getter
    @Setter
    private Veiculo veiculo;

    @Getter
    @Setter
    private Integer quantidadeHoras;
    @Getter
    @Setter
    private Integer quantidadeDesconto;

    @Getter
    @Setter
    private Integer valorPagar;
    @Getter
    @Setter
    private Integer valorDesconto;
}
