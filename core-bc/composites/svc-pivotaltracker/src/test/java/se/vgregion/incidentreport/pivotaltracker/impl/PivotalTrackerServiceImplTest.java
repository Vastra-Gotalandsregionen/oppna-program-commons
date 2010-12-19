package se.vgregion.incidentreport.pivotaltracker.impl;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.HTTPBasicAuthFilter;
import com.sun.jersey.api.client.filter.LoggingFilter;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataMultiPart;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.InputStreamBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.junit.Ignore;
import org.junit.Test;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * This action do that and that, if it has something special it is.
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public class PivotalTrackerServiceImplTest {

    /**
     * curl -H "X-TrackerToken: 322c5f338282f3ee44804b4fcee3d07a" -X POST -F Filedata=@/Users/david/Desktop/meow_cat_art.jpg http://www.pivotaltracker.com/services/v3/projects/35420/stories/7556517/attachments
     * <?xml version="1.0" encoding="UTF-8"?>
     * <attachment>
     * <id type="integer">897149</id>
     * <status>Pending</status>
     * </attachment>
     */


    @Test
    public void basicJerseyAuth() {
        String url = "https://www.pivotaltracker.com";
        String user = "TyckTill";
        String password = "tycktill3333";

        WebResource webResource = Client.create().resource(url);
        webResource.addFilter(new HTTPBasicAuthFilter(user, password));
        webResource.addFilter(new LoggingFilter());

        String text = webResource
                .path("/services/v3/tokens/active")
                .accept("*/*")
                .get(String.class);

        System.out.println(text);
    }

    @Test
    @Ignore
    public void jerseyClient() {
        File f = new File("/Users/david/Desktop/meow_cat_art.jpg");
        String url = "http://www.pivotaltracker.com/services/v3/projects/35420/stories/7556517/attachments";
        String formName = "Filedata";


        FormDataMultiPart form = new FormDataMultiPart();

        form.setContentDisposition(FormDataContentDisposition.name(formName).fileName("apa.jpg").size(f.length()).build());
        form.field(formName, f, MediaType.APPLICATION_OCTET_STREAM_TYPE);

        MultivaluedMap<String, String> headers = form.getHeaders();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        WebResource webResource = Client.create().resource(url);
        webResource.addFilter(new LoggingFilter());

        System.out.println(form.getContentDisposition().getFileName());

        webResource.type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .header("X-TrackerToken", "322c5f338282f3ee44804b4fcee3d07a")
                .header("Connection", null)
                .header("Expect", "100-continue")
                .accept("*/*")
                .post(form);

        headers = webResource.getRequestBuilder().head().getHeaders();
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

    }

    @Test
    public void basicCxfGet() {
        String url = "http://www.google.se";

        WebClient client = WebClient.create(url);
        Response response = client.accept("*/*").get();
        System.out.println(response.getStatus());
    }

    @Test
    public void basicCxfAuth() {
        String url = "https://www.pivotaltracker.com";
        String user = "TyckTill";
        String password = "tycktill3333";

        WebClient client = WebClient.create(url, user, password, null);

        String response = client
                .path("/services/v3/tokens/active")
                .accept("*/*")
                .get(String.class);

        System.out.println(response);
    }

    @Test
    @Ignore
    public void basicCxfFileUpload() throws IOException {
        File f = new File("/Users/david/Desktop/meow_cat_art.jpg");
        String url = "http://www.pivotaltracker.com";
        String path = "/services/v3/projects/35420/stories/7587203/attachments";
        String formName = "Filedata";

        WebClient client = WebClient.create(url + path);

        InputStream is = new FileInputStream(f);
        ContentDisposition cd = new ContentDisposition("form-data;name=Filedata;filename=apa.jpg;size=" + f.length());
        Attachment att = new Attachment("root", is, cd);
//        client.post(new MultipartBody(att));

// or just post the attachment if it's a single part request only
//        Response response = client.type("multipart/form-data")
//                .path(path)
//                .post(new MultipartBody(att));

        is.close();

// or just use a file
        Response response = client.type(MediaType.MULTIPART_FORM_DATA_TYPE)
                .header("X-TrackerToken", "322c5f338282f3ee44804b4fcee3d07a")
                .header("Content-Length", "26024")
                .header("Expect", "100-continue")
                .accept("*/*")
                .post(f);

        System.out.println(response.getStatus());
    }

    @Test
    @Ignore
    public void basicCurlUpload() {
        try {
            String project = "35420";
            String story = "7587203";
            String token = "322c5f338282f3ee44804b4fcee3d07a";
            String filePath = "/Users/david/Desktop/meow_cat_art.jpg";
            String curl = "curl -H X-TrackerToken:" + token + " -X POST -F Filedata=@" + filePath + " http://www.pivotaltracker.com/services/v3/projects/" + project + "/stories/" + story + "/attachments";
            String cur = "curl --help";

            System.out.println(curl);
            Process p = Runtime.getRuntime().exec(curl);

            String line;
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();

            input = new BufferedReader(new InputStreamReader(p.getErrorStream()));
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Ignore
    public void basicHttpClientUpload() {
        DefaultHttpClient client = new DefaultHttpClient();

        File f = new File("/Users/david/Desktop/meow_cat_art.jpg");
        String url = "http://www.pivotaltracker.com";
        String path = "/services/v3/projects/35420/stories/7587203/attachments";
        String token = "322c5f338282f3ee44804b4fcee3d07a";

        HttpPost httppost = new HttpPost(url + path);
        httppost.addHeader("X-TrackerToken", token);
//        httppost.removeHeaders("Connection");

        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile = new FileBody(f, "image/jpeg");
        mpEntity.addPart("Filedata", cbFile);
        httppost.setEntity(mpEntity);

        try {
            HttpResponse response = client.execute(httppost);

            String line;
            BufferedReader input = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            client.getConnectionManager().shutdown();
        }
    }

    @Test
    @Ignore
    public void basicHttpClientInputStreamUpload() throws IOException {
        DefaultHttpClient client = new DefaultHttpClient();

        File f = new File("/Users/david/Desktop/meow_cat_art.jpg");
        String url = "http://www.pivotaltracker.com";
        String path = "/services/v3/projects/35420/stories/7587203/attachments";
        String token = "322c5f338282f3ee44804b4fcee3d07a";

        HttpPost httppost = new HttpPost(url + path);
        httppost.addHeader("X-TrackerToken", token);
        httppost.removeHeaders("Connection");


        InputStream is = null;
        try {
            MultipartEntity mpEntity = new MultipartEntity();

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(f));

            SizedInputStreamBody cbFile = new SizedInputStreamBody(bis, f.getName(), f.length());
            System.out.println(cbFile.getTransferEncoding());

            mpEntity.addPart("Filedata", cbFile);
            httppost.setEntity(mpEntity);

            HttpResponse response = client.execute(httppost);

            String line;
            BufferedReader input = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            while ((line = input.readLine()) != null) {
                System.out.println(line);
            }
            input.close();

        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } finally {
            if (is != null) is.close();
            client.getConnectionManager().shutdown();
        }
    }

    class SizedInputStreamBody extends InputStreamBody {
        private long size = -1;

        public SizedInputStreamBody(InputStream in, String mimeType, String filename, long size) {
            super(in, mimeType, filename);
            this.size = size;
        }

        public SizedInputStreamBody(InputStream in, String filename, long size) {
            super(in, filename);
            this.size = size;
        }

        @Override
        public long getContentLength() {
            return size;
        }

    }
}

