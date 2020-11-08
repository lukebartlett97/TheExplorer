package theExplorer.cards.skills;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import theExplorer.ExplorerMod;
import theExplorer.cards.AbstractDynamicCard;
import theExplorer.characters.TheExplorer;
import theExplorer.util.ApplyPowerListener;

import static theExplorer.ExplorerMod.makeCardPath;
// "How come this card extends CustomCard and not DynamicCard like all the rest?"
// Skip this question until you start figuring out the AbstractDefaultCard/AbstractDynamicCard and just extend DynamicCard
// for your own ones like all the other cards.

// Well every card, at the end of the day, extends CustomCard.
// Abstract Default Card extends CustomCard and builds up on it, adding a second magic number. Your card can extend it and
// bam - you can have a second magic number in that card (Learn Java inheritance if you want to know how that works).
// Abstract Dynamic Card builds up on Abstract Default Card even more and makes it so that you don't need to add
// the NAME and the DESCRIPTION into your card - it'll get it automatically. Of course, this functionality could have easily
// Been added to the default card rather than creating a new Dynamic one, but was done so to deliberately.
public class Outbreak extends AbstractDynamicCard implements ApplyPowerListener {

    // TEXT DECLARATION

    public static final String ID = ExplorerMod.makeID(Outbreak.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    //TODO: Add real art.
    public static final String IMG = makeCardPath("Ski_Beta.png");

    public static final String NAME = cardStrings.NAME;

    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    // /TEXT DECLARATION/


    // STAT DECLARATION

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheExplorer.Enums.COLOR_GRAY;

    private static final int COST = 2;


    private static final int STARTING_POISON = 1;
    private static final int INCREASE = 1;
    private static final int INCREASE_UPGRADE = 1;

    // Hey want a second damage/magic/block/unique number??? Great!
    // Go check out DefaultAttackWithVariable and theExplorer.variable.DefaultCustomVariable
    // that's how you get your own custom variable that you can use for anything you like.
    // Feel free to explore other mods to see what variables they personally have and create your own ones.

    // /STAT DECLARATION/

    public Outbreak() {
        super(ID, IMG, COST, TYPE, COLOR, RARITY, TARGET);
        baseMagicNumber = magicNumber = STARTING_POISON;
        defaultBaseSecondMagicNumber = defaultSecondMagicNumber = INCREASE;

        // Aside from baseDamage/MagicNumber/Block there's also a few more.
        // Just type this.base and let intelliJ auto complete for you, or, go read up AbstractCard
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) {
                addToTop(new ApplyPowerAction(monster, p, new PoisonPower(monster, p, magicNumber)));
            }
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!this.upgraded) {
            upgradeName();
            upgradeDefaultSecondMagicNumber(INCREASE_UPGRADE);
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void onApplyPower(AbstractPower power, AbstractCreature target) {
        if (power.ID.equals(PoisonPower.POWER_ID) && !target.isPlayer) {
            magicNumber = baseMagicNumber += defaultSecondMagicNumber;
        }
        applyPowers();
        initializeDescription();
    }
}
