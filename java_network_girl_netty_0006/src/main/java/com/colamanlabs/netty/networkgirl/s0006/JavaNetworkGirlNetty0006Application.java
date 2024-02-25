package com.colamanlabs.netty.networkgirl.s0006;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class JavaNetworkGirlNetty0006Application
{
    
    public static void main(String[] args)
    {
        SpringApplication.run(JavaNetworkGirlNetty0006Application.class, args);
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void init() throws Exception
    {
        log.info(String.format("[JavaNetworkGirlNetty0005Application/init] BEGIN"));
        EchoServerV4 server = new EchoServerV4();
        server.main(null);
        log.info(String.format("[JavaNetworkGirlNetty0005Application/init] END"));
    }
}
