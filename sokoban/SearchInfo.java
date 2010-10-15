package sokoban;

import java.util.LinkedList;

/**
 * Contains information about a search, whether it is failed, reached a
 * solution or is inconclusive.
 */
public final class SearchInfo
{
    public final SearchStatus status;
    public final LinkedList<Board.Direction> solution;

    public static SearchInfo Inconclusive = new SearchInfo(SearchStatus.Inconclusive);
    public static SearchInfo Failed = new SearchInfo(SearchStatus.Failed);

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

    public static SearchInfo emptySolution()
    {
        return new SearchInfo();
    }
}