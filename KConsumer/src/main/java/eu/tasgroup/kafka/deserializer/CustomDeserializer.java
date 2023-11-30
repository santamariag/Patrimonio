package eu.tasgroup.kafka.deserializer;

import java.util.Map;

import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;


import com.fasterxml.jackson.databind.ObjectMapper;

import eu.tasgroup.kafka.dto.PositionDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomDeserializer implements Deserializer<PositionDTO> {
    
    private final ObjectMapper objectMapper= new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
    }

    @Override
    public PositionDTO deserialize(String topic, byte[] data) {
        try {
            if (data == null){
                log.error("Null received at deserializing");
                return null;
            }
            log.info("Deserializing...");
            return objectMapper.readValue(new String(data, "UTF-8"), PositionDTO.class);
        } catch (Exception e) {
            throw new SerializationException("Error when deserializing byte[] to PositionDTO");
        }
    }

    @Override
    public void close() {
    }
}