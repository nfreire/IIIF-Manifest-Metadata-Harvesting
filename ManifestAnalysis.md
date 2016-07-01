
# Analysis of Manifest metadata and See Also 

##Analysis of the Manifests

Europeana has analysed a series of Manifest samples in order to investigate whether the Manifest metadata could be used as an alternative to data currently harvested via OAI-PMH. This analysis focused on a comparison between the metadata expected by the Europeana Data Model (EDM) and the metadata available in the IIIF manifest. 

The general feedback is that the metadata available in Manifests doesn't meet the Europeana data quality standard as defined by EDM (for instance mandatory elements)
  - one or two collections such as the Bodleian Library or the National Library of Wales, have manifests metadata that are very close to the EDM data already available in Europeana. 
For instance http://iiif.bodleian.ox.ac.uk/iiif/manifest/efbac9aa-19b4-43cd-adb0-d49e5815a7c6.json
In the case of the Bodleian Library, the metadata missing is the Europeana specific elements such as edm:rights that are currently added by The European Library as part of the aggregation process. It should be therefore fairly easy to map this metadata to the EDM. 

### The metadata provided for the manifests presents lot of data quality issues 

- lack of metadata: such as very few descriptive metadata, empty values, html tags contained in metadata values (this is allowed by IIIF but could be flagged as an issue by data consumers).
  For instance http://www.digitale-sammlungen.de/mirador/rep/bsb00088321/bsb00088321.json
- no controlled field: metadata labels are not always controlled and sometimes provided in the orginal language of the institution (see example above). This lack of controlled elements labels would prevent the re-use of the data. 
- Issue with rights information: Europeana pays a particular attention to the quality of rights information. In the samples analysed durimng this experiment we noticed that either the rights information was completely missing, either it was not controlled (using a variety of labels and values.
  For instance: http://dams.llgc.org.uk/iiif/newspaper/issue/3101078/manifest.json

### Heterogeneous implementations of IIIF

IIIF provide ways to represent finer-grained structures in terms of content representation. For instance IIIF would allow a coherant representation of a book and its digitised pages, while in Europeana each page might be represented as a single object with no relation to its whole. 
However the sequence feature is not always implemented like in http://dams.llgc.org.uk/iiif/newspaper/issue/3101078/manifest.json
We also noticed that names of rather common concepts in sequences and canvas are not controlled ('front' 'back', 'middle', etc.). This problem can be found in many other standards but still more controlled values would help understanding the data. 

##Analysis of the See Also 

For each of the samples analysed, we also looked at the values provided in the SeeAlso if available. 
The analysis shows that this property is used in many different ways across datasets: 
- Link to a IIIF viewer 
- Link to a source image if the IIIF service points to another IIIF server (e.g. The Sentences Commentary Text Archive points to images from the National Library of France)
- Link to a metadata file expressed in different syntax: XML,turtle...

###Conclusion

Harvesting metadata direclty from the IIIF Manifest could be an alternative to the current Europeana harvesting procedure. The main motivations come from the fact that harvesting the metadata and content could be done at the same time. However the quality issues found in the metadata would require Europeana to provide the same level of recommendations than it is currently the case in terms of metadata available in a Manifest. 
One proposed solution is too ask to Europeana Data Partners using IIIF to use the SeeAlso to point to a EDM representation of their data. Europeana would then be able to access at once to both the content and the metadata. 

