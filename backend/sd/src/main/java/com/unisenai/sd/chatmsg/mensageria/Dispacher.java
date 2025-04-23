
package com.unisenai.sd.chatmsg.mensageria;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


@Service
public class Dispacher {
    
    @Value("${msg.exchange}")
    private String EXCHANGE;
    
    @Value("${msg.host}")
    private String HOST;
    
    @Value("${msg.virtual_host}")
    private String VIRTUAL_HOST;
    
    @Value("${msg.usuario}")
    private String USUARIO;
    
    @Value("${msg.passwd}")
    private String PASSWD;
    
    
    public Boolean enviaMensagens(String msg){
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        factory.setVirtualHost(VIRTUAL_HOST);
        factory.setUsername(USUARIO);
        factory.setPassword(PASSWD);
        Boolean result = false;
        
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            Map<String, Object> args = new HashMap<String, Object>();
            args.put("x-max-length", 100);
            channel.exchangeDeclare("clientes1-3", BuiltinExchangeType.FANOUT, true);
            //Enviando mensagem...
            channel.basicPublish(EXCHANGE,"", null, msg.getBytes());
            System.out.println("Mensagem enviada...: " + msg );
            result = true;
            
        } catch (IOException ex) {
            Logger.getLogger(Dispacher.class.getName()).log(Level.SEVERE, null, ex);
        } catch (TimeoutException ex) {
            Logger.getLogger(Dispacher.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;        
    }
    
    
    
    
}
