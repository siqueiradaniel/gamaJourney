package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Subcontent.
 */
@Entity
@Table(name = "subcontent")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Subcontent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    @Column(name = "id")
    private String id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subcontent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "exam", "subcontent" }, allowSetters = true)
    private Set<LearningPath> learningPaths = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subcontent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subcontent" }, allowSetters = true)
    private Set<Dependency> dependencies = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subcontent")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "subcontent", "student" }, allowSetters = true)
    private Set<PendingStudentSubcontent> pendingStudentSubcontents = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "subcontents" }, allowSetters = true)
    private Content content;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Subcontent id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Subcontent name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Subcontent description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<LearningPath> getLearningPaths() {
        return this.learningPaths;
    }

    public void setLearningPaths(Set<LearningPath> learningPaths) {
        if (this.learningPaths != null) {
            this.learningPaths.forEach(i -> i.setSubcontent(null));
        }
        if (learningPaths != null) {
            learningPaths.forEach(i -> i.setSubcontent(this));
        }
        this.learningPaths = learningPaths;
    }

    public Subcontent learningPaths(Set<LearningPath> learningPaths) {
        this.setLearningPaths(learningPaths);
        return this;
    }

    public Subcontent addLearningPath(LearningPath learningPath) {
        this.learningPaths.add(learningPath);
        learningPath.setSubcontent(this);
        return this;
    }

    public Subcontent removeLearningPath(LearningPath learningPath) {
        this.learningPaths.remove(learningPath);
        learningPath.setSubcontent(null);
        return this;
    }

    public Set<Dependency> getDependencies() {
        return this.dependencies;
    }

    public void setDependencies(Set<Dependency> dependencies) {
        if (this.dependencies != null) {
            this.dependencies.forEach(i -> i.setSubcontent(null));
        }
        if (dependencies != null) {
            dependencies.forEach(i -> i.setSubcontent(this));
        }
        this.dependencies = dependencies;
    }

    public Subcontent dependencies(Set<Dependency> dependencies) {
        this.setDependencies(dependencies);
        return this;
    }

    public Subcontent addDependency(Dependency dependency) {
        this.dependencies.add(dependency);
        dependency.setSubcontent(this);
        return this;
    }

    public Subcontent removeDependency(Dependency dependency) {
        this.dependencies.remove(dependency);
        dependency.setSubcontent(null);
        return this;
    }

    public Set<PendingStudentSubcontent> getPendingStudentSubcontents() {
        return this.pendingStudentSubcontents;
    }

    public void setPendingStudentSubcontents(Set<PendingStudentSubcontent> pendingStudentSubcontents) {
        if (this.pendingStudentSubcontents != null) {
            this.pendingStudentSubcontents.forEach(i -> i.setSubcontent(null));
        }
        if (pendingStudentSubcontents != null) {
            pendingStudentSubcontents.forEach(i -> i.setSubcontent(this));
        }
        this.pendingStudentSubcontents = pendingStudentSubcontents;
    }

    public Subcontent pendingStudentSubcontents(Set<PendingStudentSubcontent> pendingStudentSubcontents) {
        this.setPendingStudentSubcontents(pendingStudentSubcontents);
        return this;
    }

    public Subcontent addPendingStudentSubcontent(PendingStudentSubcontent pendingStudentSubcontent) {
        this.pendingStudentSubcontents.add(pendingStudentSubcontent);
        pendingStudentSubcontent.setSubcontent(this);
        return this;
    }

    public Subcontent removePendingStudentSubcontent(PendingStudentSubcontent pendingStudentSubcontent) {
        this.pendingStudentSubcontents.remove(pendingStudentSubcontent);
        pendingStudentSubcontent.setSubcontent(null);
        return this;
    }

    public Content getContent() {
        return this.content;
    }

    public void setContent(Content content) {
        this.content = content;
    }

    public Subcontent content(Content content) {
        this.setContent(content);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Subcontent)) {
            return false;
        }
        return getId() != null && getId().equals(((Subcontent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Subcontent{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
