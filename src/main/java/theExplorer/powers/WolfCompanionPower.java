package theExplorer.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theExplorer.ExplorerMod;
import theExplorer.companions.CompanionCreature;
import theExplorer.util.TextureLoader;

import java.util.ArrayList;

public class WolfCompanionPower extends CompanionPower {
    public AbstractCreature source;

    public static final String POWER_ID = ExplorerMod.makeID(WolfCompanionPower.class.getSimpleName());
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

    // We create 2 new textures *Using This Specific Texture Loader* - an 84x84 image and a 32x32 one.
    private static final Texture tex84 = TextureLoader.getTexture("theExplorerResources/images/powers/placeholder_power84.png");
    private static final Texture tex32 = TextureLoader.getTexture("theExplorerResources/images/powers/placeholder_power32.png");
    private static final Texture companionIMG = TextureLoader.getTexture("theExplorerResources/images/companions/turtle/Turtle.png");

    public WolfCompanionPower(final AbstractCreature owner, final AbstractCreature source) {
        super(new CompanionCreature(companionIMG, DESCRIPTIONS[2]));
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
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) {
            act();
        }
    }

    @Override
    public void act() {
        ArrayList<AbstractMonster> monsters = new ArrayList<>();
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped() && monster.hasPower(ResearchingPower.POWER_ID)) {
                monsters.add(monster);
            }
        }
        if (!monsters.isEmpty()) {
            super.act();
            AbstractMonster chosen = monsters.get(AbstractDungeon.monsterRng.random(monsters.size() - 1));
            this.addToBot(new DamageAction(chosen, new DamageInfo(owner, getDamage(), DamageInfo.DamageType.THORNS)));
        }
    }

    private int getDamage() {
        return 2 + (2 * getEvolutionNumber());
    }

    @Override
    public void updateDescription() {
        this.description = descriptions[0] + getDamage() + descriptions[1];
    }
}
