package theExplorer.cards.attacks;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theExplorer.ExplorerMod;
import theExplorer.characters.TheExplorer;

import java.util.stream.Stream;

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
public class Highlander extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = ExplorerMod.makeID(Highlander.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    //TODO: Add real art.
    public static final String IMG = makeCardPath("Atk_Beta.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheExplorer.Enums.COLOR_GRAY;
    private boolean isHighlander = false;

    private static final int COST = 0;

    // Hey want a second damage/magic/block/unique number??? Great!
    // Go check out DefaultAttackWithVariable and theExplorer.variable.DefaultCustomVariable
    // that's how you get your own custom variable that you can use for anything you like.
    // Feel free to explore other mods to see what variabls they personally have and create your own ones.

    // /STAT DECLARATION/

    public Highlander() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        // Aside from baseDamage/MagicNumber/Block there's also a few more.
        // Just type this.base and let intelliJ auto complete for you, or, go read up AbstractCard

        setBases();
        isEthereal = true;
    }

    private void setBases() {
        int uniques = 0;
        int count = 0;
        AbstractPlayer p = AbstractDungeon.player;
        if (AbstractDungeon.isPlayerInDungeon() && p != null && p.drawPile != null) {
            Stream<String> drawIDs = p.drawPile.group.stream().map(x -> x.cardID);
            Stream<String> handIDs = p.hand.group.stream().map(x -> x.cardID);
            Stream<String> discardIDs = p.discardPile.group.stream().map(x -> x.cardID);
            uniques = (int) Stream.concat(drawIDs, Stream.concat(handIDs, discardIDs)).distinct().count();
            count = p.drawPile.size() + p.hand.size() + p.discardPile.size();
        }
        isHighlander = uniques == count;
        baseDamage = uniques;
        baseBlock = uniques;
    }

    private void setBattleDescription() {
        this.rawDescription = upgraded ? UPGRADE_DESCRIPTION : cardStrings.DESCRIPTION;
        this.rawDescription = this.rawDescription.replace("X damage", "X( !D! ) damage");
        this.rawDescription = this.rawDescription.replace("X Block", "X( !B! ) Block");
        this.initializeDescription();
    }

    @Override
    public void applyPowers() {
        setBases();
        setBattleDescription();
        super.applyPowers();
        this.initializeDescription();
    }

    @Override
    public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return isHighlander && super.canUse(p, m);
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        setBases();
        setBattleDescription();
        super.calculateCardDamage(mo);
        this.initializeDescription();
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (isHighlander) {
            addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
            addToBot(new GainBlockAction(p, p, this.block));
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        setBases();
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (isHighlander) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            isInnate = true;
            this.rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }
}
