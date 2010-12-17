package se.vgregion.incidentreport.pivotaltracker.impl;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.multipart.FormDataMultiPart;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import java.io.File;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class PivotalTrackerServiceImplTest {

    /**
     * curl -H "X-TrackerToken: 322c5f338282f3ee44804b4fcee3d07a" -X POST -F Filedata=@/Users/david/Desktop/meow_cat_art.jpg http://www.pivotaltracker.com/services/v3/projects/35420/stories/7556517/attachments
<?xml version="1.0" encoding="UTF-8"?>
<attachment>
  <id type="integer">897149</id>
  <status>Pending</status>
</attachment>

     */

    @Test
    @Ignore
    public void jerseyClient() {
        File f = new File("/Users/david/Desktop/meow_cat_art.jpg");
        String url = "http://www.pivotaltracker.com/services/v3/projects/35420/stories/7556517/attachments";
        String formName = "Filedata";


        FormDataMultiPart form = new FormDataMultiPart().field("", f, MediaType.MULTIPART_FORM_DATA_TYPE);

        WebResource webResource = Client.create().resource(url);
        webResource.addFilter(new LoggingFilter());

        webResource.type(MediaType.MULTIPART_FORM_DATA)
                .header("X-TrackerToken", "322c5f338282f3ee44804b4fcee3d07a")
                .header("Expect", "100-continue")
                .header("Content-Length", "26024")
                .header("Host", "www.pivotaltracker.com")
                .accept("*/*")
                .post(form);
    }

    @Test
    public void basicAuth() {
        String url = "https://www.pivotaltracker.com/services/v3/tokens/active";
        String user = "TyckTill";
        String password = "tycktill3333";

        WebResource webResource = Client.create().resource(url);
        webResource.addFilter(new HTTPBasicAuthFilter(user, password));
        webResource.addFilter(new LoggingFilter());

        String text = webResource
                .accept("*/*")
                .get(String.class);

        System.out.println(text);
    }

    @Test
    public void basicGet() {
        String url = "http://www.google.com";

        WebResource webResource = Client.create().resource(url);
        String text = webResource.accept("*/*").get(String.class);

        Assert.assertTrue(text.startsWith("<!doctype html><html><head>"));
        System.out.println(text);
    }
}

