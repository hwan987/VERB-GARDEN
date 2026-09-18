package com.example

import com.example.data.model.FlowerType
import com.example.data.model.Verb
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class VerbLogicTest {

    @Test
    fun `test verb dataset contains required irregular verbs`() {
        val verbs = Verb.DEFAULT_VERBS
        assertTrue("Verb bank should contain at least 15 verbs", verbs.size >= 15)

        val goVerb = verbs.find { it.base == "go" }
        assertTrue("Go verb must exist", goVerb != null)
        assertEquals("went", goVerb?.past)
        assertEquals("gone", goVerb?.pastParticiple)
    }

    @Test
    fun `test generatePastOptions contains correct past form`() {
        val verbs = Verb.DEFAULT_VERBS
        for (verb in verbs) {
            val options = verb.generatePastOptions()
            assertEquals("Must generate exactly 3 options", 3, options.size)
            assertTrue("Options must contain the correct past form", options.contains(verb.past))
        }
    }

    @Test
    fun `test generateParticipleOptions contains correct participle form`() {
        val verbs = Verb.DEFAULT_VERBS
        for (verb in verbs) {
            val options = verb.generateParticipleOptions()
            assertEquals("Must generate exactly 3 options", 3, options.size)
            assertTrue("Options must contain the correct past participle form", options.contains(verb.pastParticiple))
        }
    }

    @Test
    fun `test flower types have valid attributes`() {
        for (type in FlowerType.values()) {
            assertTrue(type.koreanName.isNotEmpty())
            assertTrue(type.englishName.isNotEmpty())
            assertTrue(type.petalCount >= 4)
        }
    }
}
