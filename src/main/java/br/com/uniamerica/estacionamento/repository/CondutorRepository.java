package br.com.uniamerica.estacionamento.repository;

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CondutorRepository extends JpaRepository<Condutor, Long> {

    @Query("from Condutor where ativo = 1")
    public List<Condutor> findAtivo();

    @Query("from Movimentacao where Condutor = :condutor")
    public List<Movimentacao> findMovimentacaoByCondutor(@Param("condutor") final Condutor condutor);

    @Query("from Condutor where cpf = :cpf")
    public List<Condutor> findByCpf(@Param("cpf") final String cpf);

    @Query("from Condutor where cpf = :cpf AND id != :id")
    public List<Condutor> findByCpfEditar(@Param("cpf") final String cpf, @Param("id")final Long id);

    @Query("from Condutor where telefone = :telefone")
    public List<Condutor> findByTelefone(@Param("telefone") final String telefone);

    @Query("from Condutor where telefone = :telefone AND id != :id")
    public List<Condutor> findByTelefoneEditar(@Param("telefone")final String telefone, @Param("id")final Long id);
}
