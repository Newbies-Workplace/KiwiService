package pl.teamkiwi.security

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class PasswordEncoderTest {

    private val passwordEncoder = PasswordEncoder()

    @Test
    fun `encode should hash password with bcrypt cost set to 12`() {
        //given
        val password = "test"

        //when
        val encoded = passwordEncoder.encode(password)

        //then
        assertTrue { passwordEncoder.isValid(password, encoded) }
    }

    @Test
    fun `isValid should return true if password is valid`() {
        //given
        val password = "test"
        val testHash = "\$2a\$12\$LMDz4Xs.2Rn3sU5HkOBwce/OLVmhM2uYIOsNK4TUp7809nbP0Qle." //hashed 'test'

        //when
        val isValid = passwordEncoder.isValid(password, testHash)

        //then
        assertTrue(isValid)
    }

    @Test
    fun `isValid should return false if password is invalid`() {
        //given
        val password = "notTest"
        val testHash = "\$2a\$12\$LMDz4Xs.2Rn3sU5HkOBwce/OLVmhM2uYIOsNK4TUp7809nbP0Qle." //hashed 'test'

        //when
        val isValid = passwordEncoder.isValid(password, testHash)

        //then
        assertFalse(isValid)
    }
}