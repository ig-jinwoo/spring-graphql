package com.example.graphql.comment

import org.springframework.data.domain.Limit
import org.springframework.data.domain.ScrollPosition
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Window
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor
import org.springframework.data.repository.CrudRepository
import org.springframework.graphql.data.GraphQlRepository

@GraphQlRepository
interface CommentRepository : JpaRepository<Comment, Long> {
    fun findByPostIdIn(postIds: List<Long>): List<Comment>


    fun findByPostIdIn(postIds: List<Long>, position: ScrollPosition, limit: Limit, sort: Sort): Window<Comment>
}