package com.wepay.kafka.connect.bigquery.convert;

import org.apache.kafka.common.config.ConfigDef;
import org.apache.kafka.connect.connector.ConnectRecord;
import org.apache.kafka.connect.transforms.Transformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class RecordNameRouter<R extends ConnectRecord<R>> implements Transformation<R> {
    private static final Logger log = LoggerFactory.getLogger(com.wepay.kafka.connect.bigquery.convert.RecordNameRouter.class);
    public static final ConfigDef CONFIG_DEF = new ConfigDef();

    public RecordNameRouter() {
    }

    public void configure(Map<String, ?> props) {
    }

    public R apply(R record) {
        String topicName = normalise(record.topic());
        String recordName = normalise(record.valueSchema().name());

        String newTopicName = topicName + "_" + recordName;

        log.trace("Rerouting from topic '{}' and record '{}' to new topic '{}'",
                record.topic(), record.valueSchema().name(), newTopicName);

        return record.newRecord(
                newTopicName,
                record.kafkaPartition(),
                record.keySchema(),
                record.key(),
                record.valueSchema(),
                record.value(),
                record.timestamp());
    }

    private String normalise(String str) {
        return str.replace(".", "_");
    }

    public void close() {
    }

    public ConfigDef config() {
        return CONFIG_DEF;
    }
}