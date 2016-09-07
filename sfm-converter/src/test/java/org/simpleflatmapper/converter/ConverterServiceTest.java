package org.simpleflatmapper.converter;

import org.junit.Test;

import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URL;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

//IFJAVA8_START
import java.time.*;
//IFJAVA8_END
import static org.junit.Assert.*;
import static org.simpleflatmapper.converter.test.ConverterServiceTestHelper.testConverter;

public class ConverterServiceTest {

    public enum ENUM {
        type1, type2
    }

    @Test
    public void testToStringConverter() throws Exception {
        testConverter("Hoy", "Hoy", Object.class, String.class);
    }

    @Test
    public void testNumberToNumberConverter() throws Exception {
        testConverter(13, (byte)13);
        testConverter((byte)13, 13);

        testConverter(13, (short)13);
        testConverter((short)13, 13);

        testConverter(13, 13);
        testConverter(13, 13);

        testConverter(13, (long)13);
        testConverter((long)13, 13);

        testConverter(13, (float)13);
        testConverter((float)13, 13);

        testConverter(13, (double)13);
        testConverter((double)13, 13);


        testConverter(13, new BigDecimal(13));
        testConverter(new BigDecimal(13), 13);
        testConverter(new BigInteger("13"), new BigDecimal("13"));

        testConverter(13, new BigInteger("13"));
        testConverter(new BigDecimal("13"), new BigInteger("13"));
        testConverter(new BigInteger("13"), 13);
    }

    @Test
    public void testChain2Converters() throws Exception {
        testConverter("1", BigDecimal.ONE);
    }

    @Test
    public void testObjectToEnumConverter() throws Exception {
        testConverter(0, ENUM.type1, Object.class, ENUM.class);
        testConverter("type2", ENUM.type2, Object.class, ENUM.class);
        testConverter(null, null, Object.class, ENUM.class);
    }

    @Test
    public void testNumberToEnumConverter() throws Exception {
        testConverter(0, ENUM.type1);
        testConverter(1, ENUM.type2);
        testConverter(null, null, Number.class, ENUM.class);

        try {
            testConverter(2, ENUM.type2);
            fail();
        } catch(IllegalArgumentException e) {

        }
    }

    @Test
    public void testIdentity() throws Exception {
        Object o = new Object();
        testConverter(o, o);
    }

    @Test
    public void testURLConverter() throws Exception {
        testConverter("http://url.net", new URL("http://url.net"));

        try {
            ConverterService.getInstance().findConverter(String.class, URL.class).convert("blop");
            fail();
        } catch(ConversionException e) {
            // expected
        }
    }

    @Test
    public void testCharSequenceConverter() throws Exception {
        testConverter(new StringBuilder("hello"), "hello");
        testConverter("123",     Byte.valueOf((byte)123));
        testConverter("123",     Character.valueOf((char)123));
        testConverter("1234",    Short.valueOf((short)1234));
        testConverter("1234",    Integer.valueOf(1234));
        testConverter("1234",    Long.valueOf(1234));
        testConverter("1234.56", Float.valueOf(1234.56f));
        testConverter("1234.56", Double.valueOf(1234.56));
        testConverter("type2",   ENUM.type2);

        final UUID uuid = UUID.randomUUID();
        testConverter(uuid.toString(), uuid);
    }

    @Test
    public void testNoConverter()  {
        assertNull(ConverterService.getInstance().findConverter(Reader.class, System.class));
    }
}