package br.com.uniamerica.estacionamento.service;
import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.repository.CondutorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class CondutorService {

    @Autowired
    private CondutorRepository condutorRepository;

    @Transactional(rollbackFor = Exception.class)
    public void cadastrar(final Condutor condutor){

        Assert.isTrue(condutor.getNomeCondutor() != null, "nome nao informado");
        Assert.isTrue(condutor.getCpf() != null, "cpf nao informado");
        Assert.isTrue(condutor.getTelefone() !=null, "telefone nao informado");

        String cpf = "^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$";
        Assert.isTrue(condutor.getCpf().matches(cpf), "Error cpf com mascara errada");

        String numero = "^\\(\\d{2}\\)\\d{9}";
        Assert.isTrue(condutor.getTelefone().matches(numero), "Erro no numero");

        Assert.isTrue(this.condutorRepository.findByCpf(condutor.getCpf()).isEmpty(), "cpf ja existe boy");
        Assert.isTrue(this.condutorRepository.findByTelefone(condutor.getTelefone()).isEmpty(), "numero ja existe boy");
        this.condutorRepository.save(condutor);

    }

    @Transactional
    public void editar(final Long id, final Condutor condutor){

        final Condutor condutorBanco = this.condutorRepository.findById(id).orElse(null);

        Assert.isTrue(condutor.getNomeCondutor() != null, "nome nao foi colocado");
        Assert.isTrue(condutor.getCpf() != null, "cpf nao foi colocado");
        Assert.isTrue(condutor.getTelefone() != null, "telefone nao foi colocado");

        String cpf = "^\\d{3}\\.\\d{3}\\.\\d{3}\\-\\d{2}$";
        Assert.isTrue(condutor.getCpf().matches(cpf), "Erro no cpf");

        String numero = "^\\(\\d{2}\\)\\d{9}";
        Assert.isTrue(condutor.getTelefone().matches(numero), "Erro no numero");

        Assert.isTrue(condutorBanco != null || !condutorBanco.getId().equals(condutor.getId()), "nao deu pra indentificar");

        this.condutorRepository.save(condutor);
    }


    @Transactional
    public void deleta(final Condutor condutor){
        
        final Condutor condutorBanco = this.condutorRepository.findById(condutor.getId()).orElse(null);

        List<Movimentacao> movimentacaoLista = this.condutorRepository.findMovimentacaoByCondutor(condutorBanco);

        System.out.println(movimentacaoLista);

        if(movimentacaoLista.isEmpty()){
            this.condutorRepository.delete(condutorBanco);
        }else {
            condutorBanco.setAtivo(false);
            this.condutorRepository.save(condutor);
        }
    }
}
