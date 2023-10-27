package org.aboylan.junit5app.ejemplos.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class CuentaTest {

    @Test
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta("Alejandro", new BigDecimal("1000.12345"));
//        cuenta.setPersona("Alejandro");

        String esperado = "Alejandro";
        String real = cuenta.getPersona();

        assertEquals(esperado, real);
        assertTrue(real.equals("Alejandro"));
    }
}