package theExplorer.relics;

import basemod.abstracts.CustomRelic;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.random.Random;
import theExplorer.ExplorerMod;
import theExplorer.powers.ResearchStacksPower;
import theExplorer.powers.ResearchingPower;
import theExplorer.util.TextureLoader;

import static theExplorer.ExplorerMod.logger;
import static theExplorer.ExplorerMod.makeRelicPath;

public class Journal extends CustomRelic {

    /*
     * https://github.com/daviscook477/BaseMod/wiki/Custom-Relics
     *
     * Gain 1 energy.
     */

    // ID, images, text.
    public static final String ID = ExplorerMod.makeID(Journal.class.getSimpleName());

    private static final Texture IMG = TextureLoader.getTexture(makeRelicPath("book.png"));

    public Journal() {
        super(ID, IMG, RelicTier.STARTER, LandingSound.MAGICAL);
    }

    private String recent = "";

    @Override
    public void onMonsterDeath(AbstractMonster m) {
        if (m.hasPower(ResearchingPower.POWER_ID)) {
            this.flash();
            for (int i = 0; i < m.getPower(ResearchingPower.POWER_ID).amount; i++) {
                chooseBonus();
                updateTips();
            }
        }
    }

    private void chooseBonus() {
        Random rng = AbstractDungeon.relicRng;
        int roll = rng.random(0, 100);
        logger.info("Journal Bonus rolled: " + roll);
        if (roll < 20) {
            AbstractDungeon.player.increaseMaxHp(2, true);
            recent = "Max HP +2";
        } else if (roll < 40) {
            if (AbstractDungeon.player.currentHealth < AbstractDungeon.player.maxHealth) {
                AbstractDungeon.player.heal(5);
                recent = "Heal 5";
            } else {
                chooseBonus();
            }
        } else if (roll < 60) {
            AbstractDungeon.player.gainGold(15);
            recent = "15 gold";
        } else if (roll < 80) {
            AbstractDungeon.getCurrRoom().addCardToRewards();
            recent = "Extra Card Loot";
        } else if (roll < 90) {
            AbstractDungeon.getCurrRoom().addRelicToRewards(RelicTier.COMMON);
            recent = "Extra Relic Loot";
        } else {
            AbstractPower power = AbstractDungeon.player.getPower(ResearchStacksPower.POWER_ID);
            recent = "Extra Research Stack";
            if (power != null) {
                power.onSpecificTrigger();
            }
        }
    }

    private void updateTips() {
        this.tips.clear();
        String desc = getUpdatedDescription();
        if (recent != null && !recent.equals("")) {
            desc += " Recently Received: " + recent;
        }
        this.tips.add(new PowerTip(name, desc));
    }

    // Description
    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

}
