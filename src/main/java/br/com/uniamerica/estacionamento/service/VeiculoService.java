package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import br.com.uniamerica.estacionamento.repository.VeiculoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class VeiculoService {

    @Autowired
    private VeiculoRepository veiculoRepository;


    @Transactional
    public void cadastrar(Veiculo veiculo){

        Assert.isTrue(veiculo.getPlaca() != null, "placa nao identificada");
        Assert.isTrue(veiculo.getModelo() != null, "modelo nao identificado");
        Assert.isTrue(veiculo.getCor() != null, "cor nao identificada");
        Assert.isTrue(veiculo.getTipo() != null, "tipo nao identificado");
        Assert.isTrue(veiculo.getAno() != null, "ano nao identificado, ou fora do padrao");

        String placaAntiga = "^[A-Z]{3}-\\d{4}$";
        Assert.isTrue(!veiculo.getPlaca().matches(placaAntiga), "formatação de placa errada");
        String placaNova = "^[A-Z]{3}\\d{1}[A-Z]{1}\\d{2}$";
        Assert.isTrue(!veiculo.getPlaca().matches(placaNova), "formatação de placa errada");

        this.veiculoRepository.save(veiculo);

    }

    @Transactional
    public void editar(Veiculo veiculo){

        final Veiculo veiculoBanco = this.veiculoRepository.findById(veiculo.getId()).orElse(null);

        Assert.isTrue(veiculoRepository.findByPlaca(veiculo.getPlaca()).isEmpty(), "essa placa ja existe");

        Assert.isTrue(veiculo.getPlaca() != null, "placa nao foi colocado");
        Assert.isTrue(veiculo.getModelo() != null, "modelo nao foi colocado");
        Assert.isTrue(veiculo.getCor() != null, "cor nao foi colocado");
        Assert.isTrue(veiculo.getTipo() != null, "tipo nao foi colocado");
        Assert.isTrue(veiculo.getAno() != null, "ano nao foi colocado");

        String placaAntiga = "^[A-Z]{3}-\\d{4}$";
        Assert.isTrue(!veiculo.getPlaca().matches(placaAntiga), "formatação de placa errada");
        String placaNova = "^[A-Z]{3}\\d{1}[A-Z]{1}\\d{2}$";
        Assert.isTrue(!veiculo.getPlaca().matches(placaNova), "formatação de placa errada");

        Assert.isTrue(veiculoBanco == null || !veiculoBanco.getId().equals(veiculo.getId()), "nao deu pra indentificar");

        this.veiculoRepository.save(veiculo);
    }

    @Transactional
    public void deleta(final Veiculo veiculo){

        final Veiculo veiculoBanco = this.veiculoRepository.findById(veiculo.getId()).orElse(null);

        List<Movimentacao> veiculoLista = this.veiculoRepository.findMovimentacaoByVeiculo(veiculoBanco);

        if (veiculoLista == null) {
            this.veiculoRepository.delete(veiculo);
        } else {
            veiculoBanco.setAtivo(false);
            this.veiculoRepository.save(veiculo);
        }
    }

}
