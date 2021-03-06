package theExplorer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import theExplorer.ExplorerMod;
import theExplorer.util.TextureLoader;

public class QuestRepairTowerPower extends QuestPower {
    public AbstractCreature source;

    public static final String POWER_ID = ExplorerMod.makeID(QuestRepairTowerPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture("theExplorerResources/images/powers/placeholder_power84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theExplorerResources/images/powers/placeholder_power32.png");

    public QuestRepairTowerPower(final AbstractCreature owner, final AbstractCreature source) {
        name = NAME;
        ID = POWER_ID;
        setDescriptions(DESCRIPTIONS);
        this.owner = owner;
        this.source = source;
        this.countdown = 100;

        type = PowerType.BUFF;
        isTurnBased = false;
        description = DESCRIPTIONS[0];

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onGainedBlock(float blockGained) {
        if (this.countdown > blockGained) {
            this.countdown -= blockGained;
        } else {
            this.countdown = 0;
        }
        this.flash();
        if (this.countdown < 1) {
            addToBot(new ApplyPowerAction(owner, owner, new ConvertedBlockPower(owner, owner, 1)));
            addToBot(new RemoveSpecificPowerAction(owner, owner, this));
        }
        updateDescription();
    }
}
