
# Analysis of Manifest metadata 


- In general the metadata doesn't meet the Europeana data quality standard as defined by EDM (for instance mandatory elements)
  - one or two collections such as the Bodleian Library or the National Library of Wales, have manifest data that are very close to the EDM data already in Europeana. 
For instance http://iiif.bodleian.ox.ac.uk/iiif/manifest/efbac9aa-19b4-43cd-adb0-d49e5815a7c6.json
In the case of the Bodleian Library, the metadata missing is the Europeana specific elements such as edm:rights that are currently added by The European Library as part of the aggregation process. 

  - The data provided or the manifests presents lot of data quality issues 
    - lack of metadata (few fields, empty values)
    - no controlled values
    - no controlled field (e.g. metadata element 'erschienen' in German)
  - rights issues: not there or not controlled
  - IIIF has the promise of finer-grained structures
    - for example a book and all its pages as a nice sequence. Or a big map with parts.
    - but this is not always implemented: pages not sequenced, part of the map not connected together
    - names of rather common concepts in sequences are not controlled ('front' 'back', 'middle', etc.). Not better than other standards 
