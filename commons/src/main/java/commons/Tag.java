package commons;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Objects;

@Entity
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;
    private String tagName;
    private String colorCode;

    public Tag(){}
    public Tag(String tagName, String colorCode){
        this.tagName = tagName;
        this.colorCode = colorCode;
    }

    public String getId() {
        return id;
    }
    public String getTagName() {
        return tagName;
    }
    public String getColorCode() {
        return colorCode;
    }
    public void setTagName(String tagName) {
        this.tagName = tagName;
    }
    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id='" + id + '\'' +
                ", tagName='" + tagName + '\'' +
                ", colorCode='" + colorCode + '\'' +
                '}';
    }
}
