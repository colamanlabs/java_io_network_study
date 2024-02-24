package com.colamanlabs.netty.networkgirl.s0003;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class JavaNetworkGirlNetty0003Application
{
    
    public static void main(String[] args)
    {
        SpringApplication.run(JavaNetworkGirlNetty0003Application.class, args);
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void init() throws Exception
    {
        log.info(String.format("[JavaNetworkGirlNetty0003Application/init] BEGIN"));
        
        EchoClient echoClient = new EchoClient();
        echoClient.main(null);
        log.info(String.format("[JavaNetworkGirlNetty0003Application/init] END"));
    }
}
