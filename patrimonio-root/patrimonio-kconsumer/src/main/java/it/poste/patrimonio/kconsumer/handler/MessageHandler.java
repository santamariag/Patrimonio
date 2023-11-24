package it.poste.patrimonio.kconsumer.handler;

public interface MessageHandler {

	void start();
	void stop();
	void dispose(byte[] message);

}
