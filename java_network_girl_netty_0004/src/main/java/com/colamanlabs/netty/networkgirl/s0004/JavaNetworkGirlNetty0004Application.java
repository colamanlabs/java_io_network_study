package com.colamanlabs.netty.networkgirl.s0004;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class JavaNetworkGirlNetty0004Application
{
    
    public static void main(String[] args)
    {
        SpringApplication.run(JavaNetworkGirlNetty0004Application.class, args);
    }
    
    @EventListener(ApplicationReadyEvent.class)
    public void init() throws Exception
    {
        log.info(String.format("[JavaNetworkGirlNetty0004Application/init] BEGIN"));
//        EchoServer echoServer = new EchoServer();
//        echoServer.main(null);
        
//        EchoServerV2 echoServer2 = new EchoServerV2();
//        echoServer2.main(null);
        
        EchoServerV3 echoServer3 = new EchoServerV3();
        echoServer3.main(null);              
        log.info(String.format("[JavaNetworkGirlNetty0004Application/init] END"));
    }
}
