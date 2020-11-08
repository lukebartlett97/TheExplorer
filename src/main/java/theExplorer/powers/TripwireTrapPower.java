package theExplorer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import theExplorer.ExplorerMod;
import theExplorer.util.TextureLoader;

public class TripwireTrapPower extends ExplorerPower {
    public AbstractCreature source;

    public static final String POWER_ID = ExplorerMod.makeID(TripwireTrapPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture("theExplorerResources/images/powers/placeholder_power84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theExplorerResources/images/powers/placeholder_power32.png");

    private int damage;

    public TripwireTrapPower(final AbstractCreature owner, final AbstractCreature source, int amount, int damage) {
        name = NAME;
        ID = POWER_ID;
        setDescriptions(DESCRIPTIONS);
        this.owner = owner;
        this.amount = amount;
        this.source = source;

        type = PowerType.BUFF;
        isTurnBased = false;
        description = DESCRIPTIONS[0];

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        this.damage = damage;
        updateDescription();
    }

    @Override
    public int onAttacked(DamageInfo info, int damageAmount) {
        if (damageAmount > 0 && info.owner != null && info.type == DamageInfo.DamageType.NORMAL && amount > 0) {
            this.addToBot(new DamageAction(info.owner, new DamageInfo(owner, damage)));
            amount -= 1;
            if (amount == 0) {
                this.addToBot(new RemoveSpecificPowerAction(this.owner, this.owner, this));
            }
        }
        return damageAmount;
    }

    @Override
    public void updateDescription() {
        this.description = descriptions[0] + this.amount + descriptions[1] + this.damage + descriptions[2];
    }
}
