package sokoban;

public enum SearchStatus {
    /**
     * The search reached the maximum depth, and no solution was found,
     * so it's inconclusive (a solution could follow, but we don't know).
     */
    Inconclusive,

    /**
     * This search resulted in a solution.
     */
    Solution,

    /**
     * This search failed without reached the maximum depth, so there's
     * no point in trying it again with a greater search depth.
     */
    Failed,
};