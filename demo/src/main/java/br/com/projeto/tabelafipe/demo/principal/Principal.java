package br.com.projeto.tabelafipe.demo.principal;

import br.com.projeto.tabelafipe.demo.model.Dados;
import br.com.projeto.tabelafipe.demo.model.Modelos;
import br.com.projeto.tabelafipe.demo.service.ConsumoAPI;
import br.com.projeto.tabelafipe.demo.service.ConverteDados;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Principal {

    private Scanner leitura = new Scanner(System.in);
    private ConsumoAPI consumo = new ConsumoAPI();
    private ConverteDados conversor = new ConverteDados();

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
        String urlMarca = ENDERECO + tipoDeVeiculo + MARCAS + "/" + codigoMarca + "/modelos"; // Corrigido
        String jsonModelos = consumo.obterDados(urlMarca);
        var modelosLista = conversor.obterDados(jsonModelos, Modelos.class);
        System.out.println("\nModelos dessa marca: ");
        modelosLista.modelos().stream()
                .sorted(Comparator.comparing(Dados::codigo))
                .forEach(System.out::println);
    }
}
