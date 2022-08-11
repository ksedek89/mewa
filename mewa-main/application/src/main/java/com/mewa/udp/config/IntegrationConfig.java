package com.mewa.udp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ip.udp.UnicastReceivingChannelAdapter;
import org.springframework.integration.ip.udp.UnicastSendingMessageHandler;

@Configuration
//konfiguracje klienta i serwera udp
public class IntegrationConfig {

    @Value(value = "${server.incoming-port}")
    private int incomingPort;
    @Value(value = "${siu.ip}")
    private String siuIp;
    @Value(value = "${siu.port}")
    private int siuPort;

    @Bean
    public IntegrationFlow processUdpMessage() {
        return IntegrationFlows
                .from(new UnicastReceivingChannelAdapter(incomingPort))
                .handle("udpService", "receive")
                .get();
    }

    @Bean
    public UnicastSendingMessageHandler handler() {
        return new UnicastSendingMessageHandler(siuIp , siuPort);
    }
}
