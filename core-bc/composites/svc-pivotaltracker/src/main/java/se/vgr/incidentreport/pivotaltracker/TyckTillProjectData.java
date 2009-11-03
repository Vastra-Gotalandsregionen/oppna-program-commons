package se.vgr.incidentreport.pivotaltracker;

import java.util.ArrayList;
import java.util.List;

public class TyckTillProjectData {
    String id;
    String name;
    private List<String> members = new ArrayList<String>();

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

    @Override
    public String toString() {
        return "id=" + id + ", name=" + name + ", members=" + members;
    }

    public void addMember(String member) {
        members.add(member);
    }
}
