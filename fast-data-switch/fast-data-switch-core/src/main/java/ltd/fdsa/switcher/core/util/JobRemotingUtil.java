package ltd.fdsa.switcher.core.util;

import ltd.fdsa.web.view.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

public class JobRemotingUtil {
    private static final TrustManager[] trustAllCerts =
            new TrustManager[]{
                    new X509TrustManager() {
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }

                        public void checkClientTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                        }

                        public void checkServerTrusted(X509Certificate[] chain, String authType)
                                throws CertificateException {
                        }
                    }
            };
    public static String  RPC_ACCESS_TOKEN = "ACCESS-TOKEN";
    private static Logger logger = LoggerFactory.getLogger(JobRemotingUtil.class);

    // trust-https start
    private static void trustAllHosts(HttpsURLConnection connection) {
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            SSLSocketFactory newFactory = sc.getSocketFactory();

            connection.setSSLSocketFactory(newFactory);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        connection.setHostnameVerifier(
                new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
    }
    // trust-https end

    /**
     * post
     *
     * @param url
     * @param accessToken
     * @param requestObj
     * @return
     */
    public static Result<String> postBody(
            String url, String accessToken, Object requestObj, int timeout) {
        HttpURLConnection connection = null;
        BufferedReader bufferedReader = null;
        try {
            // connection
            URL realUrl = new URL(url);
            connection = (HttpURLConnection) realUrl.openConnection();

            // trust-https
            boolean useHttps = url.startsWith("https");
            if (useHttps) {
                HttpsURLConnection https = (HttpsURLConnection) connection;
                trustAllHosts(https);
            }

            // connection setting
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setReadTimeout(timeout * 1000);
            connection.setConnectTimeout(3 * 1000);
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            connection.setRequestProperty("Accept-Charset", "application/json;charset=UTF-8");

            if (accessToken != null && accessToken.trim().length() > 0) {
                connection.setRequestProperty( RPC_ACCESS_TOKEN, accessToken);
            }

            // do connection
            connection.connect();

            // write requestBody
            String requestBody = JacksonUtil.writeValueAsString(requestObj);

            DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());
            dataOutputStream.write(requestBody.getBytes("UTF-8"));
            dataOutputStream.flush();
            dataOutputStream.close();

      /*byte[] requestBodyBytes = requestBody.getBytes("UTF-8");
      connection.setRequestProperty("Content-Length", String.valueOf(requestBodyBytes.length));
      OutputStream outwritestream = connection.getOutputStream();
      outwritestream.write(requestBodyBytes);
      outwritestream.flush();
      outwritestream.close();*/

            // valid StatusCode
            int statusCode = connection.getResponseCode();
            if (statusCode != 200) {
                return Result.fail(500,
                        "project.remoting fail, StatusCode(" + statusCode + ") invalid. for url : " + url);
            }

            // result
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                result.append(line);
            }
            String resultJson = result.toString();

            // parse Result
            try {
                Map<String, Object> resultMap = JacksonUtil.readValue(resultJson, Map.class);

                Result<String> result1 = Result.success();
                if (resultMap == null) {
                    result1.setCode(500);
                    result1.setMessage("AdminBizClient Remoting call fail.");
                } else {
                    result1.setCode(Integer.valueOf(String.valueOf(resultMap.get("code"))));
                    result1.setMessage(String.valueOf(resultMap.get("msg")));
                    result1.setData(String.valueOf(resultMap.get("content")));
                }
                return result1;
            } catch (Exception e) {
                logger.error(
                        "project.remoting (url=" + url + ") response content invalid(" + resultJson + ").", e);
                return Result.fail(500,
                        "project.remoting (url=" + url + ") response content invalid(" + resultJson + ").");
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.fail(500, "project.remoting error(" + e.getMessage() + "), for url : " + url);
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
                if (connection != null) {
                    connection.disconnect();
                }
            } catch (Exception e2) {
                logger.error(e2.getMessage(), e2);
            }
        }
    }
}
