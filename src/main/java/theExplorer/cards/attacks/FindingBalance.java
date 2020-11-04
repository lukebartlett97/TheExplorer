package theExplorer.cards.attacks;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import theExplorer.ExplorerMod;
import theExplorer.characters.TheExplorer;

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
public class FindingBalance extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = ExplorerMod.makeID(FindingBalance.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    //TODO: Add real art.
    public static final String IMG = makeCardPath("FindingBalance.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheExplorer.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int DAMAGE = 12;
    private static final int UPGRADE_DAMAGE = 3;
    private static final int BLOCK = 12;
    private static final int UPGRADE_BLOCK = 3;
    private static final int REDUCTION = 6;

    // Hey want a second damage/magic/block/unique number??? Great!
    // Go check out DefaultAttackWithVariable and theExplorer.variable.DefaultCustomVariable
    // that's how you get your own custom variable that you can use for anything you like.
    // Feel free to explore other mods to see what variabls they personally have and create your own ones.

    // /STAT DECLARATION/

    public FindingBalance() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        // Aside from baseDamage/MagicNumber/Block there's also a few more.
        // Just type this.base and let intelliJ auto complete for you, or, go read up AbstractCard

        damage = baseDamage = DAMAGE;
        block = baseBlock = BLOCK;
        baseMagicNumber = magicNumber = REDUCTION;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
        addToBot(new GainBlockAction(p, p, this.block));
    }

    private void setTempValues() {
        baseDamage = DAMAGE + (upgradedDamage ? UPGRADE_DAMAGE : 0);
        baseBlock = BLOCK + (upgradedBlock ? UPGRADE_BLOCK : 0);
        int skillCount = cardCount(CardType.SKILL);
        int attackCount = cardCount(CardType.ATTACK);
        if(skillCount < attackCount) {
            baseBlock -= REDUCTION;
        }
        if(attackCount < skillCount) {
            baseDamage -= REDUCTION;
        }
    }

    @Override
    public void triggerOnGlowCheck() {
        int skillCount = cardCount(CardType.SKILL);
        int attackCount = cardCount(CardType.ATTACK);
        this.glowColor = AbstractCard.BLUE_BORDER_GLOW_COLOR.cpy();
        if (skillCount == attackCount) {
            this.glowColor = AbstractCard.GOLD_BORDER_GLOW_COLOR.cpy();
        }
    }

    private void resetTempValues() {
        baseDamage = DAMAGE + (upgradedDamage ? UPGRADE_DAMAGE : 0);
        baseBlock = BLOCK + (upgradedBlock ? UPGRADE_BLOCK : 0);
        if(block != baseBlock) {
            isBlockModified = true;
        }
        if(damage != baseDamage) {
            isDamageModified = true;
        }
    }

    @Override
    public void applyPowers() {
        setTempValues();
        super.applyPowers();
        resetTempValues();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        setTempValues();
        super.calculateCardDamage(mo);
        resetTempValues();
    }

    private int cardCount(CardType cardType) {
        int out = 0;
        if(AbstractDungeon.isPlayerInDungeon()) {
            out += (int) AbstractDungeon.player.drawPile.group.stream().filter(x -> x.type == cardType).count();
            out += (int) AbstractDungeon.player.hand.group.stream().filter(x -> x.type == cardType).count();
            out += (int) AbstractDungeon.player.discardPile.group.stream().filter(x -> x.type == cardType).count();
        } else {
            out += (int) AbstractDungeon.player.masterDeck.group.stream().filter(x -> x.type == cardType).count();
        }
        return out;
    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            upgradeBlock(UPGRADE_BLOCK);
            initializeDescription();
        }
    }
}
