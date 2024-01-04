package com.example.graphql.config

import graphql.schema.DataFetcher
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.idl.SchemaDirectiveWiring
import graphql.schema.idl.SchemaDirectiveWiringEnvironment
import org.springframework.security.core.context.SecurityContextHolder
import java.nio.file.AccessDeniedException


class AuthDirective : SchemaDirectiveWiring {
    override fun onField(environment: SchemaDirectiveWiringEnvironment<GraphQLFieldDefinition>): GraphQLFieldDefinition {
        val targetAuthRole = environment.getAppliedDirective().getArgument("role").getValue() as String

        val field = environment.element
        val parentType = environment.fieldsContainer

        val originalDataFetcher = environment.codeRegistry.getDataFetcher(parentType, field)
        val authDataFetcher = DataFetcher { dataFetchingEnvironment ->
            val auth = SecurityContextHolder.getContext().authentication
            if (auth != null && auth.authorities.any { it.authority == targetAuthRole }) {
                originalDataFetcher.get(dataFetchingEnvironment)
            } else {
                throw AccessDeniedException("필드:[${field.name}]는 [${targetAuthRole}]만 접근할 수 있습니다.")
            }
        }
        environment.getCodeRegistry().dataFetcher(parentType, field, authDataFetcher);
        return field
    }
}