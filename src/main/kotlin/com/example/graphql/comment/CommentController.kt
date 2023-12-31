package com.example.graphql.comment

import com.example.graphql.post.Post
import org.springframework.data.domain.Limit
import org.springframework.data.domain.ScrollPosition
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Window
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.BatchMapping
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.query.ScrollSubrange
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RestController
import java.util.stream.Collectors


@Controller
class CommentController(private val commentService: CommentService) {

    @MutationMapping
    fun createComment(@Argument postId: Long?, @Argument author: String, @Argument text: String): Comment {
        return commentService.createComment(postId!!, author, text)
    }

    @QueryMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    fun commentById(@Argument id: Long?): Comment {
        return commentService.findCommentById(id!!)
    }

    @QueryMapping
    fun commentsByPostId(@Argument postId: Long, subrange: ScrollSubrange): Window<Comment> {
        val scrollPosition = subrange.position().orElse(ScrollPosition.offset())
        val limit = Limit.of(subrange.count().orElse(10))
        val sort = Sort.by("text").ascending()
        return commentService.findCommentsByPostId(postId, scrollPosition, limit, sort)
    }

    @MutationMapping
    fun updateComment(@Argument id: Long?, @Argument text: String): Comment {
        return commentService.updateComment(id!!, text)
    }

    @MutationMapping
    fun deleteComment(@Argument id: Long?): Boolean {
        commentService.deleteComment(id!!)
        return true
    }

}
