package br.com.uniamerica.estacionamento.entity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "veiculos",schema = "public")
public class Veiculo extends AbstractEntity{

    @Getter@Setter
    @Column(name = "placa", length = 20, nullable = false,unique = true)
    private String placa;

    @Getter@Setter
    @ManyToOne
    @JoinColumn(name = "marca")
    private Marca marca;

    @Getter@Setter
    @ManyToOne
    @JoinColumn(name = "modelo")
    private Modelo modelo;

    @Enumerated(EnumType.STRING)
    @Getter@Setter
    @Column(name = "cor", length = 20, nullable = false)
    private Cor cor;

    @Enumerated(EnumType.STRING)
    @Getter@Setter
    @Column(name = "tipo", length = 20, nullable = false)
    private Tipo tipo;

    @Getter@Setter
    @Column(name = "ano",nullable = false)
    private Integer ano;

}
