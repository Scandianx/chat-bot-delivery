package chatbot.demo.repositorys;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import chatbot.demo.models.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Pedido findByUsername(String username);
}
