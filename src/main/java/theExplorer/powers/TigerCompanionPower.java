package theExplorer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.StrengthPower;
import theExplorer.ExplorerMod;
import theExplorer.companions.CompanionCreature;
import theExplorer.util.OnShuffleListener;
import theExplorer.util.TextureLoader;

public class TigerCompanionPower extends CompanionPower implements OnShuffleListener {
    public AbstractCreature source;

    public static final String POWER_ID = ExplorerMod.makeID(TigerCompanionPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    int shuffleCount = 0;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture("theExplorerResources/images/powers/placeholder_power84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theExplorerResources/images/powers/placeholder_power32.png");
    private static final Texture companionIMG = TextureLoader.getTexture("theExplorerResources/images/companions/turtle/Turtle.png");

    public TigerCompanionPower(final AbstractCreature owner, final AbstractCreature source) {
        super(new CompanionCreature(companionIMG, DESCRIPTIONS[3]));
        name = NAME;
        ID = POWER_ID;
        setDescriptions(DESCRIPTIONS);
        this.owner = owner;
        this.source = source;
        this.amount = -1;

        type = PowerType.BUFF;
        isTurnBased = false;
        description = DESCRIPTIONS[0];

        // We load those textures here.
        this.region128 = new TextureAtlas.AtlasRegion(tex84, 0, 0, 84, 84);
        this.region48 = new TextureAtlas.AtlasRegion(tex32, 0, 0, 32, 32);

        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        super.onInitialApplication();
        shuffleCount = 0;
    }

    @Override
    public void onShuffle() {
        shuffleCount++;
        int shufflesNeeded = getShufflesNeeded();
        if (shuffleCount >= shufflesNeeded) {
            act();
            shuffleCount = 0;
        }
    }

    @Override
    public void act() {
        super.act();
        addToTop(new ApplyPowerAction(owner, owner, new StrengthPower(owner, getStrengthAdded())));
    }

    private int getShufflesNeeded() {
        return getEvolutionNumber() > 2 ? 1 : 4 - getEvolutionNumber();
    }

    private int getStrengthAdded() {
        return getEvolutionNumber() < 4 ? 1 : getEvolutionNumber() - 2;
    }

    @Override
    public void updateDescription() {
        this.description = descriptions[0] + getShufflesNeeded() + descriptions[1] + getStrengthAdded() + descriptions[2];
    }
}
