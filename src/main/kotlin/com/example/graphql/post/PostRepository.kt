package com.example.graphql.post

import org.springframework.data.domain.Limit
import org.springframework.data.domain.ScrollPosition
import org.springframework.data.domain.Sort
import org.springframework.data.domain.Window
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.graphql.data.GraphQlRepository

@GraphQlRepository
interface PostRepository : JpaRepository<Post, Long> {
    fun findAllBy(position: ScrollPosition, limit: Limit, sort: Sort): Window<Post>
}