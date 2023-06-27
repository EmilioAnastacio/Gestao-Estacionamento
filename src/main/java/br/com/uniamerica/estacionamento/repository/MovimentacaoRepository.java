package br.com.uniamerica.estacionamento.repository;

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MovimentacaoRepository extends JpaRepository<Movimentacao, Long> {

    @Query("from Movimentacao where saida = null")
    public List<Movimentacao> findAberto();

    @Query("from Movimentacao where veiculo = :veiculo AND saida = null AND id != :id")
    public List<Movimentacao> findVeiculo(@Param("veiculo")final Veiculo veiculo, @Param("id")final Long id);

}
