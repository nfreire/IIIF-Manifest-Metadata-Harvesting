package eu.europeana.iiif_harvest;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

/**
 * Utility class to initialise the usage of SSL certificates (self signed by IIIF implementors)
 * 
 * @author Nuno
 *
 */
public class HttpsUtil {
	public static void initIgnoreSslCertificate() {
	        try {
	            // Create a trust manager that does not validate certificate chains
	            TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
	                    @Override
	                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                        return null;
	                    }
	                    @Override
	                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
	                    }
	                    @Override
	                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
	                    }
	                }
	            };
	     
	            // Install the all-trusting trust manager
	            SSLContext sc = SSLContext.getInstance("SSL");
//	            SSLContext sc = SSLContext.getInstance("TLS");
	            sc.init(null, trustAllCerts, new java.security.SecureRandom());
	            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	     
	            // Create all-trusting host name verifier
	            HostnameVerifier allHostsValid = new HostnameVerifier() {

					@Override
					public boolean verify(String hostname, SSLSession session) {
						return true;
					}
	            };
	            // Install the all-trusting host verifier
	            HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	            
	         // Now you can access an https URL without having the certificate in the truststore
//	            try {
//	                URL url = new URL("https://195.216.97.96/api/data-providers");
//	                Object content = url.getContent();
//	                System.out.println("Got https response"+IOUtils.readLines((InputStream)content));
//	            } catch (Exception e) {
//	                e.printStackTrace();
//	            }
	            
	            
//	                new HostnameVerifier()
//	                {
//	                    @Override
//	                    public boolean verify( String s, SSLSession sslSession )
//	                    {
//	                        return true;
//	                    }
//	                }, sslcontext
//	        );
//	        properties.put( HTTPSProperties.PROPERTY_HTTPS_PROPERTIES, httpsProperties );
	        } catch (Exception e) {
                e.printStackTrace();
            }

	}
}
