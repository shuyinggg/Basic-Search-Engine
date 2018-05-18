package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
import search.models.Webpage;

import java.net.URI;

/**
 * This class is responsible for computing the 'page rank' of all available webpages.
 * If a webpage has many different links to it, it should have a higher page rank.
 * See the spec for more details.
 */
public class PageRankAnalyzer {
    private IDictionary<URI, Double> pageRanks;

    /**
     * Computes a graph representing the internet and computes the page rank of all
     * available webpages.
     *
     * @param webpages  A set of all webpages we have parsed.
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    public PageRankAnalyzer(ISet<Webpage> webpages, double decay, double epsilon, int limit) {
        // Implementation note: We have commented these method calls out so your
        // search engine doesn't immediately crash when you try running it for the
        // first time.
        //
        // You should uncomment these lines when you're ready to begin working
        // on this class.

        // Step 1: Make a graph representing the 'internet'
        IDictionary<URI, ISet<URI>> graph = this.makeGraph(webpages);

        // Step 2: Use this graph to compute the page rank for each webpage
        this.pageRanks = this.makePageRanks(graph, decay, limit, epsilon);

        // Note: we don't store the graph as a field: once we've computed the
        // page ranks, we no longer need it!
    }

    /**
     * This method converts a set of webpages into an unweighted, directed graph,
     * in adjacency list form.
     *
     * You may assume that each webpage can be uniquely identified by its URI.
     *
     * Note that a webpage may contain links to other webpages that are *not*
     * included within set of webpages you were given. You should omit these
     * links from your graph: we want the final graph we build to be
     * entirely "self-contained".
     */
    private IDictionary<URI, ISet<URI>> makeGraph(ISet<Webpage> webpages) {
        //throw new NotYetImplementedException();
        IDictionary<URI, ISet<URI>> internet = new ChainedHashDictionary<>();
        for (Webpage page : webpages) {
            IList<URI> links = page.getLinks();
            URI url = page.getUri();
            ISet<URI> edges = new ChainedHashSet<>();
            for (URI link : links) {
                if (this.getAllUris(webpages).contains(link) && 
                        !link.equals(url) && !edges.contains(link) ) {
                    edges.add(link);  
                    }
                        internet.put(url, edges);   
            }
        }
        return internet;
    }

    /**
     * Computes the page ranks for all webpages in the graph.
     *
     * Precondition: assumes 'this.graphs' has previously been initialized.
     *
     * @param decay     Represents the "decay" factor when computing page rank (see spec).
     * @param epsilon   When the difference in page ranks is less then or equal to this number,
     *                  stop iterating.
     * @param limit     The maximum number of iterations we spend computing page rank. This value
     *                  is meant as a safety valve to prevent us from infinite looping in case our
     *                  page rank never converges.
     */
    private IDictionary<URI, Double> makePageRanks(IDictionary<URI, ISet<URI>> graph,
                                                   double decay,
                                                   int limit,
                                                   double epsilon) {
        // Step 1: The initialize step should go here
        IDictionary<URI, Double> toUpdateRanks = new ChainedHashDictionary<>();
        IDictionary<URI, Double> finalPageRanks = new ChainedHashDictionary<>();
        IDictionary<URI, Double> oldPageRanks = new ChainedHashDictionary<>();
        for (KVPair<URI, ISet<URI>> pair : graph) {
            URI url = pair.getKey();
            finalPageRanks.put(url, 1.0 / graph.size());
        }
        //Traverse graph each loop and update the values
        // # of web pages which have no outgoing links

        IDictionary<URI, ISet<URI>> incomingLinks = new ChainedHashDictionary<>();
        for (KVPair<URI, ISet<URI>> url1 : graph) {
            ISet<URI> linksTo = new ChainedHashSet<>();
            for (KVPair<URI, ISet<URI>> url2 : graph) {
                ISet<URI> links = url2.getValue();
                if (links != null && links.contains(url1.getKey())) {
                    linksTo.add(url2.getKey());
                }
            }
         incomingLinks.put(url1.getKey(), linksTo);
        }
        // Step 2: The update step should go here
        for (int i = 0; i < 1; i++) {
            for (KVPair<URI, Double> url : finalPageRanks) {
                oldPageRanks.put(url.getKey(), url.getValue());
            }
            Double add = 0.0;
            for (KVPair<URI, ISet<URI>> url : graph) {
                double value = (1 - decay) / graph.size();
                System.out.println(incomingLinks.get(url.getKey()).size() + "Incoming");
                for (URI link : incomingLinks.get(url.getKey())) {
                    int uniqueLinks = graph.get(link).size();
                    System.out.println(uniqueLinks + "Links");
                    if (uniqueLinks != 0) {
                        value = value + finalPageRanks.get(link) * decay / uniqueLinks;
                    }
                 }
                 toUpdateRanks.put(url.getKey(), value);
                 if (graph.get(url.getKey()) == null) {
                      add = add + decay * finalPageRanks.get(url.getKey()) / graph.size();
                 }
                    
            }
             for (KVPair<URI, Double> url : toUpdateRanks) {
               finalPageRanks.put(url.getKey(), url.getValue() + add);
             }
             // Step 3: the convergence step should go here.
             // Return early if we've converged.
             int check = 0;
             for (KVPair<URI, Double> url : finalPageRanks) {
                 if (Math.abs(url.getValue() - oldPageRanks.get(url.getKey())) <= epsilon) {
                     check++;
                 }    
             }
             if (check == finalPageRanks.size()) {
                 break;
             }
             
         }
      return finalPageRanks;  
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        return this.pageRanks.get(pageUri);
    }
    
    private ISet<URI> getAllUris(ISet<Webpage> webpages) {
        ISet<URI> allUrls = new ChainedHashSet<>();
        for (Webpage page : webpages) {
            allUrls.add(page.getUri());
        }
        return allUrls;
    }
        
}
