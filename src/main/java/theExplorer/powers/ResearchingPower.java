package theExplorer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theExplorer.ExplorerMod;
import theExplorer.util.TextureLoader;

public class ResearchingPower extends ExplorerPower {
    public AbstractCreature source;

    public static final String POWER_ID = ExplorerMod.makeID(ResearchingPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture("theExplorerResources/images/powers/researching_power84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theExplorerResources/images/powers/researching_power32.png");

    public ResearchingPower(final AbstractCreature owner, final AbstractCreature source, int amount) {
        name = NAME;
        ID = POWER_ID;
        setDescriptions(DESCRIPTIONS);
        this.owner = owner;
        this.amount = amount;
        this.source = source;

        isTurnBased = false;
        description = DESCRIPTIONS[0];
        type = PowerType.DEBUFF;

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onDeath() {
        if (AbstractDungeon.player.hasPower(ResearchStacksPower.POWER_ID)) {
            AbstractPower power = AbstractDungeon.player.getPower(ResearchStacksPower.POWER_ID);
            ((ResearchStacksPower) power).onSpecificTrigger(owner, amount);
        }
    }
}
