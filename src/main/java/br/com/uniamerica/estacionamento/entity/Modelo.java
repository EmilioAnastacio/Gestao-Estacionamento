package br.com.uniamerica.estacionamento.entity;
import jakarta.persistence.*;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "modelos", schema = "public")
public class Modelo extends AbstractEntity{

    @Getter@Setter
    @Column(name = "nome", length = 100,nullable = false,unique = true)
    private String nome;

    @Getter@Setter
    @ManyToOne
    @JoinColumn(name = "marca",nullable = false)
    private Marca marca;

}
