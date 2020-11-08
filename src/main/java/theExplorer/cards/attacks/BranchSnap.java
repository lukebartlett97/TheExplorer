package theExplorer.cards.attacks;

import basemod.abstracts.CustomCard;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import theExplorer.ExplorerMod;
import theExplorer.characters.TheExplorer;

import java.util.Iterator;

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
public class BranchSnap extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = ExplorerMod.makeID(BranchSnap.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    //TODO: Add real art.
    public static final String IMG = makeCardPath("BranchSnap.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.COMMON;
    private static final CardTarget TARGET = CardTarget.ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheExplorer.Enums.COLOR_GRAY;

    private static final int COST = 1;
    private static final int DAMAGE = 7;
    private static final int UPGRADE_DAMAGE = 3;

    // Hey want a second damage/magic/block/unique number??? Great!
    // Go check out DefaultAttackWithVariable and theExplorer.variable.DefaultCustomVariable
    // that's how you get your own custom variable that you can use for anything you like.
    // Feel free to explore other mods to see what variabls they personally have and create your own ones.

    // /STAT DECLARATION/

    public BranchSnap() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        // Aside from baseDamage/MagicNumber/Block there's also a few more.
        // Just type this.base and let intelliJ auto complete for you, or, go read up AbstractCard

        damage = baseDamage = DAMAGE;
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        if (m.hasPower(VulnerablePower.POWER_ID)) {
            int modifiedDamage = Math.round((new VulnerablePower(m, 1, false)).atDamageReceive(damage, DamageInfo.DamageType.NORMAL));
            addToBot(new DamageAction(m, new DamageInfo(p, modifiedDamage, this.damageTypeForTurn), AbstractGameAction.AttackEffect.BLUNT_HEAVY));
        } else {
            addToBot(new DamageAction(m, new DamageInfo(p, this.damage, this.damageTypeForTurn)));
        }

    }

    // Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            initializeDescription();
        }
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        AbstractPlayer player = AbstractDungeon.player;
        this.isDamageModified = false;
        if (mo != null) {
            float tmp = (float) this.baseDamage;
            Iterator var9 = player.relics.iterator();

            while (var9.hasNext()) {
                AbstractRelic r = (AbstractRelic) var9.next();
                tmp = r.atDamageModify(tmp, this);
                if (this.baseDamage != (int) tmp) {
                    this.isDamageModified = true;
                }
            }

            AbstractPower p;
            for (var9 = player.powers.iterator(); var9.hasNext(); tmp = p.atDamageGive(tmp, this.damageTypeForTurn, this)) {
                p = (AbstractPower) var9.next();
            }

            tmp = player.stance.atDamageGive(tmp, this.damageTypeForTurn, this);
            if (this.baseDamage != (int) tmp) {
                this.isDamageModified = true;
            }

            for (AbstractPower power : mo.powers) {
                float increased = power.atDamageReceive(tmp, this.damageTypeForTurn, this);
                float diff = increased - tmp;
                if (power.ID.equals(VulnerablePower.POWER_ID)) {
                    increased += diff;
                }
                tmp = increased;
            }

            for (var9 = player.powers.iterator(); var9.hasNext(); tmp = p.atDamageFinalGive(tmp, this.damageTypeForTurn, this)) {
                p = (AbstractPower) var9.next();
            }

            for (var9 = mo.powers.iterator(); var9.hasNext(); tmp = p.atDamageFinalReceive(tmp, this.damageTypeForTurn, this)) {
                p = (AbstractPower) var9.next();
            }

            if (tmp < 0.0F) {
                tmp = 0.0F;
            }

            if (this.baseDamage != MathUtils.floor(tmp)) {
                this.isDamageModified = true;
            }

            this.damage = MathUtils.floor(tmp);
        }
    }
}
