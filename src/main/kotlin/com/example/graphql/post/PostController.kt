package com.example.graphql.post

import com.example.graphql.comment.Comment
import com.example.graphql.comment.CommentService
import org.dataloader.DataLoader
import org.springframework.data.domain.Limit
import org.springframework.data.domain.ScrollPosition
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Window
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.BatchMapping
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.graphql.data.method.annotation.SchemaMapping
import org.springframework.graphql.data.query.ScrollSubrange
import org.springframework.graphql.execution.BatchLoaderRegistry
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Controller
import reactor.core.publisher.Mono
import java.util.concurrent.CompletableFuture
import java.util.stream.Collectors

@Controller
class PostController(
    private val postService: PostService,
    private val commentService: CommentService,
    registry: BatchLoaderRegistry
) {
    init {
        registry.forTypePair(Long::class.java, List::class.java)
            .registerMappedBatchLoader { postIds, env ->
                Mono.just(commentService.findCommentsByPostIds(postIds.stream().toList()).groupBy { it.post!!.id })
            }
    }

    @QueryMapping
    fun posts(subrange: ScrollSubrange): Window<Post> {
        val scrollPosition = subrange.position().orElse(ScrollPosition.offset())
        val limit = Limit.of(subrange.count().orElse(10))
        val sort = Sort.by("title").ascending()
        return postService.findAllPosts(scrollPosition, limit, sort)
    }

    @QueryMapping
    fun postById(@Argument id: Long): Post {
        return postService.findPostById(id)
    }

    @MutationMapping
    fun createPost(@Argument title: String, @Argument content: String?): Post {
        return postService.createPost(title, content)
    }

    /**
    @BatchMapping
    fun comments(posts: List<Post>): Map<Post, List<Comment>> {

        val postIds: MutableList<Long> = posts.stream().map { posts -> posts!!.id }.collect(Collectors.toList())
        val comments: List<Comment> = commentService.findCommentsByPostIds(postIds)
        val commentsByPost: Map<Post, List<Comment>> = comments.groupBy { it.post }

        return commentsByPost
    }
    **/

    @SchemaMapping
    fun comments(post: Post, dataLoader: DataLoader<Long, List<Comment>>): CompletableFuture<List<Comment>> {
        return dataLoader.load(post.id)
    }
}