package it.poste.patrimonio.kproducer.serializer;

import java.util.Map;

import org.apache.commons.lang3.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomSerializer implements Serializer<Object> {
	
    private final ObjectMapper objectMapper=new ObjectMapper();
    
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, Object data) {
        try {
            if (data == null){
                log.error("Null received at serializing");
                return null;
            }
            log.info("Serializing...");
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error when serializing PositionDTO to byte[]");
        }
    }

    @Override
    public void close() {
    }
    
}