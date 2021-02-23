package com.example.hometask1.model.base;


import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity {
    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdDate;
    @UpdateTimestamp
    @Column
    private ZonedDateTime lastModifiedDate;

    @Version
    private Long version;
}