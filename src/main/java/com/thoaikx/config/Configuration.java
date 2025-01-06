package com.thoaikx.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;


//The "system:properties" source in configuration frameworks
// (like MicroProfile Config, owner, or QConfig) refers to the JVM
// system properties.
// These are key-value pairs that can be passed to the Java application
// at runtime using the -D flag.
@LoadPolicy(LoadType.MERGE)
@Config.Sources({
    "system:properties",
    "classpath:config.properties"})
public interface Configuration  extends Config{

    @Key("target")
    String target();

    @Key("timeout")
    int timeout();

    @Key("port.android")
    int portAndroid();

    @Key("port.ios")
    int portIOS();

    @Key("port")
    int port();


    @Key("auto.report")
    boolean autoReport();
    
} 
