package sa.edu.tuwaiq.planteye.util

import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class RegisterValidationTest {
    private lateinit var registrationValidator: RegisterValidation

    @Before
    fun setUp() {
        registrationValidator = RegisterValidation()
    }

    // Test cases for email validation -------------------------------------------------------------
    @Test
    fun emailIsValidWithInvalidEmailThenReturnFalseValue() {
        val validation = registrationValidator.emailsIsValid("fatima.com")

        // to match the result with the expected result
        assertEquals(false, validation)
    }

    @Test
    fun emailIsValidWithValidEmailThenReturnTrueValue() {
        val validation = registrationValidator.emailsIsValid("fatima21@gmail.com")

        assertEquals(true, validation)
    }

    // Test cases for password validation ----------------------------------------------------------
    @Test
    fun passwordIsValidWithInvalidPasswordThenReturnFalseValue() {
        val validation = registrationValidator.passwordIsValid("128")

        assertEquals(false, validation)
    }

    @Test
    fun passwordIsValidWithValidPasswordThenReturnTrueValue() {
        val validation = registrationValidator.passwordIsValid("8820@Fati22")

        assertEquals(true, validation)
    }
}