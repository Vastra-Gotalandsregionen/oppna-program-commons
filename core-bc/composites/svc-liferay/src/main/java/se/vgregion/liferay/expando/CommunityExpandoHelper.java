package se.vgregion.liferay.expando;

/**
 * Created by IntelliJ IDEA.
 * Created: 2011-11-19 17:29
 *
 * @author <a href="mailto:david.rosell@redpill-linpro.com">David Rosell</a>
 */
public interface CommunityExpandoHelper {
    <T> void set(String columnName, T value, long companyId, long communityId);

    <T> T get(String columnName, long companyId, long communityId);
}
