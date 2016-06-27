package eu.europeana.iiif_harvest;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Form;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Selector;
import org.apache.jena.rdf.model.SimpleSelector;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;

/**
 * The starting point of the IIIF harvesting process.
 * (note: this is experimental source code)
 * 
 * @author Nuno
 *
 */
public class MainHarvestingScript {
	
	public static void main(String[] args) throws Exception {
		HttpsUtil.initIgnoreSslCertificate();
		new MainHarvestingScript().run();
	}
	
	int cnt = 0;
	IiifMetadataSampling sample=new IiifMetadataSampling();
	String currentIiifEndpointAllCollectionUri;
	String currentIiifEndpointName;
	File exportFolder =  new File("C:\\Users\\Nuno\\git\\IIIF-Manifest-Metadata-Harvesting\\IIIF-Manifest-Metadata-Harvesting");
	
	CloseableHttpClient httpClient;

	public void run() throws Exception {
		
	    httpClient = 
	    	      HttpClients.custom()
	    	                 .setSSLHostnameVerifier(new NoopHostnameVerifier())
	    	                 .build();
	    
	    
		
		for (int i=0; i<Config.IIIF_TOP_COLLECTIONS.length ; i=i+2) {
			currentIiifEndpointName = Config.IIIF_TOP_COLLECTIONS[i];
			currentIiifEndpointAllCollectionUri=Config.IIIF_TOP_COLLECTIONS[i+1];
			
			try {
				File jsonFile = new File(exportFolder, URLEncoder.encode(currentIiifEndpointAllCollectionUri, "UTF8")+".json");
				InputStream json;

				if (!jsonFile.exists()) {
//					CloseableHttpResponse execute = httpClient.execute(new HttpGet(currentIiifEndpointAllCollectionUri));
//					execute.get
					Content content = Request.Get(currentIiifEndpointAllCollectionUri).execute().returnContent();
					json = content.asStream();
					FileOutputStream outputStr = new FileOutputStream(jsonFile);
					byte[] byteArray = IOUtils.toByteArray(json);
					IOUtils.write(byteArray, outputStr);
					outputStr.flush();
					outputStr.close();
					json=new ByteArrayInputStream(byteArray);
				} else {
					json = new FileInputStream(jsonFile);
//					continue;
				}

				System.out.println("Collection Jsonld read");

//				sample = new IiifMetadataSampling();

				Model model = ModelFactory.createDefaultModel();
				RDFDataMgr.read(model, json, Lang.JSONLD);
				// System.out.println(model.toString());
				ResIterator manifests = model.listResourcesWithProperty(
						model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
						model.createResource("http://iiif.io/api/presentation/2#Manifest"));

				cnt = 0;
				harvestManifests(manifests);

				if(cnt<Config.SAMPLE_SIZE_PER_ENDPOINT) {
					ResIterator collections = model.listResourcesWithProperty(
							model.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
							model.createResource("http://iiif.io/api/presentation/2#Collection"));
				
					while (collections.hasNext() && cnt<Config.SAMPLE_SIZE_PER_ENDPOINT) {
						Resource coll = collections.next();
						Model subColl = getManifest(coll.getURI());

						manifests = subColl.listResourcesWithProperty(
								subColl.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
								subColl.createResource("http://iiif.io/api/presentation/2#Manifest"));
						
						harvestManifests(manifests);
					}
					
				}
				
				
//				String nameEncoded = URLEncoder.encode(currentIiifEndpointName, "UTF-8");
//				sample.export(new File(jsonFile.getParentFile(), nameEncoded+"/"+nameEncoded + ".xls"));
//				System.out.println("sample exported");

				// StmtIterator stms = model.listStatements();
				// while(stms.hasNext()){
				// Statement s= stms.next();
				// System.out.println(s);
				// }

				
			} catch (Exception e) {
				System.err.println("Error on "+ currentIiifEndpointAllCollectionUri);
				e.printStackTrace();
			}
		}
		sample.export(new File(exportFolder, "IIIF-Harvesting.xls"));
		sample.exportImages(new File(exportFolder, "IIIF-Harvesting-content.html"));
		sample.exportImagesMosaic(new File(exportFolder, "IIIF-Harvesting-Picture-Mosaic.html"));
		System.out.println("sample exported");
		

		
		// Request.Post("http://targethost/login")
		// .bodyForm(Form.form().add("username", "vip").add("password",
		// "secret").build())
		// .execute().returnContent();
	}
	
	
	private void harvestManifests(ResIterator manifests) throws IOException, IOException {
//		File endPointFolder=new File(exportFolder, URLEncoder.encode(currentIiifEndpointName, "UTF8"));
		File endPointFolder=new File(exportFolder, currentIiifEndpointName);
		if(!endPointFolder.exists())
			endPointFolder.mkdirs();
		while (manifests.hasNext()) {
			Resource manif = manifests.next();
			System.out.println(manif.getURI());
			cnt++;

			File manifFile = new File(endPointFolder, URLEncoder.encode(manif.getURI(), "UTF8"));
			byte[] manifestBytes;
			if(manifFile.exists()) {
				FileInputStream input = new FileInputStream(manifFile);
				manifestBytes=IOUtils.toByteArray(input);
				input.close();
			}else {
				Content content = Request.Get(manif.getURI()).execute().returnContent();
				manifestBytes=content.asBytes();
				IOUtils.write(manifestBytes,
						new FileOutputStream(manifFile));
			}
			
			Model modelManifest = ModelFactory.createDefaultModel();
			ByteArrayInputStream bytesIs = new ByteArrayInputStream(manifestBytes);
			RDFDataMgr.read(modelManifest, bytesIs, Lang.JSONLD);
			bytesIs.close();
			// modelManifest.listStatements(manif,
			// modelManifest.createProperty("http://www.w3.org/2000/01/rdf-schema#seeAlso"));
			StmtIterator seeAlso = modelManifest.listStatements(new SimpleSelector(manif,
					modelManifest.createProperty("http://www.w3.org/2000/01/rdf-schema#seeAlso"),
					(String) null));

			List<String> seeAlsos = new ArrayList<>();
			while (seeAlso.hasNext()) {
				Statement s = seeAlso.next();
				seeAlsos.add(s.getObject().toString());
				// System.out.println("seeAlso:
				// "+s.getObject().toString());
			}
			
			//rights
			StmtIterator rightsStms = modelManifest.listStatements(new SimpleSelector(manif,
					modelManifest.createProperty("http://purl.org/dc/terms/rights"),
					(String) null));
			
			String dcRights=null;
			if (rightsStms.hasNext()) {
				Statement s = rightsStms.next();
				dcRights=s.getObject().toString();
			}
			sample.add(currentIiifEndpointName, manif.getURI(), seeAlsos, dcRights);

			
			
			
			
			
			
//			Statement seq = modelManifest.getRequiredProperty(manif, modelManifest.createProperty("sequence"));
//			StmtIterator images = modelManifest.listStatements(new SimpleSelector(manif,
//					modelManifest.createProperty("http://www.w3.org/2000/01/rdf-schema#seeAlso"),
//					(String) null));

			ResIterator images = modelManifest.listResourcesWithProperty(
					modelManifest.createProperty("http://www.w3.org/1999/02/22-rdf-syntax-ns#type"),
					modelManifest.createResource("http://purl.org/dc/dcmitype/Image"));
			int imgCnt=0;
			while (images.hasNext() && imgCnt<Config.MAX_IMAGES_PER_MANIFEST) {
				imgCnt++;
				Resource img = images.next();
				sample.addImage(img.getURI());
			}
//			
////			"@type": "dctypes:Image"
//			List<String> seeAlsos = new ArrayList<>();
//			while (seeAlso.hasNext()) {
//				Statement s = seeAlso.next();
//				seeAlsos.add(s.getObject().toString());
//				// System.out.println("seeAlso:
//				// "+s.getObject().toString());
//			}
//			sample.add(currentIiifEndpointName, manif.getURI(), seeAlsos);

			// System.out.println(seeAlso);

			// model.getRequiredProperty(manif, null)
//			 StmtIterator stms = modelManifest.listStatements();
//			 while(stms.hasNext()) {
//			 Statement s= stms.next();
//			 System.out.println(s);
//			 }

			// System.out.println(content.asString());
			if (cnt >= Config.SAMPLE_SIZE_PER_ENDPOINT)
				break;
		}
		System.out.println(cnt + " manifests");
		
	}


	private static Model getManifest(String iiifEndpointAllCollectionUri) throws IOException {
			Content content = Request.Get(iiifEndpointAllCollectionUri).execute().returnContent();
			InputStream json = content.asStream();
		Model model = ModelFactory.createDefaultModel();
		RDFDataMgr.read(model, json, Lang.JSONLD);
		return model;
	}
}
