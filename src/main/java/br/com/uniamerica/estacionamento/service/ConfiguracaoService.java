package br.com.uniamerica.estacionamento.service;
import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.repository.ConfiguracaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class ConfiguracaoService {

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;

    public void configuracao(Configuracao configuracao){

        Assert.isTrue(configuracao.getValorHora() != null,"valor da hora nao informado");
        Assert.isTrue(configuracao.getValorMinutoMulta() != null,"valor da multa p/ minuto nao informado");
        Assert.isTrue(configuracao.getInicioExpediente() != null,"inicio do expediente nao informado");
        Assert.isTrue(configuracao.getFimExpediente() != null,"fim do expediente nao informado");
        Assert.isTrue(configuracao.getTempoDesconto() != null,"tempo desconto nao informado");

        configuracaoRepository.save(configuracao);
    }

    public void editar(Configuracao configuracao){

        final Configuracao configuracaoBanco = this.configuracaoRepository.findById(configuracao.getId()).orElse(null);

        Assert.isTrue(configuracao.getValorHora() != null, "valor hora nao foi colocado");
        Assert.isTrue(configuracao.getValorMinutoMulta() != null,"valor da multa p/ minuto nao foi colocado");
        Assert.isTrue(configuracao.getInicioExpediente() != null,"inicio do expediente nao foi colocado");
        Assert.isTrue(configuracao.getFimExpediente() != null,"fim do expediente nao foi colocado");
        Assert.isTrue(configuracao.getTempoDesconto() != null,"tempo desconto nao foi colocado");

        Assert.isTrue(configuracaoBanco == null || !configuracaoBanco.getId().equals(configuracao.getId()), "nao deu pra indentificar");

        configuracaoRepository.save(configuracao);
    }
}
