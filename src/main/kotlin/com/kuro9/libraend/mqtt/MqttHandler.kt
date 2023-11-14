package com.kuro9.libraend.mqtt

import org.springframework.context.annotation.Bean
import org.springframework.integration.annotation.ServiceActivator
import org.springframework.integration.channel.DirectChannel
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.MessageHandler
import org.springframework.stereotype.Service


@Service
class MqttHandler {
    @ServiceActivator(inputChannel = "mqttOrderOutboundChannel")
    fun mqttOrderMessageHandler(): MessageHandler {
        val messageHandler = MqttPahoMessageHandler(clientId, mqttClientFactory())
        messageHandler.setAsync(true)
        messageHandler.setDefaultTopic(defaultTopic)
        return messageHandler
    }

    @Bean
    fun mqttOrderOutboundChannel(): MessageChannel {
        return DirectChannel()
    }
}