package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PendingStudentSubcontent.
 */
@Entity
@Table(name = "pending_student_subcontent")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PendingStudentSubcontent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "subcontent_id")
    private String subcontentId;

    @NotNull
    @Column(name = "current_status", nullable = false)
    private String currentStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "learningPaths", "dependencies", "pendingStudentSubcontents", "content" }, allowSetters = true)
    private Subcontent subcontent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "pendingStudentSubcontents", "exam" }, allowSetters = true)
    private Student student;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PendingStudentSubcontent id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public PendingStudentSubcontent studentId(String studentId) {
        this.setStudentId(studentId);
        return this;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getSubcontentId() {
        return this.subcontentId;
    }

    public PendingStudentSubcontent subcontentId(String subcontentId) {
        this.setSubcontentId(subcontentId);
        return this;
    }

    public void setSubcontentId(String subcontentId) {
        this.subcontentId = subcontentId;
    }

    public String getCurrentStatus() {
        return this.currentStatus;
    }

    public PendingStudentSubcontent currentStatus(String currentStatus) {
        this.setCurrentStatus(currentStatus);
        return this;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public Subcontent getSubcontent() {
        return this.subcontent;
    }

    public void setSubcontent(Subcontent subcontent) {
        this.subcontent = subcontent;
    }

    public PendingStudentSubcontent subcontent(Subcontent subcontent) {
        this.setSubcontent(subcontent);
        return this;
    }

    public Student getStudent() {
        return this.student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public PendingStudentSubcontent student(Student student) {
        this.setStudent(student);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PendingStudentSubcontent)) {
            return false;
        }
        return getId() != null && getId().equals(((PendingStudentSubcontent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PendingStudentSubcontent{" +
            "id=" + getId() +
            ", studentId='" + getStudentId() + "'" +
            ", subcontentId='" + getSubcontentId() + "'" +
            ", currentStatus='" + getCurrentStatus() + "'" +
            "}";
    }
}
