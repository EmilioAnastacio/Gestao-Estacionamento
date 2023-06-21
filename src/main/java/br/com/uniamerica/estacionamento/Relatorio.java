package br.com.uniamerica.estacionamento;

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;


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
    private BigDecimal quantidadeDesconto;

    @Getter
    @Setter
    private BigDecimal valorPagar;
    @Getter
    @Setter
    private BigDecimal valorDesconto;

    public Relatorio(LocalDateTime entrada, LocalDateTime saida, Condutor condutor, Veiculo veiculo, Integer quantidadeHoras, BigDecimal quantidadeDesconto, BigDecimal valorPagar, BigDecimal valorDesconto) {
        this.entrada = entrada;
        this.saida = saida;
        this.condutor = condutor;
        this.veiculo = veiculo;
        this.quantidadeHoras = quantidadeHoras;
        this.quantidadeDesconto = quantidadeDesconto;
        this.valorPagar = valorPagar;
        this.valorDesconto = valorDesconto;
    }
}
