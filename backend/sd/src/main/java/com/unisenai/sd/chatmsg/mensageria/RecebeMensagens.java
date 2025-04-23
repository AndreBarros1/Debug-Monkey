
package com.unisenai.sd.chatmsg.mensageria;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class RecebeMensagens {
 
    @Value("${msg.host}")
    private String HOST;
    
    @Value("${msg.virtual_host}")
    private String VIRTUAL_HOST;
    
    @Value("${msg.usuario}")
    private String USUARIO;
    
    @Value("${msg.passwd}")
    private String PASSWD;
    

    public List<String> recebeMensagens(String fila){
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setUsername(USUARIO);
        factory.setPassword(PASSWD);
        factory.setVirtualHost(VIRTUAL_HOST);
        List<String> listaMsg = new ArrayList<>();
        
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            Map<String, Object> args = new HashMap<String, Object>();
            args.put("x-max-length", 100);
            channel.queueDeclare(fila, true, false, false, args);
            
            Long totmsg = channel.messageCount(fila);
            System.out.println(" [*] Aguardando mensagens via RabbitMQ... Total de msg " + totmsg );
            
            for(int i=0;i<totmsg;i++){
                DeliverCallback deliverCallback = new DeliverCallback() {
                    @Override
                    public void handle(String consumerTag, Delivery delivery) throws IOException {
                        String msg = "[" + fila + "] " + new String(delivery.getBody(), "UTF-8");
                        System.out.println(" [!] Mensagem recebida: '" + msg + "'");
                        listaMsg.add(msg);
                    }
                };
                channel.basicConsume(fila, true, deliverCallback, consumerTag -> { });
                //connection.close();
                //hannel.close();
            }

        } catch (IOException | TimeoutException ex) {
            System.out.println("Erro ao acesso RabbitMQ..: " + ex.getLocalizedMessage() + "\n" +
                               ex.getCause());
        }
        if(listaMsg.isEmpty()){
            listaMsg.add(".");
        }
        return listaMsg;
        }
       
}
