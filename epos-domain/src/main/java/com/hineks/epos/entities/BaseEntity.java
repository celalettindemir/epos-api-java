package com.hineks.epos.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

@MappedSuperclass
public class BaseEntity {

    @Id
    @Getter
    @Setter
    @GeneratedValue
    @Column(name = "id")
    @Type(type="org.hibernate.type.UUIDCharType")
    private UUID id;

    @Column(name = "createdDate", nullable = false)
    private Date createdDate;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date date) {
        this.createdDate = date;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "createdById")
    private Person createdBy;

    public Person getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Person createdBy) {
        this.createdBy = createdBy;
    }

    @Column(name = "updatedDate")
    private Date updatedDate;

    public Date getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Date date) {
        this.updatedDate = date;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updatedById")
    private Person updatedBy;

    public Person getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(Person updatedBy) {
        this.updatedBy = updatedBy;
    }

    @Column(name = "isDeleted", nullable = false)
    private boolean isDeleted = false;

    public boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
}
