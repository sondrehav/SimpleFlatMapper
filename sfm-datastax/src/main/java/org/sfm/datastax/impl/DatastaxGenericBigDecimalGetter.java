package org.sfm.datastax.impl;

import com.datastax.driver.core.DataType;
import com.datastax.driver.core.GettableData;
import org.sfm.reflect.Getter;

import java.math.BigDecimal;
import java.math.BigInteger;

public class DatastaxGenericBigDecimalGetter implements Getter<GettableData, BigDecimal> {

    private final int index;
    private final DataType.Name dataTypeName;

    public DatastaxGenericBigDecimalGetter(int index, DataType dataType) {
        this.index = index;
        this.dataTypeName = validateName(dataType);
    }

    private DataType.Name validateName(DataType dataType) {

        final DataType.Name name = dataType.getName();
        switch (name) {
            case BIGINT:
            case VARINT:
            case INT:
            case DECIMAL:
            case FLOAT:
            case DOUBLE:
            case COUNTER:
            return name;
        }
        throw new IllegalArgumentException("Datatype " + dataType + " not a number");
    }

    @Override
    public BigDecimal get(GettableData target) throws Exception {
        if (target.isNull(index)) {
            return null;
        }
        switch (dataTypeName) {
            case BIGINT:
            case COUNTER:
                return BigDecimal.valueOf(target.getLong(index));
            case VARINT:
                return new BigDecimal(target.getVarint(index));
            case INT:
                return BigDecimal.valueOf(target.getInt(index));
            case DECIMAL:
                return target.getDecimal(index);
            case FLOAT:
                return BigDecimal.valueOf(target.getFloat(index));
            case DOUBLE:
                return BigDecimal.valueOf(target.getDouble(index));
        }
        return null;
    }
}
