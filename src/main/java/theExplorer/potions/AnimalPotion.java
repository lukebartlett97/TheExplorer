package theExplorer.potions;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theExplorer.ExplorerMod;
import theExplorer.util.CompanionService;

public class AnimalPotion extends AbstractPotion {

    public static final Logger logger = LogManager.getLogger(ExplorerMod.class.getName());

    public static final String POTION_ID = ExplorerMod.makeID(AnimalPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public AnimalPotion() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main ExplorerMod.java
        super(NAME, POTION_ID, PotionRarity.UNCOMMON, PotionSize.SNECKO, PotionColor.FRUIT);

        // Potency is the damage/magic number equivalent of potions.
        potency = getPotency();

        // Initialize the Description
        description = DESCRIPTIONS[0];

        // Do you throw this potion at an enemy or do you just consume it.
        isThrown = false;
        targetRequired = false;

        // Initialize the on-hover name + description
        tips.add(new PowerTip(name, description));

    }

    @Override
    public void use(AbstractCreature target) {
        if (CompanionService.getCompanionPower() != null) {
            CompanionService.getCompanionPower().addTemporaryEvolve(this.potency);
        }
    }

    @Override
    public int getPotency(int i) {
        return 1;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new AnimalPotion();
    }
}
