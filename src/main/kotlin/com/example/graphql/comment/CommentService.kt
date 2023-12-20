package com.example.graphql.comment

import com.example.graphql.post.PostService
import org.springframework.stereotype.Service

@Service
class CommentService(private val commentRepository: CommentRepository, private val postService: PostService) {

    fun findAllComments(): List<Comment> {
        return commentRepository.findAll()
    }

    fun findCommentById(id: Long): Comment {
        return commentRepository.findById(id).orElseThrow { NoSuchElementException("Comment not found with id: $id") }
    }

    fun createComment(postId: Long, text: String): Comment {
        val post = postService.findPostById(postId)
        val comment = Comment(text = text, post = post)
        return commentRepository.save(comment)
    }

    fun updateComment(id: Long, text: String): Comment {
        val comment = findCommentById(id)
        val updatedComment = comment.copy(text = text)
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
}
