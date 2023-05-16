package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class MovimentacaoService {

    @Autowired
    private  MovimentacaoRepository movimentacaoRepository;

    @Transactional
    public void cadastrar(Movimentacao movi){

        Assert.isTrue(movi.getVeiculo() != null, "Veiculo nao informado");
        Assert.isTrue(movi.getCondutor() != null, "Condutor nao informada");
        Assert.isTrue(movi.getEntrada() != null, "Entradada nao informado");
        Assert.isTrue(movi.getSaida() != null, "Saida nao informada");

        movimentacaoRepository.save(movi);

    }

    @Transactional
    public void editar(final Long id, Movimentacao movi){

        Assert.isTrue(movi.getVeiculo() != null, "Veiculo nao informado");
        Assert.isTrue(movi.getCondutor() != null, "Condutor nao informada");
        Assert.isTrue(movi.getEntrada() != null, "Entradada nao informado");
        Assert.isTrue(movi.getSaida() != null, "Saida nao informada");

        final Movimentacao moviBanco = this.movimentacaoRepository.findById(id).orElse(null);
        Assert.isTrue(moviBanco != null, "nao foi possivel encontrar o registro");
        Assert.isTrue(!moviBanco.getId().equals(movi.getId()), "nao foi possivel encontrar o registro");

        movimentacaoRepository.save(movi);
    }

    @Transactional
    public void deleta(final Movimentacao movi){

        final Movimentacao moviBanco = this.movimentacaoRepository.findById(movi.getId()).orElse(null);

        moviBanco.setAtivo(Boolean.FALSE);
        this.movimentacaoRepository.save(movi);
    }

}
