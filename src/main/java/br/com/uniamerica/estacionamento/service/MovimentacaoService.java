package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.Relatorio;
import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.entity.Configuracao;
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
    public void cadastrar(Movimentacao movi){

        Assert.isTrue(movi.getVeiculo() != null, "Veiculo nao informado");
        Assert.isTrue(movi.getCondutor() != null, "Condutor nao informada");
        Assert.isTrue(movi.getEntrada() != null, "Entradada nao informado");
        Assert.isTrue(movi.getSaida() != null, "Saida nao informada");

        movimentacaoRepository.save(movi);

    }

    @Transactional(rollbackFor = Exception.class)
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

    @Transactional(rollbackFor = Exception.class)
    public Relatorio finalizar(final Long id){

        final Movimentacao movimentacaoBanco = this.movimentacaoRepository.findById(id).orElse(null);

        Assert.isTrue(movimentacaoBanco != null, "Error registro nao encontrado");

        // Assert.isTrue(movimentacaoBanco.getSaida() == null,"Essa movimentacao ja foi finalizada. ID:" + movimentacaoBanco.getId());

        LocalDateTime saida = LocalDateTime.now();

        Duration duracao = Duration.between(movimentacaoBanco.getEntrada(), saida);

        Configuracao config = this.configuracaoRepository.findById(1L).orElse(null);

        Condutor alguem = this.condutorRepository.findById(movimentacaoBanco.getCondutor().getId()).orElse(null);

        movimentacaoBanco.setSaida(saida);
        //movimentacaoBanco.setHoras(horas);

        final BigDecimal horas = BigDecimal.valueOf(duracao.toHoursPart());
        final BigDecimal minutos = BigDecimal.valueOf(duracao.toMinutesPart()).divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_EVEN);
        BigDecimal preco = config.getValorHora().multiply(horas).add(config.getValorHora().multiply(minutos));


        if(config.isGerarDesconto()){
            if(alguem.getTempoDesconto().compareTo(new BigDecimal(config.getNecessarioDesconto())) > 0){
                System.out.println(alguem.getTempoDesconto().compareTo(new BigDecimal(config.getNecessarioDesconto())));
                movimentacaoBanco.setValorDeconto(preco.subtract(config.getTempoDesconto()));
            }else{
                alguem.setTempoDesconto(horas.add(minutos));
            }
        }else{
            alguem.setTempoDesconto(horas.add(minutos));
        }

        Integer horasI = horas.intValue();
        Integer minutosI = minutos.intValue();

        alguem.setTempoPago(preco);

        movimentacaoBanco.setTempoHora(horasI);
        movimentacaoBanco.setTempoMinuto(minutosI);
        movimentacaoBanco.setValorHoraTotal(preco);

        this.movimentacaoRepository.save(movimentacaoBanco);

        return new Relatorio(movimentacaoBanco.getEntrada(),
                movimentacaoBanco.getSaida(),
                movimentacaoBanco.getCondutor(),
                movimentacaoBanco.getVeiculo(),
                movimentacaoBanco.getTempoHora(),
                movimentacaoBanco.getCondutor().getTempoDesconto(),
                movimentacaoBanco.getValorHoraTotal(),
                movimentacaoBanco.getValorDeconto());

    }

    @Transactional(rollbackFor = Exception.class)
    public void excluir(final Long id){

        final Movimentacao moviBanco = this.movimentacaoRepository.findById(id).orElse(null);

        moviBanco.setAtivo(Boolean.FALSE);
        this.movimentacaoRepository.save(moviBanco);
    }

}
