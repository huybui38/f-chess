package com.example.fchess.interfaces;

import com.example.fchess.enums.eChessPackage;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Type;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PacketHandler {
    eChessPackage packetName();
    Class handler();
    Class dataType();
}
