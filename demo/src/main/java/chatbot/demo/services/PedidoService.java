package chatbot.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import chatbot.demo.dtos.MessageRequestDTO;
import chatbot.demo.dtos.MessageResponseDTO;
import chatbot.demo.models.Bebida;
import chatbot.demo.models.Pedido;
import chatbot.demo.models.PedidoEstado;
import chatbot.demo.models.Pizza;
import chatbot.demo.models.PizzaSabor;
import chatbot.demo.models.PizzaTamanho;
import chatbot.demo.models.TipoBebida;
import chatbot.demo.repositorys.PedidoRepository;

@Service
public class PedidoService {
    @Autowired
    PedidoRepository pedidoRepository;
     private static final String MENSAGEM_SAUDACAO = "🌙 Boa noite! 🌟 A Pizzaria Sabor Divino está pronta para atendê-lo. Sou a assistente virtual pronta para realizar seu pedido! 🍕✨ Qualquer dúvida ou informação, ligue para (33) 99933-3333. Se deseja fazer um pedido, digite: 'Quero realizar um pedido'. 📱🍴";

private static final String MENSAGEM_PEDIDO = "🍕 Primeiro escolha um sabor:\n" +
        "1. Margherita 🍅 \n" +
        "2. Calabresa 🥩 \n" +
        "3. Pepperoni 🌶️ \n" +
        "4. Frango com Catupiry 🧀 \n" +
        "Digite o número correspondente ao sabor desejado.";

private static final String PEDIDO_DEFAULT = "🚀 Seu pedido já foi enviado ao sistema. Se houver alguma dúvida ou deseja trocar algum elemento, ligue para (33) 99933-3333.";

private static final String MENSAGEM_ESCOLHER_TAMANHO = "🍕 Escolha um tamanho para sua pizza:\n" +
        "1. Pequena\n" +
        "2. Média\n" +
        "3. Grande\n" +
        "Digite o número correspondente ao tamanho desejado.";

private static final String MENSAGEM_ESCOLHER_OUTRA_PIZZA = "🍕 Se deseja adicionar outra pizza ao pedido, digite 'Sim', caso contrário 'Não'.";

private static final String MENSAGEM_ESCOLHER_BEBIDA = "🥤 Agora, escolha uma bebida:\n1. Coca-Cola 🥤\n2. Guaraná 🍇\n3. Água 💧\nDigite o número correspondente à bebida desejada. Se não desejar nenhuma bebida, digite '0'.";

private static final String MENSAGEM_ESCOLHER_OUTRA_BEBIDA = "🥤 Se deseja escolher outra bebida, digite 'Sim', caso contrário 'Não'.";

private static final String MENSAGEM_ADICIONAR_OBSERVACAO = "📝 Digite uma observação adicional que você deseja incluir no pedido. Exemplo: 'Quero a pizza de calabresa sem orégano'.";

private static final String MENSAGEM_ADICIONAR_METODO_DE_PAGAMENTO = "🔒 Escolha o método de pagamento:\n1. Cartão de Crédito 💳\n2. Dinheiro 💵\n3. Pix 🌐\nDigite o número correspondente ao método desejado.";

private static final String MENSAGEM_ADICIONAR_TROCO = "💵 Informe o valor da cédula que irá pagar, calcularemos o troco pra você.";

private static final String MENSAGEM_ADICIONAR_TELEFONE = "📞 Por favor, informe seu número de telefone para contato.";

private static final String MENSAGEM_ADICIONAR_NOME = "👤 Informe seu nome para concluir o pedido.";

private static final String MENSAGEM_ADICIONAR_ENDERECO = "🏠 Digite o endereço de entrega. Exemplo: Rua Roberto Carlos, Morumbi, Ed Ipanema, apt 1001.";



private static final String MENSAGEM_PEDIDO_CONFIRMADO = "🎉 Seu pedido foi confirmado! Agora estamos preparando sua deliciosa pizza. Em breve, você receberá atualizações sobre o status da entrega.";


    public MessageResponseDTO processarMensagem(MessageRequestDTO mensagem) {
        Optional<Pedido> pedidoOptional = Optional.ofNullable(pedidoRepository.findByUsername(mensagem.username()));
        System.out.println(mensagem.message());
        if (verificarTexto(mensagem.message(), "quero realizar um pedido")) {
            Pedido pedido = new Pedido(mensagem.username(), PedidoEstado.ESCOLHER_PIZZA);
            pedidoRepository.save(pedido);
            return new MessageResponseDTO(MENSAGEM_PEDIDO);
        }
        if (pedidoOptional.isPresent()) {
            Pedido pedido = pedidoOptional.get();
            switch (pedido.getEstado()) {
                case ESCOLHER_PIZZA:
                    return new MessageResponseDTO(escolhendoPizza(mensagem.message(), pedido));
                case ESCOLHER_TAMANHO:
                    return new MessageResponseDTO(escolhendoTamanho(mensagem.message(), pedido));
                case ESCOLHER_OUTRA_PIZZA:
                    return escolherOutraPizza(mensagem.message(), pedido);
                case ESCOLHER_BEBIDA:
                    return escolherBebida(mensagem.message(), pedido);
                case ESCOLHER_OUTRA_BEBIDA:
                    return escolherOutraBebida(mensagem.message(), pedido);
                case ADICIONAR_OBSERVACAO:
                    return adicionarInformacoes(mensagem.message(), pedido);
                case ADICIONAR_TELEFONE:
                    return adicionarInformacoes(mensagem.message(), pedido);
                case ADICIONAR_NOME:
                    return adicionarInformacoes(mensagem.message(), pedido);
                case ADICIONAR_ENDEREÇO:
                    return adicionarInformacoes(mensagem.message(), pedido);
                case CONFIRMAR_PEDIDO:
                    return confirmarPedido(mensagem.message(), pedido);
                case ADICIONAR_METODO_DE_PAGAMENTO:
                    return escolherMetodoPagamento(mensagem.message(), pedido);
                case ADICIONAR_TROCO:
                    return determinarTroco(mensagem.message(), pedido);
                default:
                    return new MessageResponseDTO(PEDIDO_DEFAULT);
            }
        } else {
            return new MessageResponseDTO(MENSAGEM_SAUDACAO);
        }
    }

    public boolean verificarTexto(String texto, String textoRecebido) {
        if (textoRecebido == null || textoRecebido.isEmpty()) {
            // Faça algo para lidar com textoRecebido nulo ou vazio
            return false;
        }
        Pattern pattern = Pattern.compile(textoRecebido, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(texto);
        return matcher.find();
    }

    private String escolhendoPizza(String mensagem, Pedido pedido) {
        Pizza pizza;

        switch (mensagem) {
            case "1":
                pizza = new Pizza(PizzaSabor.MARGHERITA);
                break;
            case "2":
                pizza = new Pizza(PizzaSabor.CALABRESA);
                break;
            case "3":
                pizza = new Pizza(PizzaSabor.PEPPERONI);
                break;
            case "4":
                pizza = new Pizza(PizzaSabor.FRANGO_CATUPIRY);
                break;
            default:
                return "Valor inválido. Para escolher um sabor de pizza, digite somente um número.";

        }
        List<Pizza> listaDePizzas = pedido.getPizzas();
        listaDePizzas.add(pizza);
        pedido.setPizzas(listaDePizzas);
        pedido.setEstado(PedidoEstado.ESCOLHER_TAMANHO);
        pedidoRepository.save(pedido);

        return MENSAGEM_ESCOLHER_TAMANHO;
    }

    private String escolhendoTamanho(String mensagem, Pedido pedido) {
        List<Pizza> pizzas = pedido.getPizzas();
        switch (mensagem) {
            case "1":
                pizzas = setarTamanho(pizzas, PizzaTamanho.PEQUENA, 35.00);
                break;
            case "2":
                pizzas = setarTamanho(pizzas, PizzaTamanho.MEDIA, 50.00);
                break;
            case "3":
                pizzas = setarTamanho(pizzas, PizzaTamanho.GRANDE, 60.00);
                break;

            default:
                return "Valor inválido. Para escolher um tamanho de pizza, digite um número de 1 a 3.";
        }
        pedido.setPizzas(pizzas);
        pedido.setEstado(PedidoEstado.ESCOLHER_OUTRA_PIZZA);
        pedidoRepository.save(pedido);
        return MENSAGEM_ESCOLHER_OUTRA_PIZZA;
    }

    private List<Pizza> setarTamanho(List<Pizza> pizzas, PizzaTamanho tamanho, double preço) {
        for (Pizza pizza : pizzas) {
            if (pizza.getTamanho() == null) {
                pizza.setTamanho(tamanho);
                pizza.setPreço(preço);
            }
        }
        return pizzas;
    }

    private MessageResponseDTO escolherOutraPizza(String mensagem, Pedido pedido) {
        if (mensagem.equalsIgnoreCase("nao") || mensagem.equalsIgnoreCase("não") ||
                mensagem.equalsIgnoreCase("não.") || mensagem.equalsIgnoreCase("nao.")) {
            pedido.setEstado(PedidoEstado.ESCOLHER_BEBIDA);
            pedidoRepository.save(pedido);
            return new MessageResponseDTO(MENSAGEM_ESCOLHER_BEBIDA);
        } else {
            if (mensagem.equalsIgnoreCase("sim") || mensagem.equalsIgnoreCase("sim.")) {
                pedido.setEstado(PedidoEstado.ESCOLHER_PIZZA);
                pedidoRepository.save(pedido);
                return new MessageResponseDTO(MENSAGEM_PEDIDO);
            } else {
                return new MessageResponseDTO("Não foi possível reconhecer sua resposta. Digite apenas 'Sim' ou 'Não'");
            }
        }
    }

    private MessageResponseDTO escolherBebida(String mensagem, Pedido pedido) {
        List<Bebida> bebidas = pedido.getBebidas();
        switch (mensagem) {
            case "0":
                bebidas = setarBebida(bebidas, TipoBebida.NENHUMA, 0.00);
                break;
            case "1":
                bebidas = setarBebida(bebidas, TipoBebida.COCA_COLA_2_LITROS, 10.00);
                break;
            case "2":
                bebidas = setarBebida(bebidas, TipoBebida.GUARANA_2_LITROS, 10.00);
                break;
            case "3":
                bebidas = setarBebida(bebidas, TipoBebida.PEPSI_2_LITROS, 11.00);
                break;
            default:
                return new MessageResponseDTO(
                        "Não foi possível reconhecer sua resposta. Digite apenas um número válido");

        }
        pedido.setBebidas(bebidas);
        pedido.setEstado(PedidoEstado.ESCOLHER_OUTRA_BEBIDA);
        pedidoRepository.save(pedido);
        return new MessageResponseDTO(MENSAGEM_ESCOLHER_OUTRA_BEBIDA);

    }

    private List<Bebida> setarBebida(List<Bebida> bebidas, TipoBebida tipoBebida, double preco) {
        Bebida bebida = new Bebida(tipoBebida, preco);
        bebidas.add(bebida);
        return bebidas;
    }

    private MessageResponseDTO escolherOutraBebida(String mensagem, Pedido pedido) {
        if (mensagem.equalsIgnoreCase("nao") || mensagem.equalsIgnoreCase("não") ||
                mensagem.equalsIgnoreCase("não.") || mensagem.equalsIgnoreCase("nao.")) {
            pedido.setEstado(PedidoEstado.ADICIONAR_OBSERVACAO);
            pedidoRepository.save(pedido);
            return new MessageResponseDTO(MENSAGEM_ADICIONAR_OBSERVACAO);
        } else {
            if (mensagem.equalsIgnoreCase("sim") || mensagem.equalsIgnoreCase("sim.")) {
                pedido.setEstado(PedidoEstado.ESCOLHER_BEBIDA);
                pedidoRepository.save(pedido);
                return new MessageResponseDTO(MENSAGEM_ESCOLHER_BEBIDA);
            } else {
                return new MessageResponseDTO("Não foi possível reconhecer sua resposta. Digite apenas 'Sim' ou 'Não'");
            }
        }

    }

    private MessageResponseDTO adicionarInformacoes(String message, Pedido pedido) {
        if (pedido.getEstado() == PedidoEstado.ADICIONAR_OBSERVACAO) {

            pedido.setObservações(message);
            pedido.setEstado(PedidoEstado.ADICIONAR_TELEFONE);
            pedidoRepository.save(pedido);
            return new MessageResponseDTO(MENSAGEM_ADICIONAR_TELEFONE);

        } else {
            if (pedido.getEstado() == PedidoEstado.ADICIONAR_NOME) {

                pedido.setCliente(message);
                pedido.setEstado(PedidoEstado.ADICIONAR_ENDEREÇO);
                pedidoRepository.save(pedido);
                return new MessageResponseDTO(MENSAGEM_ADICIONAR_ENDERECO);
            } else {
                if (pedido.getEstado() == PedidoEstado.ADICIONAR_TELEFONE) {
                    pedido.setTelefone(message);
                    pedido.setEstado(PedidoEstado.ADICIONAR_NOME);
                    pedidoRepository.save(pedido);
                    return new MessageResponseDTO(MENSAGEM_ADICIONAR_NOME);
                } else {
                    pedido.setPreço(calcularPreço(pedido));
                    pedido.setEndereço(message);
                    pedido.setEstado(PedidoEstado.CONFIRMAR_PEDIDO);
                    pedidoRepository.save(pedido);
                    return new MessageResponseDTO(receberPedido(pedido));
                }
            }
        }

    }

    private double calcularPreço(Pedido pedido) {
        double preçoPedido = 0;
        for(Pizza pizzas: pedido.getPizzas()) {
            preçoPedido+= pizzas.getPreço();
        }
        for(Bebida bebidas: pedido.getBebidas()) {
            preçoPedido+= bebidas.getPreco();
        }
        return preçoPedido;
        
    }

    private MessageResponseDTO confirmarPedido(String mensagem, Pedido pedido) {
        if (mensagem.equalsIgnoreCase("nao") || mensagem.equalsIgnoreCase("não") ||
                mensagem.equalsIgnoreCase("não.") || mensagem.equalsIgnoreCase("nao.")) {
            pedidoRepository.delete(pedido);
            return new MessageResponseDTO(
                    "Seu pedido foi cancelado, para iniciar um novo atendimento digite: 'Quero realizar um pedido'");
        } else {
            if (mensagem.equalsIgnoreCase("sim") || mensagem.equalsIgnoreCase("sim.")) {
                pedido.setEstado(PedidoEstado.ADICIONAR_METODO_DE_PAGAMENTO);
                pedidoRepository.save(pedido);
                return new MessageResponseDTO(MENSAGEM_ADICIONAR_METODO_DE_PAGAMENTO);
            } else {
                return new MessageResponseDTO("Não foi possível reconhecer sua resposta. Digite apenas 'Sim' ou 'Não'");
            }
        }

    }

    private MessageResponseDTO escolherMetodoPagamento(String message, Pedido pedido) {
        switch (message) {
            case "1":
                pedido.setMetodoPagamento("Cartão de crédito");
                break;
            case "2":
                pedido.setMetodoPagamento("Dinheiro");
                pedido.setEstado(PedidoEstado.ADICIONAR_TROCO);
                pedidoRepository.save(pedido);
                return new MessageResponseDTO(MENSAGEM_ADICIONAR_TROCO);

            case "3":
                pedido.setMetodoPagamento("Pix");
                break;
            default:
                return new MessageResponseDTO("Não foi possível reconhecer sua resposta. Digite números");
        }
        pedido.setEstado(PedidoEstado.PEDIDO_CONFIRMADO);
        pedidoRepository.save(pedido);
        return new MessageResponseDTO(MENSAGEM_PEDIDO_CONFIRMADO);

    }

    private MessageResponseDTO determinarTroco(String message, Pedido pedido) {
        try {
            double valorTroco = Double.parseDouble(message);
            pedido.setTroco(valorTroco - pedido.getPreço());
            pedido.setEstado(PedidoEstado.PEDIDO_CONFIRMADO);
            pedidoRepository.save(pedido);
            return new MessageResponseDTO(MENSAGEM_PEDIDO_CONFIRMADO);

        } catch (NumberFormatException e) {

            return new MessageResponseDTO("Por favor, insira apenas um valor numérico válido para o troco.");
        }

    }
    public String receberPedido(Pedido pedido) {
        StringBuilder mensagem = new StringBuilder();
        mensagem.append("📥 **Pedido Recebido! ** 🎉\n\n");
        mensagem.append("👤 Cliente: ").append(pedido.getCliente()).append("\n");
        mensagem.append("☎️ Telefone: ").append(pedido.getTelefone()).append("\n");

        mensagem.append("\n🍕 **Pizza(s):**\n");
        for (Pizza pizza : pedido.getPizzas()) {
            mensagem.append("- ").append(pizza.getSabor().toString().toLowerCase().replace("_", " ")).append("\n");
        }

        mensagem.append("\n🥤 **Bebida(s):**\n");
        for (Bebida bebida : pedido.getBebidas()) {
            mensagem.append("- ").append(bebida.getBebidaTipo().toString().toLowerCase().replace("_", " ")).append("\n");
        }

        mensagem.append("\n📍 **Endereço de Entrega:** ").append(pedido.getEndereço()).append("\n");
        mensagem.append("📝 **Observações:** ").append(pedido.getObservações()).append("\n");
        mensagem.append("💰 **Preço Total:** R$").append(pedido.getPreço()).append("\n");
        mensagem.append("👍 Seu pedido está correto? Digite 'sim' ou 'não'.");

        

        // Outras lógicas, como salvar o pedido no banco de dados, podem ser adicionadas aqui.

        return mensagem.toString();
    }
}
