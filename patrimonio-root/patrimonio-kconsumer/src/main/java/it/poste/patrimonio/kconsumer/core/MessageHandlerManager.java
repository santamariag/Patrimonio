package it.poste.patrimonio.kconsumer.core;

import java.util.List;

import io.dropwizard.lifecycle.Managed;
import it.poste.patrimonio.kconsumer.handler.MessageHandler;

public class MessageHandlerManager implements Managed {
    private List<MessageHandler> messageHandlers;

    public MessageHandlerManager(List<MessageHandler> messageHandlers) {
        this.messageHandlers = messageHandlers;
    }

    @Override
    public void start() throws Exception {
        for (MessageHandler messagehandler: messageHandlers) {
            messagehandler.start();
        }
    }

    @Override
    public void stop() throws Exception {
        for (MessageHandler messagehandler : messageHandlers) {
            messagehandler.stop();
        }
    }
}