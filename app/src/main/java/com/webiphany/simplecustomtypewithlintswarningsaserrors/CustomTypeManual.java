package com.webiphany.simplecustomtypewithlintswarningsaserrors;


import com.apollographql.apollo.api.ScalarType;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
// import javax.annotation.Generated;
// import javax.annotation.Generated;

public enum CustomTypeManual implements ScalarType {
    URL {
        @Override
        public String typeName() {
            return "URL";
        }

        @Override
        public Class<?> javaType() {
            return String.class;
        }
    },
}
