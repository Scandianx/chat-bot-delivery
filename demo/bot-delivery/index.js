const qrcode = require('qrcode-terminal');
const axios = require('axios');
const { Client } = require('whatsapp-web.js');
const client = new Client();


client.on('qr', (qr) => {
    qrcode.generate(qr, { small: true });
});

client.on('ready', () => {
    console.log('Client is readyy!');
});
client.on('message', async (message) => {
    
    
        
        const textoSemExclamacao = message.body;

        
        let user = await message.getContact();

        
        const jsonData = {
            message: textoSemExclamacao,
            username: user.id.user
        };

        // Faz a requisição POST
        try {
            
            const response = await axios.post('http://localhost:8083/pedidos', jsonData);
            console.log('Resposta da requisição:', response.data);
            let responseM = response.data;
            // Se desejar, você pode enviar uma resposta ao usuário
            await client.sendMessage(message.from, responseM.mensagem);
        } catch (error) {
            console.error('Erro na requisição:', error.message);

            // Se desejar, você pode enviar uma resposta ao usuário informando sobre o erro
            await client.sendMessage(message.from, 'Erro ao enviar a requisição.');
        }
    
});

client.initialize();

