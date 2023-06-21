package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.Relatorio;
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

    @Transactional(rollbackFor = Exception.class)
    public void cadastrar(final Movimentacao movi){

        Assert.isTrue(movi.getVeiculo() != null, "Veiculo nao informado");
        Assert.isTrue(movi.getCondutor() != null, "Condutor nao informada");
        Assert.isTrue(movi.getEntrada() != null, "Entradada nao informado");
        Assert.isTrue(movi.getSaida() != null, "Saida nao informada");

        this.movimentacaoRepository.save(movi);

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
    public Relatorio horaFinal(final Long id){

        //busca a movimentacao do momento pelo id
        final Movimentacao moviBanco = this.movimentacaoRepository.findById(id).orElse(null);
        Assert.isTrue(moviBanco != null, "movimentacao nao encontrada");

        //cria uma duração q é entre a entrada e a saida
        final LocalDateTime saida = LocalDateTime.now();
        Duration duracaoHora = Duration.between(moviBanco.getEntrada(), saida);

        //busca a configuração que esta sendo usada, no caso uma long 1, a primeira
        final Configuracao configuracao = this.configuracaoRepository.findById(1L).orElse(null);
        Assert.isTrue(configuracao != null, "configuracao nao existe");

        //busca o condutor
        final Condutor condutor = this.condutorRepository.findById(moviBanco.getCondutor().getId()).orElse(null);
        Assert.isTrue(condutor != null, "condutor nao existe");

        //Set no tempo pra receber da duração entre saida e entrada a parte dos minutos e horas
        moviBanco.setTempoMinuto(duracaoHora.toMinutesPart());
        moviBanco.setTempoHora(duracaoHora.toHoursPart());
        moviBanco.setSaida(saida);

        //variavel hora recebendo como bigdeciaml o set feito em cima das horas e minutos
        final BigDecimal hora = BigDecimal.valueOf(duracaoHora.toHoursPart());
        final BigDecimal minuto = BigDecimal.valueOf(duracaoHora.toMinutesPart()).divide(BigDecimal.valueOf(60),2, RoundingMode.HALF_EVEN);

        //conta para o preco, busca da configuração o valor da hora e multiplica pela quantidade de hora passada acima,
        // e adiciona apos isso o calculo dos minutos
        BigDecimal precos = configuracao.getValorHora().multiply(hora).add(configuracao.getValorHora().multiply(minuto));

        //mesma situação, cria um bigDecimal pra buscar o tempoDesconto q ele tem.
        final BigDecimal tempoDesc = condutor.getTempoDesconto() != null ? condutor.getTempoDesconto() : new BigDecimal(0);

        //e se caso um desconto existir,o condutor tem seu tempo de desconto adicionado.
        if(configuracao.getGerarDesconto()){
            condutor.setTempoDesconto(condutor.getTempoDesconto().add(hora.add(minuto)));
        }

        //iniciado uma variavel como bigdeciaml em 0
        BigDecimal valor_desconto = BigDecimal.ZERO;

        //verifica se ele precisa do desconto ou nao, um boolean
        if(tempoDesc.compareTo(new BigDecimal(configuracao.getNecessarioDesconto())) >= 0){

            //conta de valor do desconto, pega o valor da hora e multiplica pelo tempo de desconto
            valor_desconto = configuracao.getValorHora().multiply(tempoDesc);

            //set no movimentação para o valor do desconto, ou seja, precos - o valor
            moviBanco.setValorDeconto(precos.subtract(valor_desconto));
            condutor.setTempoDesconto(new BigDecimal(0));
        }
        //sets no banco, do preco com o valor de desconto
        moviBanco.setValorTotal(precos.subtract(valor_desconto));
        moviBanco.setValorHoraTotal(configuracao.getValorHora());
        moviBanco.setValorHoraTotal(configuracao.getValorMinutoMulta());


        Relatorio relatorio = new Relatorio(moviBanco.getEntrada(), moviBanco.getSaida(), moviBanco.getCondutor(),
                moviBanco.getVeiculo(), moviBanco.getTempoHora(), condutor.getTempoDesconto(),
                precos.subtract(valor_desconto).setScale(2,RoundingMode.HALF_EVEN),
                moviBanco.getValorDeconto());


        this.condutorRepository.save(condutor);
        this.movimentacaoRepository.save(moviBanco);

        return relatorio;
    }

    @Transactional
    public void deleta(final Movimentacao movi){

        final Movimentacao moviBanco = this.movimentacaoRepository.findById(movi.getId()).orElse(null);

        moviBanco.setAtivo(Boolean.FALSE);
        this.movimentacaoRepository.save(movi);
    }

}
