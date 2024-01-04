package com.example.graphql.comment

import com.example.graphql.post.PostService
import org.springframework.data.domain.Limit
import org.springframework.data.domain.ScrollPosition
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Window
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service
import java.util.*
import kotlin.NoSuchElementException

@Service
class CommentService(private val commentRepository: CommentRepository, private val postService: PostService) {

    fun findAllComments(): List<Comment> {
        return commentRepository.findAll()
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun findCommentById(id: Long): Comment {
        return commentRepository.findById(id).orElseThrow { NoSuchElementException("Comment not found with id: $id") }
    }

    fun createComment(postId: Long, author:String, text: String): Comment {
        val post = postService.findPostById(postId)
        val comment = Comment(text = text, author = author, post = post)
        return commentRepository.save(comment)
    }

    fun updateComment(id: Long, text: String): Comment {
        val comment = findCommentById(id)
        val updatedComment = Comment(id = comment.id, text = text, post = comment.post, author = comment.author)
        return commentRepository.save(updatedComment)
    }

    fun deleteComment(id: Long) {
        if (commentRepository.existsById(id)) {
            commentRepository.deleteById(id)
        } else {
            throw NoSuchElementException("Comment not found with id: $id")
        }
    }

    fun findCommentsByPostIds(postIds: List<Long>): List<Comment> {
        return commentRepository.findByPostIdIn(postIds)
    }

    fun findCommentsByPostId(postId: Long, position: ScrollPosition, limit: Limit, sort: Sort): Window<Comment> {
        return commentRepository.findByPostIdIn(Collections.singletonList(postId), position, limit, sort)
    }
}
