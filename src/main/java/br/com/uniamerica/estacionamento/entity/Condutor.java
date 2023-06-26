package br.com.uniamerica.estacionamento.entity;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalTime;

@Entity
@Table(name = "condutores", schema = "public")
public class Condutor extends AbstractEntity{

    @Getter@Setter
    @Column(name = "nome",nullable = false,length = 110)
    private String nomeCondutor;

    @Getter@Setter
    @Column(name = "cpf",nullable = false,unique = false,length = 15)
    private String cpf;

    @Getter@Setter
    @Column(name = "telefone", nullable = false,unique = true,length = 17)
    private String telefone;

    @Getter@Setter
    @Column(name = "tempo_gasto")
    private BigDecimal tempoPago;

    @Getter@Setter
    @Column(name = "tempo_desconto")
    private BigDecimal tempoDesconto;

}
