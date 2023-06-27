package br.com.uniamerica.estacionamento.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "movimentacoes",schema = "public")
public class Movimentacao extends AbstractEntity{

    @Getter@Setter
    @ManyToOne
    @JoinColumn(name = "veiculos", nullable = false)
    private Veiculo veiculo;

    @Getter@Setter
    @ManyToOne
    @JoinColumn(name = "condutor", nullable = false, unique = true)
    public Condutor condutor;

    @Getter@Setter
    @Column(name = "entrada", nullable = false)
    public LocalDateTime entrada;

    @Getter@Setter
    @Column(name = "saida")
    public LocalDateTime saida;

    @Getter@Setter
    @Column(name = "tempo_hora")
    public Integer tempoHora;

    @Getter@Setter
    @Column(name = "tempo_minuto")
    public Integer tempoMinuto;

    @Getter@Setter
    @Column(name = "tempo_desconto")
    public LocalTime tempoDesc;

    @Getter@Setter
    @Column(name = "valor_desconto")
    public BigDecimal valorDeconto;

    @Getter@Setter
    @Column(name = "valor_multa")
    public BigDecimal valorMulta;

    @Getter@Setter
    @Column(name = "valor_hora_total")
    public BigDecimal valorHoraTotal;

    @Getter@Setter
    @Column(name = "valor_hora_Mmulta")
    public BigDecimal valorHoraMulta;

    @Getter@Setter
    @Column(name = "tempo_multa")
    public BigDecimal tempoMulta;

    @Getter@Setter
    @Column(name = "valor_total")
    private BigDecimal valorTotal;

}
