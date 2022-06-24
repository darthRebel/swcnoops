package swcnoops.server.game;

public interface BuildableData {
    String getUid();
    long getBuildingTime();
    int getSize();
    String getType();
    ContractType getContractType();
}
