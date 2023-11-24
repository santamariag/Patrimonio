package it.poste.patrimonio.kconsumer.handler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LogMessageHandler implements MessageHandler{

	@Override
	public void start() {
		log.info("Handler is started");
	}

	@Override
	public void stop() {
		log.info("Handler is stopped");
	}

	@Override
	public void dispose(byte[] message) {
		log.info(new String(message) + " is diposed.");
	}

}
