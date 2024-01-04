package com.example.graphql.post

import org.springframework.data.domain.Limit
import org.springframework.data.domain.ScrollPosition
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Window
import org.springframework.security.access.annotation.Secured
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class PostService(private val postRepository: PostRepository) {

    fun findAllPosts(position: ScrollPosition, limit: Limit, sort: Sort): Window<Post> {
        return postRepository.findAllBy(position, limit, sort)
    }

    fun findPostById(id: Long): Post {
        return postRepository.findById(id).orElseThrow { NoSuchElementException("Post not found with id: $id") }
    }

    @Secured("ROLE_ADMIN")
    fun createPost(title: String, content: String?): Post {
        val post = Post(title = title, content = content)
        return postRepository.save(post)
    }

    fun updatePost(id: Long, title: String, content: String): Post {
        val post = findPostById(id)
        val updatedPost = Post(id = post.id, title = title, content = content)
        return postRepository.save(updatedPost)
    }

    fun deletePost(id: Long) {
        if (postRepository.existsById(id)) {
            postRepository.deleteById(id)
        } else {
            throw NoSuchElementException("Post not found with id: $id")
        }
    }
}
