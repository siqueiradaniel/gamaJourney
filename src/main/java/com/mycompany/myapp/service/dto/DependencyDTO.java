package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Dependency} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DependencyDTO implements Serializable {

    private Long id;

    private String firstSubcontentId;

    private String secondSubcontentId;

    private SubcontentDTO subcontent;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstSubcontentId() {
        return firstSubcontentId;
    }

    public void setFirstSubcontentId(String firstSubcontentId) {
        this.firstSubcontentId = firstSubcontentId;
    }

    public String getSecondSubcontentId() {
        return secondSubcontentId;
    }

    public void setSecondSubcontentId(String secondSubcontentId) {
        this.secondSubcontentId = secondSubcontentId;
    }

    public SubcontentDTO getSubcontent() {
        return subcontent;
    }

    public void setSubcontent(SubcontentDTO subcontent) {
        this.subcontent = subcontent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DependencyDTO)) {
            return false;
        }

        DependencyDTO dependencyDTO = (DependencyDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, dependencyDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DependencyDTO{" +
            "id=" + getId() +
            ", firstSubcontentId='" + getFirstSubcontentId() + "'" +
            ", secondSubcontentId='" + getSecondSubcontentId() + "'" +
            ", subcontent=" + getSubcontent() +
            "}";
    }
}
