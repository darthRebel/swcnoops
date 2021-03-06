package swcnoops.server.session;

import swcnoops.server.datasource.GuildSettings;
import java.util.List;
import java.util.Map;

public interface GuildSession {
    String getGuildId();

    void join(PlayerSession playerSession);

    void troopsRequest(String playerId, String message, long time);

    String getGuildName();

    void processDonations(Map<String, Integer> troopsDonated, String requestId, PlayerSession playerSession, String recipientId, long time);

    void warMatchmakingStart(List<String> participantIds, boolean isSameFactionWarAllowed);

    void leave(PlayerSession playerSession);

    GuildSettings getGuildSettings();
}
