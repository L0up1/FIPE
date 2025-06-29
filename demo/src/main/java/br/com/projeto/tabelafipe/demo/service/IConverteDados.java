package br.com.projeto.tabelafipe.demo.service;

public interface IConverteDados {
    <T> T obterDados(String json, Class<T> classe);
}
