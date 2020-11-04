package theExplorer.cards.skills;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import theExplorer.ExplorerMod;
import theExplorer.actions.MedleyBrewAction;
import theExplorer.cards.BrewCard;
import theExplorer.characters.TheExplorer;

import static theExplorer.ExplorerMod.makeCardPath;

public class MedleyBrew extends BrewCard {

    // TEXT DECLARATION

    public static final String ID = ExplorerMod.makeID(MedleyBrew.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    //TODO: Add real art.
    public static final String IMG = makeCardPath("Ski_Beta.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;
    public static final String UPGRADE_DESCRIPTION = cardStrings.UPGRADE_DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.RARE;
    private static final CardTarget TARGET = CardTarget.SELF;
    private static final CardType TYPE = CardType.SKILL;
    public static final CardColor COLOR = TheExplorer.Enums.COLOR_GRAY;

    private static final int COST = -1;

    // Hey want a second damage/magic/block/unique number??? Great!
    // Go check out DefaultAttackWithVariable and theExplorer.variable.DefaultCustomVariable
    // that's how you get your own custom variable that you can use for anything you like.
    // Feel free to explore other mods to see what variabls they personally have and create your own ones.

    // /STAT DECLARATION/

    public MedleyBrew() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);
        this.exhaust = true;
        // Aside from baseDamage/MagicNumber/Block there's also a few more.
        // Just type this.base and let intelliJ auto complete for you, or, go read up AbstractCard
    }

    // Actions the card should do.
    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        int effect = EnergyPanel.totalCount;
        if (this.energyOnUse != -1) {
            effect = this.energyOnUse;
        }
        if (p.hasRelic(ChemicalX.ID)) {
            effect += 2;
            p.getRelic(ChemicalX.ID).flash();
        }
        effect += upgraded ? 1 : 0;

        if (effect > 0) {
            for (int i = 0; i < effect; i++) {
                addToBot(new MedleyBrewAction(p, m, this));
            }

            if (!this.freeToPlayOnce) {
                p.energy.use(EnergyPanel.totalCount);
            }
        }

    }

    //Upgraded stats.
    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            rawDescription = UPGRADE_DESCRIPTION;
            initializeDescription();
        }
    }

    @Override
    public void onAttackDiscarded(AbstractPlayer p, AbstractMonster m) {
        AbstractCard card = AbstractDungeon.returnTrulyRandomCardInCombat(CardType.ATTACK).makeCopy();
        card.setCostForTurn(0);
        addToTop(new MakeTempCardInHandAction(card, true));
    }

    @Override
    public void onSkillDiscarded(AbstractPlayer p, AbstractMonster m) {
        AbstractCard card = AbstractDungeon.returnTrulyRandomCardInCombat(CardType.SKILL).makeCopy();
        card.setCostForTurn(0);
        addToTop(new MakeTempCardInHandAction(card, true));

    }

    @Override
    public void onPowerDiscarded(AbstractPlayer p, AbstractMonster m) {
        AbstractCard card = AbstractDungeon.returnTrulyRandomCardInCombat(CardType.POWER).makeCopy();
        card.setCostForTurn(0);
        addToTop(new MakeTempCardInHandAction(card, true));
    }

    @Override
    public void onOtherDiscarded(AbstractPlayer p, AbstractMonster m) {
        //Do Nothing
    }
}