
package com.unisenai.sd.chatmsg.controller;

import com.unisenai.sd.chatmsg.service.MensagemService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class MensagemController {
    
    @Autowired
    private MensagemService msgService;
    
    
    @PostMapping("/enviamsg")
    public ResponseEntity<String> enviaMensagensTodos(@RequestBody String msg){
        
        if(msgService.despacharMensagens(msg)){
            return new ResponseEntity<>("Mensagem enviada para todos! ", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Erro ao enviar mensagem para todos!", HttpStatus.INTERNAL_SERVER_ERROR);
        }        
        
    }
    @GetMapping("/recebemsg/{fila}")
    public ResponseEntity<List<String>> recebeMensagensPorFila(@PathVariable("fila") String fila){
        
        
        List<String> msg = msgService.recebeMensagem(fila);
        if(!msg.isEmpty()){
            return new ResponseEntity<>(msg, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } 
        
        
    }
    
}
