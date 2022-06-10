package com.example.mobileappproject.extensions

import org.junit.Test

import org.junit.Assert.*

internal class FormValidatorKtTest {

    @Test
    fun checkIsEmailTrue() {
        val result: Boolean = FormValidator().checkIsEmail("abc@123.com")
        assertEquals(true, result)
    }

    @Test
    fun checkIsEmailFalse() {
        val result: Boolean = FormValidator().checkIsEmail("abc@123")
        assertEquals(false, result)
    }

    @Test
    fun checkIsEmailEdge() {
        val result: Boolean = FormValidator().checkIsEmail("")
        assertEquals(false, result)
    }

    @Test
    fun checkIsPassword() {
        val result = FormValidator().checkIsPassword("abc123")
        assertEquals(true, result)
    }

    @Test
    fun checkIsPasswordFalse() {
        val result = FormValidator().checkIsPassword("abc1231231123123123123")
        assertEquals(false, result)
    }

    @Test
    fun checkIsPasswordEdge() {
        val result = FormValidator().checkIsPassword("")
        assertEquals(false, result)
    }

    @Test
    fun checkIsNotEmpty() {
        val result = FormValidator().checkIsNotEmpty("notEmpty")
        assertEquals(true, result)

    }

    @Test
    fun checkIsNotEmptyFalse() {
        val result = FormValidator().checkIsNotEmpty("")
        assertEquals(false, result)
    }
}