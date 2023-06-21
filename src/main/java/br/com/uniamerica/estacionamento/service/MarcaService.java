package br.com.uniamerica.estacionamento.service;

import br.com.uniamerica.estacionamento.entity.Condutor;
import br.com.uniamerica.estacionamento.entity.Marca;
import br.com.uniamerica.estacionamento.entity.Modelo;
import br.com.uniamerica.estacionamento.entity.Movimentacao;
import br.com.uniamerica.estacionamento.repository.MarcaRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

@Service
public class MarcaService {

    @Autowired
    private MarcaRepository marcaRepository;

    @Transactional(rollbackFor = Exception.class)
    public void cadastrar(final Marca marca){

        Assert.isTrue(marca.getNome() != null, "nome nao informado");

        this.marcaRepository.save(marca);
    }

    @Transactional(rollbackFor = Exception.class)
    public void editar(final Long id, Marca marca){

        Assert.isTrue(marca.getNome() != null, "nome nao informado");

        final Marca marcaBanco = this.marcaRepository.findById(id).orElse(null);
        Assert.isTrue(marcaBanco != null || !marcaBanco.getId().equals(id), "nao foi possivel encontrar o registro");
        this.marcaRepository.save(marca);
    }

    @Transactional
    public void excluir(final Long id) {


        final Marca marcaBanco = this.marcaRepository.findById(id).orElse(null);

        List<Modelo> modeloLista = this.marcaRepository.findModeloByMarca(marcaBanco);

        if (modeloLista == null) {
            this.marcaRepository.delete(marcaBanco);
        } else {
            marcaBanco.setAtivo(false);
        }
        marcaBanco.setAtivo(false);
        this.marcaRepository.save(marcaBanco);
    }
}
