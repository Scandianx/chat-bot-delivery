package chatbot.demo.models;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class Pizza {
private PizzaSabor sabor;
private PizzaTamanho tamanho;
private double preço;
public Pizza (PizzaSabor sabor) {
    this.sabor=sabor;
}
}
