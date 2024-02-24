package com.colamanlabs.netty.networkgirl.s0002;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class JavaNetworkGirlNetty0002Application
{
    
    public static void main(String[] args)
    {
        SpringApplication.run(JavaNetworkGirlNetty0002Application.class, args);
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void init() throws Exception
    {
        log.info(String.format("[JavaNetworkGirlNetty0002Application/init] BEGIN"));
        
        EchoServer echoServer = new EchoServer();
        echoServer.main(null);
        log.info(String.format("[JavaNetworkGirlNetty0002Application/init] END"));
    }
}
