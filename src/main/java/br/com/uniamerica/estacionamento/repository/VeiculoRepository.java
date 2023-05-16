package br.com.uniamerica.estacionamento.repository;

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {

    @Query("from Veiculo where ativo = 1")
    public List<Veiculo> findAtivo();

    @Query("from Movimentacao where Veiculo = :veiculo")
    public List<Movimentacao> findMovimentacaoByVeiculo(@Param("veiculo") final Veiculo veiculo);

    @Query("from Veiculo where placa = :placa")
    public List<Veiculo> findByPlaca(@Param("placa") final String placa);
}
