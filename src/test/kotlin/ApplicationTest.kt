package com.jdulfer

import com.jdulfer.config.module
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ApplicationTest {

    @Test
    fun `test GET hello returns 200`() = testApplication {
        application {
            module()
        }

        val response = client.get("/hello")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("Hello, World!", response.bodyAsText())
    }

    @Test
    fun `test GET investors returns 200 with correct data`() = testApplication {
        application {
            module()
        }

        val response = client.get("/investors")

        assertEquals(HttpStatusCode.OK, response.status)
        val responseText = response.bodyAsText()
        val jsonResponse = Json.parseToJsonElement(responseText).jsonArray

        assertTrue(jsonResponse.isNotEmpty())
        val firstInvestor = jsonResponse[0].jsonObject

        assertTrue(firstInvestor.containsKey("id"))
        assertTrue(firstInvestor.containsKey("name"))
        assertTrue(firstInvestor.containsKey("investoryType"))
        assertTrue(firstInvestor.containsKey("country"))
        assertTrue(firstInvestor.containsKey("dateAdded"))
        assertTrue(firstInvestor.containsKey("lastUpdated"))
        assertTrue(firstInvestor.containsKey("commitmentTotal"))
    }

    @Test
    fun `test GET investor commitments returns 200 with correct data`() = testApplication {
        application {
            module()
        }

        val response = client.get("/investors/1/commitments")

        assertEquals(HttpStatusCode.OK, response.status)
        val responseText = response.bodyAsText()
        val jsonResponse = Json.parseToJsonElement(responseText).jsonArray

        if (jsonResponse.isNotEmpty()) {
            val firstCommitment = jsonResponse[0].jsonObject
            assertTrue(firstCommitment.containsKey("id"))
            assertTrue(firstCommitment.containsKey("amount"))
            assertTrue(firstCommitment.containsKey("currency"))
            assertTrue(firstCommitment.containsKey("assetClass"))
        }
    }

    @Test
    fun `test GET investor commitments with invalid ID returns 400`() = testApplication {
        application {
            module()
        }

        val response = client.get("/investors/invalid/commitments")

        assertEquals(HttpStatusCode.BadRequest, response.status)
        val responseText = response.bodyAsText()
        assertTrue(responseText.contains("Investor id must be a number"))
    }

    @Test
    fun `test GET investor commitments with non-existent ID returns 404`() = testApplication {
        application {
            module()
        }

        val response = client.get("/investors/999/commitments")

        assertEquals(HttpStatusCode.NotFound, response.status)
        val responseText = response.bodyAsText()
        assertTrue(responseText.contains("Could not find commitments for investor 999"))
    }
}