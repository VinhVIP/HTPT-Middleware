package htpt.connection;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


public class Connector {
    public static final String TOKEN = "6f68f34e23b7963df8d6627b609c573891720eed";
    /**
     * Handler trả về kết quả thực thi
     */
    private ResponseHandler<Object[]> responseHandler = new ResponseHandler<Object[]>() {
        @Override
        public Object[] handleResponse(HttpResponse respone) throws ClientProtocolException, IOException {
            int status = respone.getStatusLine().getStatusCode();
            HttpEntity entity = respone.getEntity();
            Object[] obj = new Object[2];
            obj[0] = status;
            obj[1] = entity != null ? EntityUtils.toString(entity) : null;
            return obj;
        }
    };

    public Connector() {
    }

    /**
     * Lấy sinh viên theo mã sinh viên
     *
     * @param url
     * @return object[0]: status code, object[1]: message
     */
    public Object[] getStudent(String url) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpGet httpGet = new HttpGet(url);
            System.out.println("executing :" + httpGet.getRequestLine());

            Object[] response = httpClient.execute(httpGet, responseHandler);
            return response;

        } catch (IOException e) {
        }
        return null;
    }

    /**
     * Lấy danh sách tất cả sinh viên
     *
     * @param url
     * @return
     */
    public Object[] getListStudents(String url) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();

            HttpGet httpGet = new HttpGet(url);
            System.out.println("executing :" + httpGet.getRequestLine());

            Object[] response = httpClient.execute(httpGet, responseHandler);
            return response;

        } catch (IOException e) {
        }
        return null;
    }

    /**
     * Thêm sinh viên
     *
     * @param url
     * @param studentJson
     * @return
     */
    public Object[] postStudent(String url, String studentJson) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader(HttpHeaders.ACCEPT, "application/json");
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Beaer " + TOKEN);

            StringEntity stringEntity = new StringEntity(studentJson, "UTF-8");
            httpPost.setEntity(stringEntity);

            Object[] response = httpClient.execute(httpPost, responseHandler);
            return response;
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * Cập nhật sinh viên
     *
     * @param url
     * @param studentJson
     * @return
     */
    public Object[] putStudent(String url, String studentJson) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPut httpPut = new HttpPut(url);
            httpPut.setHeader(HttpHeaders.ACCEPT, "application/json");
            httpPut.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            httpPut.setHeader(HttpHeaders.AUTHORIZATION, "Beaer " + TOKEN);

            StringEntity stringEntity = new StringEntity(studentJson, "UTF-8");
            httpPut.setEntity(stringEntity);

            Object[] response = httpClient.execute(httpPut, responseHandler);
            return response;
        } catch (IOException e) {
        }
        return null;
    }

    /**
     * Xóa sinh viên theo mã sinh viên
     *
     * @param url
     * @return
     */
    public Object[] deleteStudent(String url) {
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpDelete httpDelete = new HttpDelete(url);
            httpDelete.setHeader(HttpHeaders.AUTHORIZATION, "Beaer " + TOKEN);

            Object[] response = httpClient.execute(httpDelete, responseHandler);
            return response;
        } catch (IOException e) {
        }
        return null;
    }
}
