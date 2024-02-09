package chatbot.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import chatbot.demo.dtos.MessageRequestDTO;
import chatbot.demo.dtos.MessageResponseDTO;
import chatbot.demo.services.PedidoService;

@RestController
@CrossOrigin("*")
@RequestMapping("pedidos")
public class PedidoController {
    @Autowired
    PedidoService pedidoService;
    
    @PostMapping()
    public MessageResponseDTO processarMensagem (@RequestBody MessageRequestDTO mensagem) {
        return pedidoService.processarMensagem(mensagem);
    }
}
