package theExplorer.cards.attacks;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDiscardAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.status.Burn;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theExplorer.ExplorerMod;
import theExplorer.actions.LeechAction;
import theExplorer.characters.TheExplorer;

import java.util.ArrayList;

import static theExplorer.ExplorerMod.makeCardPath;

public class ForestFire extends CustomCard {

    // TEXT DECLARATION

    public static final String ID = ExplorerMod.makeID(ForestFire.class.getSimpleName());
    private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);

    //TODO: Add real art.
    public static final String IMG = makeCardPath("Atk_Beta.png");

    public static final String NAME = cardStrings.NAME;
    public static final String DESCRIPTION = cardStrings.DESCRIPTION;

    private static final CardRarity RARITY = CardRarity.UNCOMMON;
    private static final CardTarget TARGET = CardTarget.ALL_ENEMY;
    private static final CardType TYPE = CardType.ATTACK;
    public static final CardColor COLOR = TheExplorer.Enums.COLOR_GRAY;

    private static final int COST = 2;
    private static final int DAMAGE = 18;
    private static final int UPGRADE_DAMAGE = 5;

    public ForestFire() {
        super(ID, NAME, IMG, COST, DESCRIPTION, TYPE, COLOR, RARITY, TARGET);

        damage = baseDamage = DAMAGE;
    }

    @Override
    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractCard> cardsPlayed = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        int amount = cardsPlayed.get(cardsPlayed.size() - 1).uuid.equals(this.uuid) ? cardsPlayed.size() - 1 : cardsPlayed.size();
        for(int i = 0; i < amount; i++) {
            addToBot(new DamageAllEnemiesAction(p, this.damage, this.damageTypeForTurn, AbstractGameAction.AttackEffect.FIRE));
            addToBot(new MakeTempCardInDrawPileAction(new Burn(), 1, true, true));
        }
    }

    @Override
    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upgradeDamage(UPGRADE_DAMAGE);
            initializeDescription();
        }
    }

    @Override
    public void applyPowers() {
        super.applyPowers();
        initializeDescription();
    }

    @Override
    public void initializeDescription() {
        this.rawDescription = cardStrings.DESCRIPTION;
        if(AbstractDungeon.actionManager != null &&
                AbstractDungeon.actionManager.cardsPlayedThisTurn != null &&
                AbstractDungeon.getCurrMapNode() != null &&
                AbstractDungeon.getCurrRoom() != null) {
            int count = AbstractDungeon.actionManager.cardsPlayedThisTurn.size();
            this.rawDescription = this.rawDescription + cardStrings.EXTENDED_DESCRIPTION[0] + count;
            if (count == 1) {
                this.rawDescription = this.rawDescription + cardStrings.EXTENDED_DESCRIPTION[1];
            } else {
                this.rawDescription = this.rawDescription + cardStrings.EXTENDED_DESCRIPTION[2];
            }
        }
        super.initializeDescription();
    }
}
