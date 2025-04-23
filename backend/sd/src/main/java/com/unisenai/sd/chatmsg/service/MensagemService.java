
package com.unisenai.sd.chatmsg.service;

import com.unisenai.sd.chatmsg.mensageria.Dispacher;
import com.unisenai.sd.chatmsg.mensageria.RecebeMensagens;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MensagemService {
    
    @Autowired
    private Dispacher dispacher;
    
    @Autowired
    private RecebeMensagens recMensagens;
    
    public Boolean despacharMensagens(String msg){
        
        return dispacher.enviaMensagens(msg);
    }
    
    public List<String> recebeMensagem(String fila){
        
        return recMensagens.recebeMensagens(fila);
    }
    
    
    
}
