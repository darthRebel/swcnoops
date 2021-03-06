package swcnoops.server.commands.guild;

import swcnoops.server.ServiceFactory;
import swcnoops.server.commands.guild.response.GuildResult;
import swcnoops.server.json.JsonParser;
import swcnoops.server.model.SquadMsgType;
import swcnoops.server.session.GuildSession;
import swcnoops.server.session.PlayerSession;

import java.util.List;

public class GuildWarMatchmakingStart extends GuildCommandAction<GuildWarMatchmakingStart, GuildResult> {
    private List<String> participantIds;
    private boolean isSameFactionWarAllowed;

    @Override
    protected GuildResult execute(GuildWarMatchmakingStart arguments, long time) throws Exception {
        // TODO - not complete currently does not do much except send notification
        // need to change login details to keep in this state
        PlayerSession playerSession = ServiceFactory.instance().getSessionManager()
                .getPlayerSession(arguments.getPlayerId());
        GuildSession guildSession = playerSession.getGuildSession();
        guildSession.warMatchmakingStart(arguments.getParticipantIds(), arguments.isSameFactionWarAllowed);
        GuildResult guildResult = new GuildResult(playerSession.getPlayerId(),
                playerSession.getPlayer().getPlayerSettings().getName(), guildSession);
        guildResult.setNotificationData(SquadMsgType.warMatchMakingBegin, null);
        return guildResult;
    }

    @Override
    protected GuildWarMatchmakingStart parseArgument(JsonParser jsonParser, Object argumentObject) {
        return jsonParser.fromJsonObject(argumentObject, GuildWarMatchmakingStart.class);
    }

    @Override
    public String getAction() {
        return "guild.war.matchmaking.start";
    }

    public List<String> getParticipantIds() {
        return participantIds;
    }

    public boolean isSameFactionWarAllowed() {
        return isSameFactionWarAllowed;
    }
}
