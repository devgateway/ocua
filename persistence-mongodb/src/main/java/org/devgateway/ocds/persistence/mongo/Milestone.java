package org.devgateway.ocds.persistence.mongo;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.annotation.Generated;
import java.util.*;


/**
 * Milestone OCDS entity http://standard.open-contracting.org/latest/en/schema/reference/#milestone
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "title",
        "description",
        "dueDate",
        "dateModified",
        "status",
        "documents"
})
public class Milestone {

    /**
     * A local identifier for this milestone, unique within this block. This field is used to keep track of
     * multiple revisions of a milestone through the compilation from release to record mechanism.
     * (Required)
     *
     */
    @JsonProperty("id")
    private String id;

    /**
     * Milestone title
     *
     */
    @JsonProperty("title")
    private String title;

    /**
     * A description of the milestone.
     *
     */
    @JsonProperty("description")
    private String description;

    /**
     * The date the milestone is due.
     *
     */
    @JsonProperty("dueDate")
    private Date dueDate;

    /**
     * The date the milestone was last reviewed or modified and the status was altered or confirmed to still be correct.
     *
     */
    @JsonProperty("dateModified")
    private Date dateModified;

    /**
     * The status that was realized on the date provided in dateModified, drawn from the
     * [milestoneStatus codelist]
     *  (http://ocds.open-contracting.org/standard/r/1__0__0/en/schema/codelists#milestone-status).
     *
     */
    @JsonProperty("status")
    private Milestone.Status status;

    /**
     * List of documents associated with this milestone.
     *
     */
    @JsonProperty("documents")
    @JsonDeserialize(as = java.util.LinkedHashSet.class)
    private Set<Document> documents = new LinkedHashSet<Document>();

    /**
     * A local identifier for this milestone, unique within this block. This field is used to keep track of
     * multiple revisions of a milestone through the compilation from release to record mechanism.
     * (Required)
     *
     * @return
     *     The id
     */
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    /**
     * A local identifier for this milestone, unique within this block. This field is used to keep track of
     * multiple revisions of a milestone through the compilation from release to record mechanism.
     * (Required)
     *
     * @param id
     *     The id
     */
    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Milestone title
     *
     * @return
     *     The title
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * Milestone title
     *
     * @param title
     *     The title
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * A description of the milestone.
     *
     * @return
     *     The description
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * A description of the milestone.
     *
     * @param description
     *     The description
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * The date the milestone is due.
     *
     * @return
     *     The dueDate
     */
    @JsonProperty("dueDate")
    public Date getDueDate() {
        return dueDate;
    }

    /**
     * The date the milestone is due.
     *
     * @param dueDate
     *     The dueDate
     */
    @JsonProperty("dueDate")
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    /**
     * The date the milestone was last reviewed or modified and the status was altered or confirmed to still be correct.
     *
     * @return
     *     The dateModified
     */
    @JsonProperty("dateModified")
    public Date getDateModified() {
        return dateModified;
    }

    /**
     * The date the milestone was last reviewed or modified and the status was altered or confirmed to still be correct.
     *
     * @param dateModified
     *     The dateModified
     */
    @JsonProperty("dateModified")
    public void setDateModified(Date dateModified) {
        this.dateModified = dateModified;
    }

    /**
     * The status that was realized on the date provided in dateModified, drawn from the
     * [milestoneStatus codelist]
     *  (http://ocds.open-contracting.org/standard/r/1__0__0/en/schema/codelists#milestone-status).
     *
     * @return
     *     The status
     */
    @JsonProperty("status")
    public Milestone.Status getStatus() {
        return status;
    }

    /**
     * The status that was realized on the date provided in dateModified, drawn from the
     * [milestoneStatus codelist]
     *  (http://ocds.open-contracting.org/standard/r/1__0__0/en/schema/codelists#milestone-status).
     *
     * @param status
     *     The status
     */
    @JsonProperty("status")
    public void setStatus(Milestone.Status status) {
        this.status = status;
    }

    /**
     * List of documents associated with this milestone.
     *
     * @return
     *     The documents
     */
    @JsonProperty("documents")
    public Set<Document> getDocuments() {
        return documents;
    }

    /**
     * List of documents associated with this milestone.
     *
     * @param documents
     *     The documents
     */
    @JsonProperty("documents")
    public void setDocuments(Set<Document> documents) {
        this.documents = documents;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().
                append(id).
                append(title).
                append(description).
                append(dueDate).
                append(dateModified).
                append(status).
                append(documents).
                toHashCode();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Milestone)) {
            return false;
        }
        Milestone rhs = ((Milestone) other);
        return new EqualsBuilder().
                append(id, rhs.id).
                append(title, rhs.title).
                append(description, rhs.description).
                append(dueDate, rhs.dueDate).
                append(dateModified, rhs.dateModified).
                append(status, rhs.status).
                append(documents, rhs.documents).
                isEquals();
    }

    public enum Status {
        MET("met"),

        NOT_MET("notMet"),

        PARTIALLY_MET("partiallyMet");

        private final String value;

        private final static Map<String, Milestone.Status> CONSTANTS = new HashMap<String, Milestone.Status>();

        static {
            for (Milestone.Status c: values()) {
                CONSTANTS.put(c.value, c);
            }
        }

        Status(String value) {
            this.value = value;
        }

        @JsonValue
        @Override
        public String toString() {
            return this.value;
        }

        @JsonCreator
        public static Milestone.Status fromValue(String value) {
            Milestone.Status constant = CONSTANTS.get(value);
            if (constant == null) {
                throw new IllegalArgumentException(value);
            } else {
                return constant;
            }
        }

    }

}
