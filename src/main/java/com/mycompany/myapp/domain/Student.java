package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Student.
 */
@Entity
@Table(name = "student")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "student")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subcontent", "student" }, allowSetters = true)
    private Set<PendingStudentSubcontent> pendingStudentSubcontents = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "students", "learningPaths" }, allowSetters = true)
    private Exam exam;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Student id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Student name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<PendingStudentSubcontent> getPendingStudentSubcontents() {
        return this.pendingStudentSubcontents;
    }

    public void setPendingStudentSubcontents(Set<PendingStudentSubcontent> pendingStudentSubcontents) {
        if (this.pendingStudentSubcontents != null) {
            this.pendingStudentSubcontents.forEach(i -> i.setStudent(null));
        }
        if (pendingStudentSubcontents != null) {
            pendingStudentSubcontents.forEach(i -> i.setStudent(this));
        }
        this.pendingStudentSubcontents = pendingStudentSubcontents;
    }

    public Student pendingStudentSubcontents(Set<PendingStudentSubcontent> pendingStudentSubcontents) {
        this.setPendingStudentSubcontents(pendingStudentSubcontents);
        return this;
    }

    public Student addPendingStudentSubcontent(PendingStudentSubcontent pendingStudentSubcontent) {
        this.pendingStudentSubcontents.add(pendingStudentSubcontent);
        pendingStudentSubcontent.setStudent(this);
        return this;
    }

    public Student removePendingStudentSubcontent(PendingStudentSubcontent pendingStudentSubcontent) {
        this.pendingStudentSubcontents.remove(pendingStudentSubcontent);
        pendingStudentSubcontent.setStudent(null);
        return this;
    }

    public Exam getExam() {
        return this.exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public Student exam(Exam exam) {
        this.setExam(exam);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        return getId() != null && getId().equals(((Student) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Student{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
