package com.ippodakelabs.types;


import com.ippodakelabs.types.inter.IDataType;

public class Varchar implements IDataType {
    static Varchar varchar = null;
    private Varchar()
    {

    }
    public static Varchar get()
    {
        if(varchar == null)
        {
            varchar = new Varchar();
        }
        return varchar;
    }
    @Override
    public boolean match(Class<?> clazz) {
        return String.class.equals(clazz);
    }

    @Override
    public String sqlType() {
        return "VARCHAR(%s)";
    }
    @Override
    public String stringifyType()
    {
        return "\"%s\"";
    }
}
