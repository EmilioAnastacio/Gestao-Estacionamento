package br.com.uniamerica.estacionamento.service;
import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.repository.ConfiguracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
public class ConfiguracaoService {

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;

    @Transactional(rollbackFor = Exception.class)
    public void cadastrar(final Configuracao configuracao){

        Assert.isTrue(configuracao.getValorHora() != null,"valor da hora nao informado");
        Assert.isTrue(configuracao.getValorMinutoMulta() != null,"valor da multa p/ minuto nao informado");
        Assert.isTrue(configuracao.getInicioExpediente() != null,"inicio do expediente nao informado");
        Assert.isTrue(configuracao.getFimExpediente() != null,"fim do expediente nao informado");
        Assert.isTrue(configuracao.getTempoDesconto() != null,"tempo desconto nao informado");

        configuracaoRepository.save(configuracao);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editar(final Long id, Configuracao configuracao){

        final Configuracao configuracaoBanco = this.configuracaoRepository.findById(id).orElse(null);

        Assert.isTrue(configuracao.getValorHora() != null, "valor hora nao foi colocado");
        Assert.isTrue(configuracao.getValorMinutoMulta() != null,"valor da multa p/ minuto nao foi colocado");
        Assert.isTrue(configuracao.getInicioExpediente() != null,"inicio do expediente nao foi colocado");
        Assert.isTrue(configuracao.getFimExpediente() != null,"fim do expediente nao foi colocado");
        Assert.isTrue(configuracao.getTempoDesconto() != null,"tempo desconto nao foi colocado");

        Assert.isTrue(configuracaoBanco != null || !configuracaoBanco.getId().equals(id), "nao deu pra indentificar");

        configuracaoRepository.save(configuracao);
    }
}
