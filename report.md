# Report from the IIIF Harvesting Hack

## Context

- Europeana wants to explore other ways to aggregate data
- IIIF is a more and more popular protocol for serving images
- IIIF 'presentation' protocol has hooks for metadata ('seeAlso' and 'metadata' fields in IIIF 'manifests')

## What we've done

- Identify [lists of collections served over IIIF] (pointers.md)

- Look at the parts of the specs that could be relevant for us
  - i.e. harvesting IIIF manifests and get metadata

- Harvest the manifests for a sample of each collection
  - 13 collections
  - 80 manifests
  - covering more than 10K images

- Create a table with for each manifest, the rights and the seeAlso

- Analyze the manifests and what they link to

## Community contribution

- raise awareness to the problem of discovering/harvesting IIIF

- fed back our work live to the IIIF Slack and the IIIF Newspapers Interest Group

- we will circulate our github resources (and findings)

- we raised a technical issue about rights on one IIIF spec: https://github.com/IIIF/iiif.io/issues/868 

## Conclusions

- Harvesting seems possible, but to make it easier to harvest all IIIF endpoints, some requirements should be supported at the provider side
  - where to start harvesting and how to continue...
  - IIIF top-level Collection (specific to Europeana?), or another standard (repository, site maps). Yet to be determined!! 

- IIIF is still an evolving technology
  - Legacy (old IIIF versions) and immature IIIF implementations exist

- In general the metadata doesn't meet the Europeana standard
  - one or two collections come close (incl. Bodleian and NL Wales)
  - metadata quality issues
    - lack of metadata (few fields, empty values)
    - no controlled values
    - no controlled field (e.g. metadata element 'erschienen' in German)
  - rights issues: not there or not controlled
  - IIIF has the promise of finer-grained structures
    - for example a book and all its pages as a nice sequence. Or a big map with parts.
    - but this is not always implemented: pages not sequenced, part of the map not connected together
    - names of rather common concepts in sequences are not controlled ('front' 'back', 'middle', etc.). Not better than other standards 

Take-home message: 
- Harvesting metadata in IIIF is not possible yet
- We should define interoperability requirements for IIIF endpoints (Europeana IIIF profile) 
or tell providers to point to EDM files from their IIIF data.

