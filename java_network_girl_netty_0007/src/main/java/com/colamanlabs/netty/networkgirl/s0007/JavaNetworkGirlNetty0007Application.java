package com.colamanlabs.netty.networkgirl.s0007;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class JavaNetworkGirlNetty0007Application
{
    
    public static void main(String[] args)
    {
        SpringApplication.run(JavaNetworkGirlNetty0007Application.class, args);
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void init() throws Exception
    {
        log.info(String.format("[JavaNetworkGirlNetty0007Application/init] BEGIN"));
        
        EchoServerWithFuture server = new EchoServerWithFuture();
        server.main(null);
        log.info(String.format("[JavaNetworkGirlNetty0007Application/init] END"));
    }
    
}
