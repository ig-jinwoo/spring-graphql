package com.example.graphql.data

import com.example.graphql.comment.Comment
import com.example.graphql.comment.CommentRepository
import com.example.graphql.post.Post
import com.example.graphql.post.PostRepository
import net.datafaker.Faker
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class DataLoader {

    @Bean
    fun initData(postRepository: PostRepository, commentRepository: CommentRepository): CommandLineRunner {
        return CommandLineRunner { args: Array<String?>? ->
            val faker = Faker()

            (1..50).forEach { _ ->
                // 포스트 생성
                val title = faker.book().title()
                val content = faker.name().title()
                val post = Post(title = title, content = content)

                // 포스트 저장
                postRepository.save(post)

                // 각 포스트에 대한 3개의 코멘트 생성 및 저장
                (1..3).forEach { _ ->
                    val commentText = faker.lorem().sentence()
                    val comment = Comment(text = commentText, post = post)
                    commentRepository.save(comment)
                }
            }
        }
    }
}