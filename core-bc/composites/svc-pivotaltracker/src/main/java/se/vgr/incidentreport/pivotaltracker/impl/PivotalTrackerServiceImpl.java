package se.vgr.incidentreport.pivotaltracker.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import se.vgr.incidentreport.pivotaltracker.PTStory;
import se.vgr.incidentreport.pivotaltracker.PivotalTrackerService;
import se.vgr.incidentreport.pivotaltracker.TyckTillProjectData;
import se.vgr.util.HTTPUtils;

/**
 * This class implements the api method calls to send and retrieve the data objects.
 * 
 * @author Jon Stevens
 */
public class PivotalTrackerServiceImpl implements PivotalTrackerService {

    private static final String TYCKTILL_PT_PWD = "tycktill3333";
    private static final String TYCK_TILL_PT_USER = "TyckTill";
    private static final String GET_USER_TOKEN = "https://www.pivotaltracker.com/services/tokens/active";
    private static final String GET_PROJECT = "http://www.pivotaltracker.com/services/v2/projects";

    /** */
    // private String token = null;

    /**
     * Get a user token. Also assigns the token to the inner member (token) of this class.
     * 
     * @param username
     *            the users username
     * @param password
     *            the users password
     * @return a populated TokenData object or null
     * @throws Exception
     *             when there is http problems
     */
    private String getUserToken(String username, String password) {
        DefaultHttpClient client = new DefaultHttpClient();
        String tokenFound = null;
        try {
            // Use basicAuth to make the request to the server
            HttpResponse response = HTTPUtils.basicAuthRequest(GET_USER_TOKEN, username, password, client);
            HttpEntity entity = response.getEntity();

            if ((response.getStatusLine().getStatusCode() != 200) || (entity.getContentLength() == 1)) {
                return null;
            }

            // Convert the xml response into an object
            String xml = convertStreamToString(entity.getContent());
            System.out.println(xml);
            String guid = getTagValue(xml, 0, "guid");
            System.out.println("guid=" + guid);

            tokenFound = guid;
        }
        catch (Exception e) {
            throw new RuntimeException("TODO: Handle this exception better", e);
        }
        finally {
            client.getConnectionManager().shutdown();
        }

        return tokenFound;
    }

    private String getTagValue(String xml, int index, String tagName) {

        int beginIndex = xml.indexOf("<" + tagName + ">") + tagName.length() + 2;
        int endIndex = xml.indexOf("</" + tagName + ">", beginIndex);
        String result = xml.substring(beginIndex, endIndex);

        return result;
    }

    /** */
    private TyckTillProjectData getSingleProject(String projectId, String token) throws Exception {
        if (token == null) {
            throw new RuntimeException("Token cannot be null. Please set it first.");
        }

        DefaultHttpClient client = new DefaultHttpClient();
        TyckTillProjectData result = null;
        try {
            HttpResponse response = HTTPUtils.makeRequest(GET_PROJECT + "/" + projectId, token, client);
            HttpEntity entity = response.getEntity();

            // Convert the xml response into an object
            result = getProjectData(entity.getContent()).get(0);
        }
        catch (RuntimeException e) {
            e.printStackTrace();
        }
        finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }

    private String getStoriesForProject(String projectId, String token) {
        if (token == null) {
            throw new RuntimeException("Token cannot be null. Please set it first.");
        }
        DefaultHttpClient client = new DefaultHttpClient();
        String result = null;
        try {
            HttpResponse response = HTTPUtils.makeRequest(GET_PROJECT + "/" + projectId + "/stories", token,
                    client);
            HttpEntity entity = response.getEntity();
            String xml = convertStreamToString(entity.getContent());
            System.out.println(xml);

            // Convert the xml response into an object
            // result = getProjectData((entity.getContent()));

        }
        catch (Exception e) {
            throw new RuntimeException("TODO: Handle this exception better", e);
        }
        finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }

    public String addStoryForProject(String projectId, PTStory story) {
        String token = getUserToken(TYCK_TILL_PT_USER, TYCKTILL_PT_PWD);
        if (token == null) {
            throw new RuntimeException("Token cannot be null. Please set it first.");
        }
        DefaultHttpClient client = new DefaultHttpClient();
        String result = null;
        String xml = "<story><story_type>" + story.getType() + "</story_type><name>" + story.getName() + "</name>"
                + "<description>" + story.getDescription() + "</description><requested_by>"
                + story.getRequestedBy() + "</requested_by></story>";

        try {
            HttpResponse response = HTTPUtils.makePostXML(GET_PROJECT + "/" + projectId + "/stories", token,
                    client, xml);
            HttpEntity entity = response.getEntity();
            String xmlout = convertStreamToString(entity.getContent());
            System.out.println(xmlout);
            String url = getTagValue(xmlout, 0, "url");
            result = url;

            // Convert the xml response into an object
            // result = getProjectData((entity.getContent()));

        }
        catch (Exception e) {
            throw new RuntimeException("TODO: Handle this exception better", e);
        }
        finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }

    /** */
    @SuppressWarnings("deprecation")
    private List<TyckTillProjectData> getAllProjects(String token) {
        if (token == null) {
            throw new RuntimeException("Token cannot be null. Please set it first.");
        }

        DefaultHttpClient client = new DefaultHttpClient();
        List result = null;
        try {
            HttpResponse response = HTTPUtils.makeRequest(GET_PROJECT, token, client);
            HttpEntity entity = response.getEntity();
            // entity.writeTo(System.out);

            // Convert the xml response into an object
            result = getProjectData((entity.getContent()));

        }
        catch (Exception e) {
            throw new RuntimeException("TODO: Handle this exception better", e);
        }
        finally {
            client.getConnectionManager().shutdown();
        }
        return result;
    }

    protected List<TyckTillProjectData> getProjectData(InputStream xml) throws Exception {
        List<TyckTillProjectData> result = new ArrayList<TyckTillProjectData>();
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
        docBuilderFactory.setValidating(false);
        docBuilderFactory.setNamespaceAware(false);
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document doc = docBuilder.parse(xml);
        xml.close();

        NodeList projectNodes = doc.getElementsByTagName("project");
        System.out.println("antal projekt=" + projectNodes.getLength());
        for (int s = 0; s < projectNodes.getLength(); s++) {
            Node projectNode = projectNodes.item(s);
            NodeList projectChildNodes = projectNode.getChildNodes();
            TyckTillProjectData data = new TyckTillProjectData();
            for (int i = 0; i < projectChildNodes.getLength(); i++) {
                Node projectAttributeNode = projectChildNodes.item(i);
                String nodeName = projectAttributeNode.getNodeName();
                String nodeValue = projectAttributeNode.getTextContent();
                short nodeType = projectAttributeNode.getNodeType();

                if (nodeType == 1) {
                    // System.out.println("node:" + nodeName + "=" + nodeValue);
                    if ("id".equals(nodeName)) {
                        data.setId(nodeValue);
                    }
                    else if ("name".equals(nodeName)) {
                        data.setName(nodeValue);
                    }
                    else if ("memberships".equals(nodeName)) {
                        NodeList mNodes = projectAttributeNode.getChildNodes();

                        for (int k = 0; k < mNodes.getLength(); k++) {
                            Node mNode = mNodes.item(k);// membership
                            NodeList mNodes2 = mNode.getChildNodes();
                            for (int m = 0; m < mNodes2.getLength(); m++) {
                                Node maNode = mNodes2.item(m);// membership
                                if (maNode.getNodeType() == 1) {
                                    if ("person".equals(maNode.getNodeName())) {
                                        data.addMember(maNode.getTextContent());
                                    }
                                    else {
                                        // System.out.println(" fgfgf=" + maNode.getNodeName());
                                    }
                                }
                            }

                        }

                    }
                }
            }
            result.add(data);

        }

        return result;
    }

    public String convertStreamToString(InputStream is) {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine() method. We iterate until the
         * BufferedReader return null which means there's no more data to read. Each line will appended to a
         * StringBuilder and returned as String.
         */
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(is, "utf-8"));
        }
        catch (UnsupportedEncodingException e1) {
            throw new RuntimeException("TODO: Handle this exception better", e1);
        }
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            try {
                is.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    /** */
    public static void main(String[] argv) {
        PivotalTrackerServiceImpl pt = new PivotalTrackerServiceImpl();
        try {
            System.out.println(pt.getUserToken("andrew.culbert@vgregion.se", "gr0Sv3nor"));

            PTStory story = new PTStory();
            story.setName("Meddelande från Tyck till : " + new Date());
            story.setType("bug");
            story.setRequestedBy("TyckTill");
            story.setDescription("Testar att skicka meddelanden via Tyck Till.");
            System.out.println(pt.addStoryForProject("35420", story));

            String token = pt.getUserToken(TYCK_TILL_PT_USER, TYCKTILL_PT_PWD);
            List<TyckTillProjectData> l = pt.getAllProjects(token);
            for (TyckTillProjectData d : l) {
                System.out.println(d);
            }
            System.out.println(pt.getStoriesForProject("35420", token));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String createuserStory(PTStory ptstory) {
        throw new UnsupportedOperationException("TODO: Implement this method");
    }
}
