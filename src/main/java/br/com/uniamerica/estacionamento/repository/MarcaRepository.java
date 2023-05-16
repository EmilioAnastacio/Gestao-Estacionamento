package br.com.uniamerica.estacionamento.repository;

import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MarcaRepository extends JpaRepository<Marca, Long> {

    @Query("from Marca where ativo = 1")
    public List<Marca> findAtivo();

    @Query("from Modelo where Marca = :marca")
    public List<Modelo> findModeloByMarca(@Param("marca") final Marca marca);
}
