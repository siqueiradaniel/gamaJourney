package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A LearningPath.
 */
@Entity
@Table(name = "learning_path")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LearningPath implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "exam_id")
    private String examId;

    @Column(name = "subcontent_id")
    private String subcontentId;

    @Column(name = "jhi_order")
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "students", "learningPaths" }, allowSetters = true)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "learningPaths", "dependencies", "pendingStudentSubcontents", "content" }, allowSetters = true)
    private Subcontent subcontent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LearningPath id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExamId() {
        return this.examId;
    }

    public LearningPath examId(String examId) {
        this.setExamId(examId);
        return this;
    }

    public void setExamId(String examId) {
        this.examId = examId;
    }

    public String getSubcontentId() {
        return this.subcontentId;
    }

    public LearningPath subcontentId(String subcontentId) {
        this.setSubcontentId(subcontentId);
        return this;
    }

    public void setSubcontentId(String subcontentId) {
        this.subcontentId = subcontentId;
    }

    public Integer getOrder() {
        return this.order;
    }

    public LearningPath order(Integer order) {
        this.setOrder(order);
        return this;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Exam getExam() {
        return this.exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public LearningPath exam(Exam exam) {
        this.setExam(exam);
        return this;
    }

    public Subcontent getSubcontent() {
        return this.subcontent;
    }

    public void setSubcontent(Subcontent subcontent) {
        this.subcontent = subcontent;
    }

    public LearningPath subcontent(Subcontent subcontent) {
        this.setSubcontent(subcontent);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LearningPath)) {
            return false;
        }
        return getId() != null && getId().equals(((LearningPath) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LearningPath{" +
            "id=" + getId() +
            ", examId='" + getExamId() + "'" +
            ", subcontentId='" + getSubcontentId() + "'" +
            ", order=" + getOrder() +
            "}";
    }
}
