package com.algaworks.algasensores.device.management;

import com.algaworks.algasensores.device.management.common.IDGenerator;
import io.hypersistence.tsid.TSID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TSIDTest {


    @Test
    void shouldGenerateTSID() {

        TSID fast = TSID.fast(); //Nao deve ser usado em producao, apenas para testes
        System.out.println(fast);
        System.out.println(fast.toLong());
        System.out.println(fast.getInstant());

    }

    @Test
    void shouldGenerateTSID_1() {

        System.setProperty("tsid.node", "7");
        System.setProperty("tsid.node.count", "32");

        TSID.Factory factory = TSID.Factory.builder().build();
        TSID fast = factory.generate();

        System.out.println(fast);
        System.out.println(fast.toLong());
        System.out.println(fast.getInstant());

    }

    @Test
    void shouldGenerateTSID_2() {

        TSID fast = IDGenerator.generate();

        Assertions.assertNotNull(fast);
        Assertions.assertNotNull(fast.getInstant());
        Assertions.assertTrue(fast.getInstant().toEpochMilli() > 0);
        Assertions.assertTrue(fast.getInstant().isBefore(java.time.Instant.now()));


        System.out.println(fast);
        System.out.println(fast.toLong());
        System.out.println(fast.getInstant());

    }


}
