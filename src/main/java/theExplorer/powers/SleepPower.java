package theExplorer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.evacipated.cardcrawl.mod.stslib.powers.StunMonsterPower;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theExplorer.ExplorerMod;
import theExplorer.util.TextureLoader;

public class SleepPower extends ExplorerPower {
    private int sleepTimer;
    public AbstractCreature source;

    public static final String POWER_ID = ExplorerMod.makeID(SleepPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture[] tex84 = {
            TextureLoader.getTexture("theExplorerResources/images/powers/SleepPower1_128.png"),
            TextureLoader.getTexture("theExplorerResources/images/powers/SleepPower2_128.png"),
            TextureLoader.getTexture("theExplorerResources/images/powers/SleepPower3_128.png")
    };
    private static final Texture[] tex32 = {
            TextureLoader.getTexture("theExplorerResources/images/powers/SleepPower1_32.png"),
            TextureLoader.getTexture("theExplorerResources/images/powers/SleepPower2_32.png"),
            TextureLoader.getTexture("theExplorerResources/images/powers/SleepPower3_32.png")
    };

    public SleepPower(final AbstractCreature owner, final AbstractCreature source, int amount) {
        name = NAME;
        ID = POWER_ID;
        setDescriptions(DESCRIPTIONS);
        this.owner = owner;
        this.amount = amount;
        this.source = source;
        this.sleepTimer = 3;

        type = PowerType.DEBUFF;
        isTurnBased = false;
        description = DESCRIPTIONS[0];

        // We load those textures here.
        updateTextures();
        updateDescription();
    }

    private int getTextureIndex() {
        int index = sleepTimer - 1;
        if(index < 0) {
            index = 0;
        }
        if(index > 2) {
            index = 2;
        }
        return index;
    }

    private void updateTextures() {
        this.region128 = new TextureAtlas.AtlasRegion(tex84[getTextureIndex()], 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32[getTextureIndex()], 0, 0, 32, 32);
    }

    @Override
    public void atEndOfRound() {
        if (sleepTimer > 1) {
            sleepTimer--;
        } else {
            addToTop(new ApplyPowerAction(owner, source, new StunMonsterPower((AbstractMonster)owner, this.amount), this.amount));
            addToBot(new RemoveSpecificPowerAction(owner, owner, SleepPower.POWER_ID));
        }
        updateTextures();
        this.flash();
        updateParticles();
    }

    @Override
    public void updateDescription() {
        this.description = descriptions[0] + this.sleepTimer + descriptions[1] + this.amount + descriptions[2];
    }
}
