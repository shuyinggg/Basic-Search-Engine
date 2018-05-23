package search.analyzers;

import datastructures.concrete.ChainedHashSet;
import datastructures.concrete.KVPair;
import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IList;
import datastructures.interfaces.ISet;
//import misc.exceptions.NotYetImplementedException;
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
        IDictionary<URI, ISet<URI>> result = new ChainedHashDictionary<>();
        
        // get all the valid URIs to the space for later use
        ISet<URI> space = new ChainedHashSet<>(); 
        for (Webpage page : webpages) {
            space.add(page.getUri());
        }
        
        for (Webpage page : webpages) {
            IList<URI> links = page.getLinks();
            URI selflink = page.getUri();
            ISet<URI> pointsTo = new ChainedHashSet<>();
            
            for (URI link : links) { 
                if (space.contains(link) && !link.equals(selflink) && !pointsTo.contains(link)) {
                  pointsTo.add(link);
                }
            }
            result.put(selflink, pointsTo);
        } 
        return result;
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
        IDictionary<URI, Double> result = new ChainedHashDictionary<>();
        
        // equal weight rank
        int totalPages = graph.size();
        IDictionary<URI, Double> rank = new ChainedHashDictionary<>();
        for (KVPair<URI, ISet<URI>> page : graph) {
            rank.put(page.getKey(), 1.0 / totalPages);   
        }
        
        // get all the valid URIs to the space for later use
        ISet<URI> space = new ChainedHashSet<>();
        for (KVPair<URI, ISet<URI>> pair : graph) { 
            space.add(pair.getKey());
        }
        
        for (int i = 0; i < limit; i++) {
            // Step 2: The update step should go here
            
            // 2.1
            if (i != 0) {
                for (KVPair<URI, ISet<URI>> page : graph) {
                    rank.put(page.getKey(), result.get(page.getKey()));
                }
            }
            
            for (KVPair<URI, ISet<URI>> page : graph) {
                result.put(page.getKey(), 0.0);
            }
            
            // 2.2
            for (KVPair<URI, ISet<URI>> page : graph) {
                URI selflink = page.getKey();
                ISet<URI> links = page.getValue();
                int numLinks = links.size();
                
                if (numLinks == 0) {
                    // increase rank for every other pages
                    for (URI uri : space) {
                        result.put(uri, result.get(uri) + (decay * rank.get(selflink) / totalPages));
                    }
                } else {
                    for (URI link : links) {
                        result.put(link, result.get(link) + (decay * rank.get(selflink) / numLinks));
                    }
                }
            }
            
            // 2.3
            for (KVPair<URI, ISet<URI>> page : graph) {
                URI uri = page.getKey();
                result.put(uri, result.get(uri) + ((1 - decay) / totalPages));
            }
            
            // Step 3: the convergence step should go here.
            // Return early if we've converged.
            
            //throw new NotYetImplementedException();
            Boolean converged = true;
            for (KVPair<URI, ISet<URI>> page : graph) {
                URI urii = page.getKey();
                
                // set to false if there is one case that >= epsilon
                if (Math.abs(result.get(urii) - rank.get(urii)) >= epsilon) {
                    converged = false;
                }
            }
            if (converged) {
                return result;
            }
        }
        //throw new NotYetImplementedException();
        return result;
    }

    /**
     * Returns the page rank of the given URI.
     *
     * Precondition: the given uri must have been one of the uris within the list of
     *               webpages given to the constructor.
     */
    public double computePageRank(URI pageUri) {
        // Implementation note: this method should be very simple: just one line!
        return this.pageRanks.get(pageUri);
    }
}
