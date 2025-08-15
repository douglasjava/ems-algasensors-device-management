package com.algaworks.algasensores.device.management.common;

import io.hypersistence.tsid.TSID;

import java.util.Optional;

public class IDGenerator {

    private static final TSID.Factory factory;

    public static final String TS_ID_NODE = "tsid.node";
    public static final String TS_ID_NODE_COUNT = "tsid.node.count";

    static {
        Optional.ofNullable(System.getenv(TS_ID_NODE)).ifPresent(tsidNode -> System.setProperty(TS_ID_NODE, tsidNode));
        Optional.ofNullable(System.getenv(TS_ID_NODE_COUNT)).ifPresent(tsidNodeCount -> System.setProperty(TS_ID_NODE_COUNT, tsidNodeCount));

        factory = new TSID.Factory.Builder().build();
    }

    private IDGenerator() {

    }

    public static TSID generate() {
        return factory.generate();
    }

}
