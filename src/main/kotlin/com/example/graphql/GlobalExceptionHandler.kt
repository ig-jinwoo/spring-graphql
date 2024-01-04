package com.example.graphql

import graphql.ErrorClassification
import graphql.GraphQLError
import org.springframework.graphql.data.method.annotation.GraphQlExceptionHandler
import org.springframework.web.bind.annotation.ControllerAdvice
import java.net.BindException
import java.nio.file.AccessDeniedException


@ControllerAdvice
class GlobalExceptionHandler {
    @GraphQlExceptionHandler
    fun handle(ex: BindException): GraphQLError {
        return GraphQLError.newError().errorType(ErrorType.DataFetchingException).message(ex.message).build()
    }

    @GraphQlExceptionHandler
    fun handleAccessDenied(ex: AccessDeniedException): GraphQLError {
        return GraphQLError.newError().errorType(ErrorType.AccessDenied).message(ex.message).build()
    }


    enum class ErrorType : ErrorClassification {
        InvalidSyntax,
        ValidationError,
        DataFetchingException,
        NullValueInNonNullableField,
        OperationNotSupported,
        ExecutionAborted,
        AccessDenied
    }
}