package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.entity.Configuracao;
import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.repository.CondutorRepository;
import br.com.uniamerica.estacionamento.repository.ConfiguracaoRepository;
import br.com.uniamerica.estacionamento.repository.MovimentacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class MovimentacaoService {

    @Autowired
    private  MovimentacaoRepository movimentacaoRepository;

    @Autowired
    private ConfiguracaoRepository configuracaoRepository;

    @Autowired
    private CondutorRepository condutorRepository;

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
    public void horaFinal(final Long id){
        final Movimentacao moviBanco = this.movimentacaoRepository.findById(id).orElse(null);
        Assert.isTrue(moviBanco != null, "movimentacao nao encontrada");

        final LocalDateTime saida = LocalDateTime.now();
        Duration duracaoHora = Duration.between(moviBanco.getEntrada(), saida);

        final Configuracao configuracao = this.configuracaoRepository.findById(1L).orElse(null);
        Assert.isTrue(configuracao != null, "configuracao nao existe");

        final Condutor condutor = this.condutorRepository.findById(moviBanco.getCondutor().getId()).orElse(null);
        Assert.isTrue(condutor != null, "condutor nao existe");

        moviBanco.setTempoMinuto(duracaoHora.toMinutesPart());
        moviBanco.setTempoHora(duracaoHora.toHoursPart());
        moviBanco.setSaida(saida);

        final BigDecimal hora = BigDecimal.valueOf(duracaoHora.toHoursPart());
        final BigDecimal minuto = BigDecimal.valueOf(duracaoHora.toMinutesPart()).divide(BigDecimal.valueOf(60),2, RoundingMode.HALF_EVEN);

        BigDecimal precos = configuracao.getValorHora().multiply(hora).add(configuracao.getValorHora().multiply(minuto));

        final BigDecimal tempoDesc = condutor.getTempoDesconto() != null ? condutor.getTempoDesconto() : new BigDecimal(0);

        if(configuracao.getGerarDesconto()){
            condutor.setTempoDesconto(condutor.getTempoDesconto().add(hora.add(minuto)));
        }

        BigDecimal valor_desconto = BigDecimal.ZERO;

        if(tempoDesc.compareTo(new BigDecimal(configuracao.getNecessarioDesconto())) >= 0){

            valor_desconto = configuracao.getValorHora().multiply(tempoDesc);


            moviBanco.setValorDeconto(precos.subtract(valor_desconto));
            condutor.setTempoDesconto(new BigDecimal(0));
        }

        moviBanco.setValorTotal(precos.subtract(valor_desconto));
        moviBanco.setValorHoraTotal(configuracao.getValorHora());
        moviBanco.setValorHoraTotal(configuracao.getValorMinutoMulta());

        this.condutorRepository.save(condutor);
        this.movimentacaoRepository.save(moviBanco);
    }

    @Transactional
    public void deleta(final Movimentacao movi){

        final Movimentacao moviBanco = this.movimentacaoRepository.findById(movi.getId()).orElse(null);

        moviBanco.setAtivo(Boolean.FALSE);
        this.movimentacaoRepository.save(movi);
    }

}
