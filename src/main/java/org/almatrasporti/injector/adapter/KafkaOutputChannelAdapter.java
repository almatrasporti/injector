package org.almatrasporti.injector.adapter;

import org.almatrasporti.common.models.CANBusMessage;
import org.almatrasporti.common.services.CANBusMessageFormatter;
import org.almatrasporti.common.utils.Config;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Collection;
import java.util.Properties;

public class KafkaOutputChannelAdapter implements IOutputChannelAdapter {

    Producer<String, String> producer;

    public KafkaOutputChannelAdapter() {
        Properties props = new Properties();
        props.put("bootstrap.servers", Config.getInstance().get("Kafka.servers")  != null ? Config.getInstance().get("Kafka.servers") : "localhost:9092");
        props.put("acks", Config.getInstance().get("Kafka.acks") != null ? Config.getInstance().get("Kafka.acks") : "all");
        props.put("retries", Config.getInstance().get("Kafka.retries") != null ? Integer.getInteger(Config.getInstance().get("Kafka.retries")) : 0);
        props.put("linger.ms", Config.getInstance().get("Kafka.linger.ms") != null ? Integer.getInteger(Config.getInstance().get("Kafka.linger.ms")) : 1);
        props.put("key.serializer", Config.getInstance().get("Kafka.key.serializer") != null ? Config.getInstance().get("Kafka.key.serializer") : "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", Config.getInstance().get("Kafka.value.serializer") != null ? Config.getInstance().get("Kafka.value.serializer") : "org.apache.kafka.common.serialization.StringSerializer");

        this.producer = new KafkaProducer<>(props);
    }

    public boolean store(Collection<CANBusMessage> data) {
        data.stream().forEach(message -> this.send(message));
        return true;
    }

    protected void send(CANBusMessage message) {
        String topic = message.getVin();
        this.producer.send(new ProducerRecord<>(topic, CANBusMessageFormatter.toCSV(message)));

        this.producer.flush();
    }
}
