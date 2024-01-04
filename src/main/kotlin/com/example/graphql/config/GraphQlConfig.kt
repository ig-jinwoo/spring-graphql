package com.example.graphql.config

import com.github.benmanes.caffeine.cache.Caffeine
import graphql.execution.preparsed.PreparsedDocumentEntry
import graphql.execution.preparsed.PreparsedDocumentProvider
import graphql.schema.DataFetcher
import graphql.schema.GraphQLFieldDefinition
import graphql.schema.GraphQLFieldsContainer
import graphql.schema.idl.RuntimeWiring
import graphql.schema.idl.SchemaDirectiveWiring
import graphql.schema.idl.SchemaDirectiveWiringEnvironment
import graphql.validation.rules.OnValidationErrorStrategy
import graphql.validation.rules.ValidationRules
import graphql.validation.schemawiring.ValidationSchemaWiring
import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.graphql.execution.RuntimeWiringConfigurer
import org.springframework.security.core.context.SecurityContextHolder
import java.nio.file.AccessDeniedException


@Configuration
class GraphQlConfig {

    @Bean
    fun graphQlSourceCustomizer(): GraphQlSourceBuilderCustomizer {
        val cache = Caffeine.newBuilder()
            .maximumSize(10_000)
            .build<String, PreparsedDocumentEntry>()

        val preparsedCache = PreparsedDocumentProvider { executionInput, computeFunction ->
            val mapCompute = { key: String -> computeFunction.apply(executionInput) }
            cache.get(executionInput.query, mapCompute)
        }

        return GraphQlSourceBuilderCustomizer { builder ->
            builder.configureGraphQl { graphQlBuilder ->
                graphQlBuilder.preparsedDocumentProvider(preparsedCache)
            }
        }
    }


    @Bean
    fun runtimeWiringConfigurer(): RuntimeWiringConfigurer {
        val validationRules = ValidationRules.newValidationRules()
            .onValidationErrorStrategy(OnValidationErrorStrategy.RETURN_NULL)
            .build()
        val schemaWiring = ValidationSchemaWiring(validationRules)

        return RuntimeWiringConfigurer { builder: RuntimeWiring.Builder ->
            builder.directiveWiring(schemaWiring)
                .directive("auth", AuthDirective()).build()
        }
    }
}