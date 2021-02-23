package com.example.hometask1.aspects.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CrudOperation {

    Operation operation();

    enum Operation {
        INSERT("insert operation"),
        UPDATE("update operation"),
        DELETE("delete operation");

        private final String message;

        Operation(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

}
