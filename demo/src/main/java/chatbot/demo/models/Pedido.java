package chatbot.demo.models;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(nullable = true)
    private String cliente;
    @Column(nullable = true)
    private String telefone;
    @ElementCollection
    private List<Pizza> pizzas;
    @ElementCollection
    private List<Bebida> bebidas;
    @Column(nullable = true)
    private String endereço;
    @Column(nullable = true)
    private String observações;
    @Column(nullable = true)
    private double preço;
    @Column(nullable = true)
    private String metodoPagamento;
    @Column(nullable = true)
    private double troco;
    


    // Classe utilizada para controlar em qual fase do pedido o cliente está
    @Column(nullable = false)
    private PedidoEstado estado;
    // Classe utilizada para controlar o cliente do pedido de acordo com a
    // requisição feita com o username (userID)
    @Column(name = "username", nullable = false)
    private String username;

    public Pedido(String userID, PedidoEstado estado) {
        this.username = userID;
        this.estado = estado;
    }

}
