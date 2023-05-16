package br.com.uniamerica.estacionamento.repository;

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ModeloRepository extends JpaRepository<Modelo, Long> {

    @Query("from Modelo where ativo = 1")
        public List<Modelo> findAtivo();

    @Query("from Veiculo where Modelo = :modelo")
        public List<Veiculo> findVeiculoByModelo(@Param("modelo") final Modelo modelo);
}
