package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Subcontent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SubcontentDTO implements Serializable {

    private String id;

    private String name;

    private String description;

    private ContentDTO content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ContentDTO getContent() {
        return content;
    }

    public void setContent(ContentDTO content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SubcontentDTO)) {
            return false;
        }

        SubcontentDTO subcontentDTO = (SubcontentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, subcontentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SubcontentDTO{" +
            "id='" + getId() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", content=" + getContent() +
            "}";
    }
}
