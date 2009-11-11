/**
 * 
 */
package se.vgr.usdservice.domain;

/**
 * @author Anders Bergkvist
 * 
 */
public class Issue {
    private String description;
    private String summary;
    private Integer refNum;
    private String url;
    private String statusId;
    private Status status;

    private enum Status {
        OP("Open"), IMPL("Implementation in progress"), CL("Closed"), CNCL("Cancelled"), APP(
                "Approval in progress"), CLREQ("Close Requested"), HOLD("Hold"), RE("Resolved"), SUSPEND(
                "Suspended"), VRFY("Verification in progress"), UNKNOWNSTAT("Unknown status");

        private String statusDesc;

        Status(String internalStatus) {
            statusDesc = internalStatus;
        }

        public String getDescription() {
            return statusDesc;
        }
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStatusId() {
        return statusId;
    }

    public void setStatusId(String statusId) {
        this.statusId = statusId;
        // Set status enum as well
        try {
            status = Status.valueOf(statusId);
        }
        catch (IllegalArgumentException e) {
            status = Status.UNKNOWNSTAT;
        }
    }

    public String getStatusDescription() {
        if (status != null) {
            return status.getDescription();
        }
        else {
            return Status.UNKNOWNSTAT.getDescription();
        }
    }

    public Integer getRefNum() {
        return refNum;
    }

    public void setRefNum(Integer refNum) {
        this.refNum = refNum;
    }

    public String getURL() {
        return url;
    }

    public void setURL(String url) {
        this.url = url;
    }
}
