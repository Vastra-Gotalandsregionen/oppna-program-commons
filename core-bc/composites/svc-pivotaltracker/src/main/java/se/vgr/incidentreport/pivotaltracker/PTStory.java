package se.vgr.incidentreport.pivotaltracker;

import java.io.File;
import java.util.List;

public class PTStory {

    private String projectId;
    private String type;
    private String id;
    private List<File> attachments;
	private String name;
    private String requestedBy;
    private String description;

    
    public String getStoryId() {
		return id;
	}

	public void setStoryId(String id) {
		this.id = id;
	}

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(String requestedBy) {
        this.requestedBy = requestedBy;
    }

    public void setDescription(String description) {
        this.description = description;
    }

	public void setAttachments(List<File> attachments) {
		this.attachments = attachments;
	}

	public List<File> getAttachments() {
		return attachments;
	}

}
