package br.com.uniamerica.estacionamento.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Table(name = "configuracoes", schema = "public")
public class Configuracao extends AbstractEntity{

    @Getter@Setter
    @Column(name = "valor_hora")
    private BigDecimal valorHora;

    @Getter@Setter
    @Column(name = "valor_multa")
    private BigDecimal valorMinutoMulta;

    @Getter@Setter
    @Column(name = "inicio_expediente")
    private LocalTime inicioExpediente;

    @Getter@Setter
    @Column(name = "fim_expediente")
    private LocalTime fimExpediente;

    @Getter@Setter
    @Column(name = "tempo_desconto")
    private BigDecimal tempoDesconto;

    @Getter@Setter
    @Column(name = "gerar_desconto")
    private Boolean gerarDesconto;

    @Getter@Setter
    @Column(name = "necessario_desconto")
    private Integer necessarioDesconto;

    @Getter@Setter
    @Column(name = "vagas_moto")
    private int vagasMoto;

    @Getter@Setter
    @Column(name = "vagas_carro")
    private int vagasCarro;

    @Getter@Setter
    @Column(name = "vagas_va")
    private int vagasVa;


}

