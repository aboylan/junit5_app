package org.aboylan.junit5app.ejemplos.models;

import org.aboylan.junit5app.ejemplos.exceptions.DineroInsuficienteException;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CuentaTest {

    Cuenta cuenta;

    @BeforeEach
    void initMetodoTest() {
        this.cuenta = new Cuenta("Alejandro", new BigDecimal("1000.12345"));

        System.out.println("iniciando el metodo");
    }

    @AfterEach
    void tearDown() {
        System.out.println("finalizando el metodo de prueba");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("inicializando el test");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("finalizando el test");
    }

    @Test
    @DisplayName("probando nombre de la cuenta corriente!")
    void testNombreCuenta() {
//        cuenta.setPersona("Alejandro");

        String esperado = "Alejandro";
        String real = cuenta.getPersona();

        assertNotNull(real, () -> "la cuenta no puede ser nula");
        assertEquals(esperado, real, () -> "el nombre de la cuenta no es el que se esperaba: se esperaba " + esperado
                + " sin embargo fue " + real);
        assertTrue(real.equals("Alejandro"), () -> "nombre cuenta esperada debe ser igual a la real");
    }

    @Test
    @DisplayName("probando el saldo de la cuenta corriente, que no sea null, mayor que cero, valur esperado")
    void testSaldoCuenta() {
        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0);
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @Test
    @DisplayName("testeando referencias que sean iguales con el metodo equals.")
    void testReferenciaCuenta() {
        cuenta = new Cuenta("John Doe", new BigDecimal("8900.9997"));
        Cuenta cuenta2 = new Cuenta("John Doe", new BigDecimal("8900.9997"));

//        assertNotEquals(cuenta2, cuenta);
        assertEquals(cuenta2, cuenta);
    }

    @Test
    void testDebitoCuenta() {
        cuenta.debito(new BigDecimal(100));

        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCreditoCuenta() {
        cuenta.credito(new BigDecimal(100));

        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.12345", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficienteExceptionCuenta() {
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal(1500));
        });
        String actual = exception.getMessage();
        String esperado = "Dinero Insuficiente";
        assertEquals(esperado, actual);
    }

    @Test
    void testTransferirDineroCuentas() {
        Cuenta cuenta1 = new Cuenta("John Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Alejandro", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.setNombre("Banco del Estado");
        banco.transferir(cuenta2, cuenta1, new BigDecimal("500"));
        assertEquals("1000.8989", cuenta2.getSaldo().toString());
        assertEquals("3000", cuenta1.getSaldo().toString());
    }

    @Test
    //@Disabled
    @DisplayName("probando relaciones entre las cuentas y el banco con assertAll.")
    void testRelacionBancoCuentas() {
        //fail();
        Cuenta cuenta1 = new Cuenta("Jhon Doe", new BigDecimal("2500"));
        Cuenta cuenta2 = new Cuenta("Alejandro", new BigDecimal("1500.8989"));

        Banco banco = new Banco();
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);
        banco.setNombre("Banco del Estado");

        banco.transferir(cuenta2, cuenta1, new BigDecimal("500"));
        assertAll(
                () -> assertEquals("1000.8989", cuenta2.getSaldo().toPlainString(),
                        () -> "el valor del saldo de la cuenta2 no es el esperado"),
                () -> assertEquals("3000", cuenta1.getSaldo().toString(),
                        () -> "el valor del saldo de la cuenta1 no es el esperado"),
                () -> assertEquals(2, banco.getCuentas().size(),
                        () -> "el banco no tiene las cuentas esperadas"),
                () -> assertEquals("Banco del Estado", cuenta1.getBanco().getNombre()),
                () -> assertEquals("Alejandro", banco.getCuentas().stream()
                        .filter(c -> c.getPersona().equals("Alejandro"))
                        .findFirst().get().getPersona()),
                () -> assertTrue(banco.getCuentas().stream().anyMatch(c -> c.getPersona().equals("Alejandro")))
        );
    }
}