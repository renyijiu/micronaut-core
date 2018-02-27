package org.particleframework.openapi

import groovy.transform.CompileStatic
import org.particleframework.http.annotation.Body
import org.particleframework.http.annotation.CookieValue
import org.particleframework.http.annotation.Header

import java.lang.annotation.Annotation
import java.lang.reflect.Parameter

@CompileStatic
class OpenApiParameter {
    OpenApiParameterIn paramIn
    String name
    OpenApiParameterSchema schema
    boolean required

    static OpenApiParameter of(String path, Parameter parameter, Class paramClass, Annotation[] annotations) {
        OpenApiParameter swaggerParameter = new OpenApiParameter()
        swaggerParameter.with {
            paramIn = OpenApiParameterIn.of(path, parameter, annotations)
            name = name(parameter, annotations)
            required = !(paramClass == Optional)
            schema = OpenApiParameterSchema.of(paramClass)
        }
        swaggerParameter
    }

    static String name(Parameter parameter, Annotation[] annotations) {
        if ( annotations != null ) {
            for ( Annotation annotation : annotations ) {
                if ( annotation instanceof CookieValue) {
                    CookieValue cookieValueAnnotation = (CookieValue) annotation
                    String value = cookieValueAnnotation.value()
                    if ( value != null ) {
                        return value
                    }
                }
                if ( annotation instanceof Header) {
                    Header headerAnnotation = (Header) annotation
                    String value = headerAnnotation.value()
                    if ( value != null ) {
                        return value
                    }
                }
                if ( annotation instanceof Body) {
                    Body headerAnnotation = (Body) annotation
                    String value = headerAnnotation.value()
                    if ( value != null ) {
                        return value
                    }
                }
                if ( annotation instanceof org.particleframework.http.annotation.Parameter) {
                    org.particleframework.http.annotation.Parameter parameterAnnotation = (org.particleframework.http.annotation.Parameter) annotation
                    String value = parameterAnnotation.value()
                    if ( value != null ) {
                        return value
                    }
                }
            }
        }
        parameter.name
    }

    Map toMap() {
        Map m = [:]
        m['required'] = required
        if ( name ) {
            m['name'] = name
        }
        if ( paramIn ) {
            m['in'] = paramIn.name().toLowerCase()
        }
        if  ( schema ) {
            m['type'] = schema.type
        }
        /*
        Map schemaMap = schema.topMap()
        if ( schemaMap ) {
            m['schema'] = schemaMap
        }
        */
        m
    }
}
