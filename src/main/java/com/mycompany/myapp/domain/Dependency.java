package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Dependency.
 */
@Entity
@Table(name = "dependency")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Dependency implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "first_subcontent_id")
    private String firstSubcontentId;

    @Column(name = "second_subcontent_id")
    private String secondSubcontentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "learningPaths", "dependencies", "pendingStudentSubcontents", "content" }, allowSetters = true)
    private Subcontent subcontent;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Dependency id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstSubcontentId() {
        return this.firstSubcontentId;
    }

    public Dependency firstSubcontentId(String firstSubcontentId) {
        this.setFirstSubcontentId(firstSubcontentId);
        return this;
    }

    public void setFirstSubcontentId(String firstSubcontentId) {
        this.firstSubcontentId = firstSubcontentId;
    }

    public String getSecondSubcontentId() {
        return this.secondSubcontentId;
    }

    public Dependency secondSubcontentId(String secondSubcontentId) {
        this.setSecondSubcontentId(secondSubcontentId);
        return this;
    }

    public void setSecondSubcontentId(String secondSubcontentId) {
        this.secondSubcontentId = secondSubcontentId;
    }

    public Subcontent getSubcontent() {
        return this.subcontent;
    }

    public void setSubcontent(Subcontent subcontent) {
        this.subcontent = subcontent;
    }

    public Dependency subcontent(Subcontent subcontent) {
        this.setSubcontent(subcontent);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dependency)) {
            return false;
        }
        return getId() != null && getId().equals(((Dependency) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Dependency{" +
            "id=" + getId() +
            ", firstSubcontentId='" + getFirstSubcontentId() + "'" +
            ", secondSubcontentId='" + getSecondSubcontentId() + "'" +
            "}";
    }
}
