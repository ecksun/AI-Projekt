package sokoban;

import java.util.LinkedList;

/**
 * Contains information about a search, whether it is failed, reached a
 * solution or is inconclusive.
 */
public final class SearchInfo
{
    /**
     * The status of this search
     */
    public final SearchStatus status;

    /**
     * The solution
     */
    public final LinkedList<Board.Direction> solution;

    /**
     * Field describing that the search was inconclusive
     */
    public static SearchInfo Inconclusive = new SearchInfo(
            SearchStatus.Inconclusive);

    /**
     * Field indicating the search failed
     */
    public static SearchInfo Failed = new SearchInfo(SearchStatus.Failed);

    /**
     * Create a new SearchInfo with the specified searchstatus
     * 
     * @param status The status of this search
     */
    public SearchInfo(final SearchStatus status)
    {
        this.status = status;
        solution = null;
    }

    private SearchInfo()
    {
        status = SearchStatus.Solution;
        solution = new LinkedList<Board.Direction>();
    }

    /**
     * Create a new empty SearchInfo, which will be a solution
     * 
     * @return the new SearchInfo
     */
    public static SearchInfo emptySolution()
    {
        return new SearchInfo();
    }
}