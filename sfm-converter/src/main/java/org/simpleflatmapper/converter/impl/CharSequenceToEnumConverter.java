package org.simpleflatmapper.converter.impl;

import org.simpleflatmapper.converter.Converter;
import org.simpleflatmapper.util.EnumHelper;

public class CharSequenceToEnumConverter<E extends Enum<E>> implements Converter<CharSequence, E> {
    private final Class<E> enumClass;
    private final E[] values;

    public CharSequenceToEnumConverter(Class<E> enumClass) {
        this.enumClass = enumClass;
        this.values = EnumHelper.getValues(enumClass);
    }

    @Override
    public E convert(CharSequence in) throws Exception {
        if (in == null || in.length() == 0) return null;
        
        char c = in.charAt(0);
        if (Character.isDigit(c)) {
            int i = Integer.parseInt(in.toString());
            if (i < 0 || i >= values.length) {
                throw new IllegalArgumentException("Invalid ordinal value " + in  + " for " + enumClass);
            }
            return values[i];
        }
        
        return Enum.valueOf(enumClass, in.toString());
    }
}
