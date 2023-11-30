package eu.tasgroup.kafka.serializer;

import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import eu.tasgroup.kafka.dto.PositionDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomSerializer implements Serializer<PositionDTO> {
	
    private final ObjectMapper objectMapper=new ObjectMapper();
    
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public byte[] serialize(String topic, PositionDTO data) {
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