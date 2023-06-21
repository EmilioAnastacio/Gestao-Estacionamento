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


    @Transactional(rollbackFor = Exception.class)
    public void cadastrar(final Modelo modelo){

        Assert.isTrue(modelo.getNome() != null, "nome nao informado");
        Assert.isTrue(modelo.getMarca() != null, "marca nao informada");

        this.modeloRepository.save(modelo);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editar(final Long id, Modelo modelo){

        Assert.isTrue(modelo.getNome() != null, "nome nao informado");
        Assert.isTrue(modelo.getMarca() != null, "marca nao informada");

        final Modelo modeloBanco = this.modeloRepository.findById(id).orElse(null);
        Assert.isTrue(modeloBanco != null || !modeloBanco.getId().equals(id), "nao foi possivel encontrar o registro");

        this.modeloRepository.save(modelo);
    }

    @Transactional
    public void excluir(final Long id) {

        final Modelo modeloBanco = this.modeloRepository.findById(id).orElse(null);

        List<Veiculo> modeloLista = this.modeloRepository.findVeiculoByModelo(modeloBanco);

        if (modeloLista == null) {
            this.modeloRepository.delete(modeloBanco);
        } else {
            modeloBanco.setAtivo(false);
        }
        modeloBanco.setAtivo(false);
        this.modeloRepository.save(modeloBanco);
    }
}
