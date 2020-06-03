import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

/* CompliantNode refers to a node that follows the rules (not malicious)*/
public class CompliantNode implements Node {
    private double p_graph;
    private double p_malicious;
    private double p_txDistribution;
    private int numRounds;

    private boolean[] followees;

    private Set<Transaction> pendingTransactions;

    private boolean[] blackListed;


    public CompliantNode(double p_graph, double p_malicious, double p_txDistribution, int numRounds) {
        //Constructor/
        this.p_graph = p_graph;
        this.p_malicious = p_malicious;
        this.p_txDistribution = p_txDistribution;
        this.numRounds = numRounds;
    }

    public void setFollowees(boolean[] followees) {
        //Set followees and empty blacklist
        this.followees = followees;
        this.blackListed = new boolean[followees.length];
    }

    public void setPendingTransaction(Set<Transaction> pendingTransactions) {
        //Set the panding transactions
        this.pendingTransactions = pendingTransactions;
    }

    public Set<Transaction> sendToFollowers() {
        //send and clear transactions.
        Set<Transaction> toSend = new HashSet<>(pendingTransactions);
        pendingTransactions.clear();
        return toSend;
    }

    public void receiveFromFollowees(Set<Candidate> candidates) {
        Set<Integer> senders = candidates.stream().map(c -> c.sender).collect(toSet());

        //blacklist if candidate is not a followee
        for (int i = 0; i < followees.length; i++) {
            if (followees[i] && !senders.contains(i))
                blackListed[i] = true;
        }

        //
        for (Candidate c : candidates) {
            if (!blackListed[c.sender]) {
                pendingTransactions.add(c.tx);
            }
        }
    }
}