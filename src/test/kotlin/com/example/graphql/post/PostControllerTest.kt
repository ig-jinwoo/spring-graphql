package com.example.graphql.post

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.HttpGraphQlTester
import org.springframework.security.test.context.support.WithMockUser


@SpringBootTest(properties = [
    "graphql.servlet.enabled=false",
])
@AutoConfigureHttpGraphQlTester
class PostControllerTest {

    @Autowired
    lateinit var httpGraphQlTester: HttpGraphQlTester

    @Test
    @WithMockUser(username="admin",roles=["ADMIN"])
    fun createPost() {
        val post: Post = httpGraphQlTester
            .document(
                """
                mutation {
                  createPost(title: "sample", content: "Hi") {
                    id
                    title
                    content
                  }
                }
                """.trimIndent()
            )
            .execute()
            .errors()
            .verify()
            .path("createPost")
            .entity(Post::class.java)
            .get()

        assertEquals("sample", post.title)
        assertEquals("Hi", post.content)
    }

    @Test
    @WithMockUser(username="user",roles=["USER"])
    fun createPostErrorTest() {
        httpGraphQlTester
            .document(
                """
                mutation {
                  createPost(title: "sample", content: "Hi") {
                    id
                    title
                    content
                  }
                }
                """.trimIndent()
            )
            .execute()
            .errors()
            .expect { error ->
                error.message!!.contains("Forbidden")
            }
    }
}