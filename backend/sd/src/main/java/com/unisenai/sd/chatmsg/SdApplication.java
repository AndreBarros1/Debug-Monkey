package com.unisenai.sd.chatmsg;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SdApplication {

	public static void main(String[] args) {
		SpringApplication.run(SdApplication.class, args);
                
                System.out.println("\n");
                System.out.println("################################################");
                System.out.println(" Servidor de mensagens via RabbitMQ, On The Fly!");
                System.out.println("################################################\n\n");
	}

}
