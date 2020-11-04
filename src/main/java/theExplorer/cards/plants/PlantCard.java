package theExplorer.cards.plants;

import basemod.abstracts.CustomCard;

public abstract class PlantCard extends CustomCard {
    String baseName;

    public PlantCard(String id, String name, String img, int cost, String rawDescription, CardType type, CardColor color, CardRarity rarity, CardTarget target) {
        super(id, name, img, cost, rawDescription, type, color, rarity, target);
        exhaust = true;
        baseName = name;
    }

    @Override
    protected void upgradeName() {
        ++this.timesUpgraded;
        this.upgraded = true;
        this.name = baseName + "+" + this.timesUpgraded;
        this.initializeTitle();
    }
}
