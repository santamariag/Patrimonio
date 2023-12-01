package it.poste.patrimonio.kproducer.serializer;

import org.apache.kafka.common.serialization.Serializer;

import com.fasterxml.jackson.annotation.JsonTypeName;

import io.dropwizard.kafka.serializer.SerializerFactory;

@JsonTypeName("custom-serializer")
public class CustomSerializerFactory extends SerializerFactory {


    @Override
    public Class<? extends Serializer<?>> getSerializerClass() {
        return CustomSerializer.class;
    }
}