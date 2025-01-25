package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Exam.
 */
@Entity
@Table(name = "exam")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Exam implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "exam")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "pendingStudentSubcontents", "exam" }, allowSetters = true)
    private Set<Student> students = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "exam")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "exam", "subcontent" }, allowSetters = true)
    private Set<LearningPath> learningPaths = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Exam id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Exam name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Exam description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Student> getStudents() {
        return this.students;
    }

    public void setStudents(Set<Student> students) {
        if (this.students != null) {
            this.students.forEach(i -> i.setExam(null));
        }
        if (students != null) {
            students.forEach(i -> i.setExam(this));
        }
        this.students = students;
    }

    public Exam students(Set<Student> students) {
        this.setStudents(students);
        return this;
    }

    public Exam addStudent(Student student) {
        this.students.add(student);
        student.setExam(this);
        return this;
    }

    public Exam removeStudent(Student student) {
        this.students.remove(student);
        student.setExam(null);
        return this;
    }

    public Set<LearningPath> getLearningPaths() {
        return this.learningPaths;
    }

    public void setLearningPaths(Set<LearningPath> learningPaths) {
        if (this.learningPaths != null) {
            this.learningPaths.forEach(i -> i.setExam(null));
        }
        if (learningPaths != null) {
            learningPaths.forEach(i -> i.setExam(this));
        }
        this.learningPaths = learningPaths;
    }

    public Exam learningPaths(Set<LearningPath> learningPaths) {
        this.setLearningPaths(learningPaths);
        return this;
    }

    public Exam addLearningPath(LearningPath learningPath) {
        this.learningPaths.add(learningPath);
        learningPath.setExam(this);
        return this;
    }

    public Exam removeLearningPath(LearningPath learningPath) {
        this.learningPaths.remove(learningPath);
        learningPath.setExam(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Exam)) {
            return false;
        }
        return getId() != null && getId().equals(((Exam) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Exam{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
