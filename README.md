#Web crowler for the Sinhala Corpus project.#

If you want to create a parser for a new document, implement the interface Parser in corpus.sinhala.crowler.parser package. Make sure that the constructor of the implemented class accepts a HTML of the page as a string and the page url as a string. Then change the addDocument method and create the parser object using your newly created class
