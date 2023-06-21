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

    @Transactional
    public void cadastrar(Marca marca){

        Assert.isTrue(marca.getNome() != null, "nome nao informado");

        marcaRepository.save(marca);
    }

    @Transactional
    public void editar(final Long id, Marca marca){

        Assert.isTrue(marca.getNome() != null, "nome nao informado");

        final Marca marcaBanco = this.marcaRepository.findById(id).orElse(null);
        Assert.isTrue(marcaBanco != null, "nao foi possivel encontrar o registro");
        Assert.isTrue(!marcaBanco.getId().equals(marca.getId()),"nao foi possivel entrar no registro");

        marcaRepository.save(marca);


    }

    @Transactional
    public void deleta(final Marca marca) {

        final Marca marcaBanco = this.marcaRepository.findById(marca.getId()).orElse(null);

        List<Modelo> modeloLista = this.marcaRepository.findModeloByMarca(marcaBanco);

        if (modeloLista.isEmpty()) {
            this.marcaRepository.delete(marcaBanco);
        } else {
            marcaBanco.setAtivo(false);
            this.marcaRepository.save(marca);
        }
    }
}
