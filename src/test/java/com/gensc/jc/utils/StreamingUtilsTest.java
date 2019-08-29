package com.gensc.jc.utils;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.spark.streaming.api.java.JavaInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class StreamingUtilsTest {

    StreamingUtils streamingUtils = new StreamingUtils();

    @Test
    public void getJavaStreamingContext() {

        JavaStreamingContext streamingContext = streamingUtils.getJavaStreamingContext();

        assertThat(streamingContext, instanceOf(JavaStreamingContext.class));
        assertThat(streamingContext.getState().toString(), equalTo("INITIALIZED"));
    }
/*
    @Test
    public void getConsumerRecordInputDStream() {
        JavaStreamingContext sc = streamingUtils.getJavaStreamingContext();
        JavaInputDStream<ConsumerRecord<String, String>> consumerRecordInputDStream = streamingUtils.getConsumerRecordInputDStream(sc);

        assertThat(consumerRecordInputDStream, instanceOf(JavaInputDStream.class));
        assertThat(consumerRecordInputDStream.toString(), containsString("org.apache.spark.streaming.api.java.JavaInputDStream@"));
    } */
}