package se.vgr.incidentreport.pivotaltracker;


public interface PivotalTrackerService {

    /**
     * @param ir
     * @return the url of the story in PT
     */
    String createuserStory(PTStory ptstory);
    void addAttachmentToStory(String projectId, PTStory story);
    // Properties getPivotalTrackerMappings();

}
