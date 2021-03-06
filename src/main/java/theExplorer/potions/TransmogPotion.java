package theExplorer.potions;

import com.megacrit.cardcrawl.actions.common.SuicideAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import theExplorer.ExplorerMod;
import theExplorer.actions.SpawnMonsterAction;
import theExplorer.characters.TheExplorer;

public class TransmogPotion extends AbstractPotion {

    public static final Logger logger = LogManager.getLogger(ExplorerMod.class.getName());

    public static final String POTION_ID = ExplorerMod.makeID(TransmogPotion.class.getSimpleName());
    private static final PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);

    public static final String NAME = potionStrings.NAME;
    public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;

    public TransmogPotion() {
        // The bottle shape and inside is determined by potion size and color. The actual colors are the main ExplorerMod.java
        super(NAME, POTION_ID, PotionRarity.RARE, PotionSize.M, PotionColor.SMOKE);

        // Potency is the damage/magic number equivalent of potions.
        potency = getPotency();

        // Initialize the Description
        description = DESCRIPTIONS[0];

        // Do you throw this potion at an enemy or do you just consume it.
        isThrown = true;
        targetRequired = true;

        // Initialize the on-hover name + description
        tips.add(new PowerTip(name, description));

    }

    @Override
    public void use(AbstractCreature target) {
        if (AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT && target instanceof AbstractMonster) {
            if (AbstractDungeon.player instanceof TheExplorer) {
                TheExplorer explorer = (TheExplorer) AbstractDungeon.player;
                Class<? extends AbstractCreature> randomKilled = explorer.getResearchStacksPower().getRandomKilled();
                addToBot(new SpawnMonsterAction(target.drawX, target.drawY, randomKilled));
                addToBot(new SuicideAction((AbstractMonster) target, false));
            }
        }
    }

    @Override
    public int getPotency(int i) {
        return 0;
    }

    @Override
    public AbstractPotion makeCopy() {
        return new TransmogPotion();
    }


    @Override
    public boolean canUse() {
        if (AbstractDungeon.player instanceof TheExplorer) {
            TheExplorer explorer = (TheExplorer) AbstractDungeon.player;
            if (explorer.getResearchStacksPower().hasKilled()) {
                return super.canUse();
            }
        }
        return false;
    }
}
