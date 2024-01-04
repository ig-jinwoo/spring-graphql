package com.example.graphql.comment

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureHttpGraphQlTester
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.graphql.test.tester.HttpGraphQlTester

@SpringBootTest(properties = [
    "graphql.servlet.enabled=false",
])
@AutoConfigureHttpGraphQlTester
class CommentControllerTest {

    @Autowired
    lateinit var httpGraphQlTester: HttpGraphQlTester


}