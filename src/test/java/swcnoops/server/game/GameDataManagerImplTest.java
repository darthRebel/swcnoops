package swcnoops.server.game;

import org.junit.Test;
import swcnoops.server.Config;
import swcnoops.server.ServiceFactory;

import static org.junit.Assert.assertNotNull;

public class GameDataManagerImplTest {
    static {
        ServiceFactory.instance(new Config());
    }

    @Test
    public void test() {
        GameDataManager gameDataManager = new GameDataManagerImpl();
        gameDataManager.initOnStartup();
        TroopData troopData = gameDataManager.getTroopDataByUid("troopEmpireRageRancorCreature10");
        assertNotNull(troopData);
        BuildingData buildingData = gameDataManager.getBuildingDataByUid("empireTrapDropshipCreature10");
        assertNotNull(buildingData);
        troopData = gameDataManager.getTroopDataByUid("specialAttackEmpireCreatureDropship10");
        assertNotNull(troopData);
    }
}