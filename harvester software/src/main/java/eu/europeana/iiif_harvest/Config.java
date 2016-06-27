package eu.europeana.iiif_harvest;

/**
 * Configuration file for the harvest, and the samples extracted 
 * 
 * @author Nuno
 *
 */
public class Config {
	public static final int SAMPLE_SIZE_PER_ENDPOINT = 10;

	public static final String[] IIIF_TOP_COLLECTIONS = new String[] {
			"Digital.Bodleian",
			"http://iiif.bodleian.ox.ac.uk/iiif/collection/All",	
			"e-codices - Virtual Manuscript Library of Switzerland",
			"http://www.e-codices.unifr.ch/metadata/iiif/collection.json",
			"From The Pages",
			"http://fromthepage.com/iiif/collections",
			"Nat. Lib. of Wales - Newspapers",
			"http://dams.llgc.org.uk/iiif/newspapers/3100020.json", 
			"Nat. Lib. of Wales - Manuscripts",
			"http://dams.llgc.org.uk/iiif/collection/saints.json",
			"Sentences Commentary Text Archive",
			"http://scta.info/iiif/collection/scta",
			"Stanford",
			"https://graph.global/static/data/universes/iiif/stanford.json",
			"Text Grid Lab",
			"http://textgridlab.org/1.0/iiif/manifests/collection.json",
//			"https://textgridlab.org/1.0/iiif/manifests/collection.json",
			"Villanova Library",
			"http://digital.library.villanova.edu/Collection/vudl:3/IIIF",
			"Wellcome Library - Artists",
//			"https://wellcomelibrary.org/service/collections/", 
			"http://wellcomelibrary.org/service/collections/genres/Artists%20with%20mental%20disabilities/", 
			"Wellcome Library - Caricatures",
			"http://wellcomelibrary.org/service/collections/genres/Caricatures/", 
			"World Digital Library",
			"https://www.wdl.org/en/item/11849/manifest",
			"Yale Digital Collections Center",
			"http://manifests.ydc2.yale.edu/manifest",
			"Bavarian State Library",
			"http://www.digitale-sammlungen.de/mirador/rep/bsb00088321/bsb00088321.json" 
			};

	public static final int MAX_IMAGES_PER_MANIFEST = 10;
}
