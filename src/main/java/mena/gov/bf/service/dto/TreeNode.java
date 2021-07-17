package mena.gov.bf.service.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TreeNode implements Serializable {
    private Long id;
    private String label;
    private String data;
    private String collapsedIcon;
    private String expandedIcon;
    private Long niveau;
    private Long pereId;
    private List<TreeNode> children = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCollapsedIcon() {
        return collapsedIcon;
    }

    public void setCollapsedIcon(String collapsedIcon) {
        this.collapsedIcon = collapsedIcon;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void setChildren(List<TreeNode> children) {
        this.children = children;
    }

    public String getExpandedIcon() {
        return expandedIcon;
    }

    public void setExpandedIcon(String expandedIcon) {
        this.expandedIcon = expandedIcon;
    }

    public Long getNiveau() {
        return niveau;
    }

    public void setNiveau(Long niveau) {
        this.niveau = niveau;
    }

    public Long getPereId() {
        return pereId;
    }

    public void setPereId(Long pereId) {
        this.pereId = pereId;
    }

    @Override
    public String toString() {
        return "TreeNode{" +
            "id=" + id +
            ", label='" + label + '\'' +
            ", data='" + data + '\'' +
            ", collapsedIcon='" + collapsedIcon + '\'' +
            ", expandedIcon='" + expandedIcon + '\'' +
            ", niveau=" + niveau +
            ", pereId=" + pereId +
            ", children=" + children +
            '}';
    }
}
