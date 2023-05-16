package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Veiculo;
import br.com.uniamerica.estacionamento.repository.ModeloRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class ModeloService {

    @Autowired
    private ModeloRepository modeloRepository;


    @Transactional
    public void cadastrar(Modelo modelo){
        Assert.isTrue(modelo.getNome() != null, "nome nao informado");
        Assert.isTrue(modelo.getMarca() != null, "marca nao informada");

        modeloRepository.save(modelo);
    }

    @Transactional
    public void editar(final Long id, Modelo modelo){

        Assert.isTrue(modelo.getNome() != null, "nome nao informado");
        Assert.isTrue(modelo.getMarca() != null, "marca nao informada");

        final Modelo modeloBanco = this.modeloRepository.findById(id).orElse(null);
        Assert.isTrue(modeloBanco != null, "nao foi possivel encontrar o registro");
        Assert.isTrue(!modeloBanco.getId().equals(modelo.getId()), "nao foi possivel encontrar o registro");

        modeloRepository.save(modelo);
    }

    @Transactional
    public void deleta(final Modelo modelo) {

        final Modelo modeloBanco = this.modeloRepository.findById(modelo.getId()).orElse(null);

        List<Veiculo> modeloLista = this.modeloRepository.findVeiculoByModelo(modeloBanco);

        if (modeloLista == null) {
            this.modeloRepository.delete(modeloBanco);
        } else {
            modeloBanco.setAtivo(false);
            this.modeloRepository.save(modelo);
        }
    }
}
