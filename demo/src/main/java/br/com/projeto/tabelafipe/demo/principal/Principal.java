package br.com.projeto.tabelafipe.demo.principal;

import br.com.projeto.tabelafipe.demo.model.Dados;
import br.com.projeto.tabelafipe.demo.model.Modelos;
import br.com.projeto.tabelafipe.demo.model.Veiculo;
import br.com.projeto.tabelafipe.demo.service.ConsumoAPI;
import br.com.projeto.tabelafipe.demo.service.ConverteDados;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private final Scanner leitura = new Scanner(System.in);
    private final ConsumoAPI consumo = new ConsumoAPI();
    private final ConverteDados conversor = new ConverteDados();

    private String ENDERECO = "https://parallelum.com.br/fipe/api/v1/";
    private String MARCAS = "/marcas";

    public void exibeMenu() {
        System.out.println("""
                Digite o tipo de veiculo:
                Carros
                Motos
                Caminhoes
                """);

        String tipoDeVeiculo = leitura.nextLine().trim().toLowerCase();
        String url = ENDERECO + tipoDeVeiculo + MARCAS;
        String json = consumo.obterDados(url);

        List<Dados> dados = conversor.obterLista(json, Dados.class);

        for (Dados d : dados) {
            System.out.println(d.codigo() + " - " + d.nome());
        }


        System.out.println("Digite a marca desejada: ");
        String codigoMarca = leitura.nextLine().trim().toLowerCase();
        String urlMarca = ENDERECO + tipoDeVeiculo + MARCAS + "/" + codigoMarca + "/modelos";
        String jsonModelos = consumo.obterDados(urlMarca);
        var modelosLista = conversor.obterDados(jsonModelos, Modelos.class);
        System.out.println("\nModelos dessa marca: ");
        modelosLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);

        System.out.println("\nDigite um trecho do nome do caro a ser buscado: ");
        var nomeVeiculo = leitura.nextLine();

        List<Dados> modelosFilter = modelosLista.modelos().stream()
                .filter(m -> m.nome().toLowerCase().contains(nomeVeiculo.toLowerCase()))
                .collect(Collectors.toList());

        System.out.println("\nModelos filtrados");
        modelosFilter.forEach(System.out::println);

        System.out.println("Digite o codigo do modelo para buscar valores: ");
        var codigoModelo = leitura.nextLine();

        String urlAnos = ENDERECO + tipoDeVeiculo + MARCAS + "/" + codigoMarca + "/modelos/" + codigoModelo + "/anos";
        String jsonAnos = consumo.obterDados(urlAnos);
        List<Dados> anos = conversor.obterLista(jsonAnos, Dados.class);
        List<Veiculo> veiculos = new ArrayList<>();

        for (Dados ano : anos) {
            var enderecoAnos = ENDERECO + tipoDeVeiculo + MARCAS + "/" + codigoMarca + "/modelos/" + codigoModelo + "/anos/" + ano.codigo();
            String jsonVeiculo = consumo.obterDados(enderecoAnos);
            Veiculo veiculo = conversor.obterDados(jsonVeiculo, Veiculo.class);
            veiculos.add(veiculo);
        }

        System.out.println("\nTodos os veiculos filtrados com avaliaçoes por ano:");
        veiculos.forEach(System.out::println);

    }
}
